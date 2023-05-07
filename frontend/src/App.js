import Button from "./components/Button";
import ButtonGroup from "./components/ButtonGroup";
import Container from "./components/Container";
import TextInput from "./components/TextInput";

function App() {
  return (
    <Container>
      <TextInput placeholder={"type your id"} />
      <ButtonGroup>
        <Button>Connect</Button>
      </ButtonGroup>
    </Container>
  );
}

export default App;
