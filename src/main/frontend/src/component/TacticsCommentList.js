import { useState } from "react";
import api from "../api/axiosConfig";
import "./TacticsCommentList.css";

function TacticsCommentList({ loadComment, user, comments }) {
  const [editCommentContent, setEditCommentContent] = useState("");
  const [editCommentId, setEditCommentId] = useState(null);

  const commentFormatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString("ko-KR");
  };

  // 삭제
  const handleCommentDelete = async (commentId) => {
    if (!window.confirm("댓글을 삭제하시겠습니까?")) {
      return;
    }
    try {
      await api.delete(`/api/tactics/comment/${commentId}`);
      alert("댓글이 삭제 되었습니다.");
      loadComment();
    } catch (err) {
      console.error(err);
      alert("댓글 삭제 권한이 없거나 삭제할 수 없는 댓글입니다.");
    }
  };

  // 수정
  const handleCommentUpdate = async (commentId) => {
    if (!editCommentContent.trim()) {
      alert("댓글 내용을 입력해주세요.");
      return;
    }

    try {
      await api.put(`/api/tactics/comment/${commentId}`, {
        content: editCommentContent,
      });
      setEditCommentId(null);
      setEditCommentContent("");
      loadComment();
      alert("댓글이 수정되었습니다.");
    } catch (err) {
      console.error(err);
      alert("댓글 수정에 실패했습니다.");
    }
  };

  const handleCommentEdit = (comment) => {
    setEditCommentId(comment.id);
    setEditCommentContent(comment.content);
  };

  const handleCancelEdit = () => {
    setEditCommentId(null);
    setEditCommentContent("");
  };

  return (
    <div className="comment-list-container">
      {comments.length === 0 ? (
        <p className="comment-list-empty">등록된 댓글이 없습니다.</p>
      ) : (
        <ul className="comment-list">
          {comments.map((comment) => (
            <li key={comment.id} className="comment-item">
              <div className="comment-header">
                <div className="comment-author-info">
                  <span className="comment-author">
                    {comment.author.nickname}
                  </span>
                  <span className="comment-date">
                    {commentFormatDate(comment.createDate)}
                  </span>
                </div>
              </div>

              {editCommentId === comment.id ? (
                <div className="comment-edit-form">
                  <textarea
                    className="comment-edit-textarea"
                    value={editCommentContent}
                    onChange={(e) => setEditCommentContent(e.target.value)}
                  />
                  <div className="comment-edit-actions">
                    <button
                      className="comment-cancel-btn"
                      onClick={handleCancelEdit}
                    >
                      취소
                    </button>
                    <button
                      className="comment-save-btn"
                      onClick={() => handleCommentUpdate(comment.id)}
                    >
                      저장
                    </button>
                  </div>
                </div>
              ) : (
                <div>
                  <div className="comment-content">{comment.content}</div>
                  {user === comment.author.userId && (
                    <div className="comment-actions">
                      <button
                        className="comment-edit-btn"
                        onClick={() => handleCommentEdit(comment)}
                      >
                        수정
                      </button>
                      <button
                        className="comment-delete-btn"
                        onClick={() => handleCommentDelete(comment.id)}
                      >
                        삭제
                      </button>
                    </div>
                  )}
                </div>
              )}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default TacticsCommentList;
