import SockJS from "sockjs-client";
import Button from "./components/Button";
import ButtonGroup from "./components/ButtonGroup";
import Container from "./components/Container";
import TextInput from "./components/TextInput";
import { useState } from "react";
import { over } from "stompjs";

var stompClient =null;

function App() {
  const token = "asasdasd";
  const [status, setStatus] = useState(false);
  const [username, setUsername] = useState('');
  
  const connect = () => {
    let Sock = new SockJS(`http://localhost:8080/ws?token=${token}`);
    stompClient = over(Sock);
    stompClient.connect({ login: "1", token }, onConnected, onError);
  };

  const onConnected = () => {
    setStatus(true)
    stompClient.subscribe("/chatroom/public", onMessageReceived);
    stompClient.subscribe(`/user/${username}/private`, onPrivateMessage);
  };

  const onError = (err) => {
    console.log(err);
  }

  const onMessageReceived = (msg) => {
    console.log({msg});
  }

  const onPrivateMessage = (msg) => {
    console.log({msg});
  }

  return (
    <Container>
      <form
        onSubmit={(e) => {
          e.preventDefault();

          const username = e.target.username.value;

          console.log({ username });

          setUsername(username);
          connect();
        }}
      >
        <TextInput name="username" placeholder={"type your id"} />
        <ButtonGroup>
          <Button type={"submit"}>Connect</Button>
        </ButtonGroup>
      </form>
    </Container>
  );
}

export default App;
