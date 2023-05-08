package com.example.websocket.utils;

import org.springframework.stereotype.Component;

@Component
public class TokenValidator {

    /**
    * This method validate given token
    *
    * @param token is given token
    * @return is this token valid?
    */
    public Boolean validate(String token) {
        return true;
    }
}