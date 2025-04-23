import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";
import VideoUpload from "./components/VideoUpload";

function App() {
  const [count, setCount] = useState(0);

  return (
    <>
      <div>
        <h1 className="font-extrabold text-gray-400">
          Welcome to Stream svc application
        </h1>
        <VideoUpload />
      </div>
    </>
  );
}

export default App;
