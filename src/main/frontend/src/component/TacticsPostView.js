import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";
import "./TacticsPostView.css";

function TacticsPostView({ post, user, setEdit, setPost }) {
  const navigate = useNavigate();

  const [liked, setLiked] = useState(false);
  const [disLiked, setDisLiked] = useState(false);

  const handleDelete = async () => {
    try {
      if (window.confirm("정말 삭제 하시겠습니까?")) {
        await api.delete(`/api/tactics/${post.id}`);
        alert("삭제 되었습니다.");
        navigate("/board");
      }
    } catch (err) {
      console.error(err);
      if (err.response?.status === 403) {
        alert("삭제 권한이 없습니다.");
      } else {
        alert("삭제 실패 했습니다.");
      }
    }
  };

  const handleLike = async () => {
    try {
      const res = await api.put(`/api/board/${post.id}/like`);
      setPost(res.data);
      setLiked(true);
    } catch (err) {
      console.error(err);
      if (err.response?.status === 400) {
        alert("이미 추천한 게시물 입니다.");
      } else if (err.response?.status === 401) {
        alert("로그인 후 추천 가능합니다.");
      } else {
        alert("추천 실패하였습니다.");
      }
    }
  };

  const handleDisLike = async () => {
    try {
      const res = await api.put(`/api/board/${post.id}/dislike`);
      setPost(res.data);
      setDisLiked(true);
    } catch (err) {
      console.error(err);
      if (err.response?.status === 400) {
        alert("이미 비추천한 게시물 입니다.");
      } else if (err.response?.status === 401) {
        alert("로그인 후 비추천 가능합니다.");
      } else {
        alert("비추천 실패하였습니다.");
      }
    }
  };

  const isAuthor = user && user === post?.author?.userId;

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString("ko-KR");
  };

  return (
    <div className="post-view-container">
      <div className="post-view-header">
        <h2 className="post-view-title">{post.title}</h2>
        <div className="post-view-meta">
          <span className="post-view-author">
            작성자: {post.author.nickname}
          </span>
          <span className="post-view-author">조회수: {post.view}</span>
          {post.createdAt && (
            <span className="post-view-date">{formatDate(post.createdAt)}</span>
          )}
        </div>
      </div>

      <div className="post-view-content">{post.content}</div>

      <div className="post-like-btn">
        <button
          className={`post-like-btn ${liked ? "liked" : ""}`}
          onClick={handleLike}
        >
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
            <path d="M14 9V5a3 3 0 0 0-3-3L8 9H4a1 1 0 0 0-1 1v10a1 1 0 0 0 1 1h10.28a2 2 0 0 0 1.94-1.5l2.33-8A2 2 0 0 0 16.61 9H14z" />
          </svg>
          {post.likes}
        </button>

        <button
          className={`post-like-btn ${disLiked ? "disliked" : ""}`}
          onClick={handleDisLike}
        >
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
            <path d="M10 15v4a3 3 0 0 0 3 3l3-7h4a1 1 0 0 0 1-1V4a1 1 0 0 0-1-1H9.72a2 2 0 0 0-1.94 1.5l-2.33 8A2 2 0 0 0 7.39 15H10z" />
          </svg>
          {post.dislikes}
        </button>
      </div>

      <div className="post-view-actions">
        <button
          className="post-view-back-btn"
          onClick={() => navigate("/board")}
        >
          목록
        </button>
        {isAuthor && (
          <div className="post-view-author-actions">
            <button
              className="post-view-edit-btn"
              onClick={() => setEdit(true)}
            >
              수정
            </button>
            <button className="post-view-delete-btn" onClick={handleDelete}>
              삭제
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

export default TacticsPostView;
