import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import api from "../api/axiosConfig";
import TacticsPostEdit from "../component/TacticsPostEdit";
import TacticsPostView from "../component/TacticsPostView";
import "./TacticsDetail.css";
import TacticsCommentWrite from "../component/TacticsCommentWrite";
import TacticsCommentList from "../component/TacticsCommentList";

function TacticsDetail({ user }) {
  const { id } = useParams();

  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [edit, setEdit] = useState(false);

  const [comments, setComments] = useState([]);

  // 특정 글 로딩
  const loadPost = async () => {
    try {
      setLoading(true);
      const res = await api.get(`/api/tactics/${id}`);
      setPost(res.data);
    } catch (err) {
      console.error(err);
      setError("존재하지 않는 게시글입니다.");
    } finally {
      setLoading(false);
    }
  };

  // 댓글 로딩
  const loadComment = async () => {
    try {
      const res = await api.get(`/api/tactics/comment/${id}`);
      setComments(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    loadPost();
    loadComment();
  }, [id]);

  if (loading) {
    return (
      <div className="board-detail-container">
        <p className="loading-message">게시글 불러오는 중...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="board-detail-container">
        <p className="error-message">{error}</p>
      </div>
    );
  }

  if (!post) {
    return (
      <div className="board-detail-container">
        <p className="error-message">존재하지 않는 글입니다.</p>
      </div>
    );
  }

  return (
    <div className="board-detail-container">
      <div className="post-detail-section">
        {edit ? (
          <TacticsPostEdit post={post} setPost={setPost} setEdit={setEdit} />
        ) : (
          <TacticsPostView
            post={post}
            user={user}
            setPost={setPost}
            setEdit={setEdit}
          />
        )}
      </div>

      <div className="comment-section">
        <div className="comment-header">
          <h3 className="comment-title">
            댓글 <span className="comment-count">({comments.length})</span>
          </h3>
        </div>

        {user ? (
          <div className="comment-write-section">
            <TacticsCommentWrite
              loadComment={loadComment}
              boardId={id}
              user={user}
            />
          </div>
        ) : (
          <div className="comment-login-notice">
            댓글을 작성하려면 <Link to="/login">로그인</Link>이 필요합니다.
          </div>
        )}

        <div className="comment-list-section">
          <TacticsCommentList
            loadComment={loadComment}
            user={user}
            comments={comments}
          />
        </div>
      </div>
    </div>
  );
}

export default TacticsDetail;
