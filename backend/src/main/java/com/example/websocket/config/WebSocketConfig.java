package com.example.websocket.config;

import com.example.websocket.repository.NotificationUserRepository;
import com.example.websocket.utils.TokenValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Objects;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final NotificationUserRepository notificationUserRepository;
    private final TokenValidator tokenValidator;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .addInterceptors(new HandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                        String authorizationHeader = request.getHeaders().getFirst("Authorization");

                        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();
                        String token = queryParams.getFirst("token");

                        if (token != null && tokenValidator.validate(token)) {
                            log.info("TOKEN {}",  token);
                            return true;
                        } else {
                            return false;
                        }
                        /*
                        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                            String jwtToken = authorizationHeader.substring(7);
                            //Authentication authentication = JwtUtils.parseJwtToken(jwtToken); // Assuming you have a JwtUtils class that can parse the JWT token and return an Authentication object.
                            //SecurityContextHolder.getContext().setAuthentication(authentication);
                        } else {
                            return false;
                        }

                        */
                    }

                    @Override
                    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
                    }
                })
                .setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                log.info("{ user: {}, command: {} }", accessor.getLogin(), accessor.getCommand());

                if (accessor.getCommand() == StompCommand.CONNECT) {
                    Long userId = Long.parseLong(accessor.getLogin());
                    notificationUserRepository.add(userId);
                }
                else if (accessor.getCommand() == StompCommand.DISCONNECT) {
                    Long userId = Long.parseLong(accessor.getLogin());
                    notificationUserRepository.remove(userId);
                }
            }
        });
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/chatroom","/user");
        registry.setUserDestinationPrefix("/user");
    }
}
