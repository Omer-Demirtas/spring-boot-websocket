import Button from "./components/Button";
import ButtonGroup from "./components/ButtonGroup";
import Container from "./components/Container";
import TextInput from "./components/TextInput";

function App() {
  const token = "asasdasd";

  return (
    <Container>
      <form
        onSubmit={(e) => {
          e.preventDefault();

          const username = e.target.username.value;

          console.log({ username });

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
