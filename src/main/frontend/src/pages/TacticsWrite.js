import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";
import "./TacticsWrite.css";

function TacticsWrite({ user }) {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [errors, setErrors] = useState({});

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({});
    if (!user) {
      alert("로그인 후 작성 가능합니다.");
      return;
    }
    try {
      await api.post("/api/tactics", { title, content });
      navigate("/tactics");
    } catch (err) {
      if (err.response && err.response.status === 400) {
        setErrors(err.response.data);
      } else {
        console.error(err);
      }
    }
  };

  return (
    <div className="board-write-container">
      {" "}
      <h2>글쓰기</h2>
      <form onSubmit={handleSubmit} className="write-form">
        <input
          type="text"
          placeholder="제목"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        ></input>
        <textarea
          placeholder="내용"
          value={content}
          onChange={(e) => setContent(e.target.value)}
        ></textarea>
        {errors.title && <p className="error-message">{errors.title}</p>}{" "}
        {errors.content && <p className="error-message">{errors.content}</p>}{" "}
        <button type="submit" className="write-submit-btn">
          {" "}
          작성 완료
        </button>
      </form>
    </div>
  );
}

export default TacticsWrite;
