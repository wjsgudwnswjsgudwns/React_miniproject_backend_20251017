import api from "../api/axiosConfig";
import { useState } from "react";
import "./PostEdit.css";

function PostEdit({ post, setPost, setEdit }) {
  const [title, setTitle] = useState(post.title);
  const [content, setContent] = useState(post.content);

  const handleUpdate = async (e) => {
    e.preventDefault();

    if (!window.confirm("정말 수정하시겠습니까?")) {
      return;
    }

    try {
      const res = await api.put(`/api/board/${post.id}`, { title, content });
      setPost(res.data);
      setEdit(false);
      alert("수정되었습니다.");
    } catch (err) {
      console.error(err);
      if (err.response?.status === 403) {
        alert("수정 권한이 없습니다.");
      } else {
        alert("수정 실패 했습니다.");
      }
    }
  };

  return (
    <div className="post-edit-container">
      <form onSubmit={handleUpdate} className="post-edit-form">
        <input
          type="text"
          placeholder="제목"
          className="post-edit-title-input"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          required
        />
        <textarea
          placeholder="내용"
          className="post-edit-content-textarea"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          required
        />

        <div className="post-edit-button-group">
          <button
            type="button"
            className="post-edit-cancel-btn"
            onClick={() => setEdit(false)}
          >
            취소
          </button>
          <button type="submit" className="post-edit-submit-btn">
            수정
          </button>
        </div>
      </form>
    </div>
  );
}

export default PostEdit;
