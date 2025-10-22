import { useState } from "react";
import api from "../api/axiosConfig";
import "./CommentWrite.css";

function CommentWrite({ loadComment, boardId, user }) {
  const [newComment, setNewComment] = useState("");
  const [commentErrors, setCommentErrors] = useState({});

  const handleComment = async (e) => {
    e.preventDefault();
    setCommentErrors({});

    if (!user) {
      alert("로그인 한 후 댓글을 작성해 주세요.");
      return;
    }

    if (!newComment.trim()) {
      alert("댓글 내용을 입력해주세요.");
      return;
    }

    try {
      await api.post(`/api/comment/${boardId}`, { content: newComment });
      setNewComment("");
      loadComment();
    } catch (err) {
      if (err.response && err.response.status === 400) {
        setCommentErrors(err.response.data);
      } else {
        console.error(err);
        alert("댓글 등록 실패!");
      }
    }
  };

  return (
    <div className="comment-write-container">
      <form onSubmit={handleComment} className="comment-write-form">
        <textarea
          className="comment-write-textarea"
          placeholder="댓글을 입력하세요."
          value={newComment}
          onChange={(e) => setNewComment(e.target.value)}
        />
        {commentErrors.content && (
          <p className="comment-write-error">{commentErrors.content}</p>
        )}
        <div className="comment-write-actions">
          <button type="submit" className="comment-write-submit-btn">
            등록
          </button>
        </div>
      </form>
    </div>
  );
}

export default CommentWrite;
