import React, { useState } from "react";
import "./VideoUpload.css";

function VideoUpload() {
  const [selectedFile, setSelectedFile] = useState(null);
  const [progress, setProgress] = useState(0);
  const [uploading, setUploading] = useState(false);
  const [message, setMessage] = useState("");
  const [meta, setMeta] = useState({
    title: "",
    description: "",
  });

  function handleFileUpload(event) {
    setSelectedFile(event.target.files[0]);
  }

  function handleForm(formEvent) {
    formEvent.preventDefault();
    console.log("Form submitted with file:", selectedFile);
  }

  return (
    <div className="card">
      <form onSubmit={handleForm}>
        <div>
          <input type="text" placeholder="Enter title" />
          <textarea placeholder="Enter description" rows="4" cols="50" />
          <input
            onChange={handleFileUpload}
            className="file-input"
            type="file"
          />
          <button className="btn">Upload</button>
        </div>
      </form>
    </div>
  );
}

export default VideoUpload;
