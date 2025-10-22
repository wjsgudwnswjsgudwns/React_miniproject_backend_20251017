import { useState, useEffect } from "react";
import api from "../api/axiosConfig";
import { useNavigate } from "react-router-dom";
import "./Board.css";

function Board({ user }) {
  const [posts, setPosts] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [searchType, setSearchType] = useState("title");
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalItems, setTotalItems] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const navigate = useNavigate();

  // 게시판 글 요청
  const loadPosts = async (
    page = 0,
    type = searchType,
    keyword = searchQuery
  ) => {
    try {
      setLoading(true);
      setError("");

      let url = `/api/board?page=${page}&size=10`;

      // 검색어가 있으면 검색 파라미터 추가
      if (keyword && keyword.trim()) {
        url += `&searchType=${type}&keyword=${encodeURIComponent(keyword)}`;
      }

      const res = await api.get(url);
      setPosts(res.data.posts);
      setCurrentPage(res.data.currentPage);
      setTotalPages(res.data.totalPages);
      setTotalItems(res.data.totalItems);
    } catch (err) {
      console.error(err);
      setError("게시글을 불러오는데 실패했습니다.");
      setPosts([]);
    } finally {
      setLoading(false);
    }
  };

  // 컴포넌트 마운트 시 게시글 로드
  useEffect(() => {
    loadPosts(currentPage);
  }, [currentPage]);

  // 검색 처리
  const handleSearch = (e) => {
    e.preventDefault();
    setCurrentPage(0); // 검색 시 첫 페이지로
    loadPosts(0, searchType, searchQuery);
  };

  // 글쓰기
  const handleWritePost = () => {
    if (!user) {
      alert("로그인이 필요합니다.");
      navigate("/login");
      return;
    }
    navigate("/board/write");
  };

  // 게시글 클릭
  const handlePostClick = (postId) => {
    navigate(`/board/${postId}`);
  };

  // 페이지 번호 표시 함수
  const getPageNumbers = () => {
    const start = Math.floor(currentPage / 10) * 10;
    const end = Math.min(start + 10, totalPages);
    const pages = [];
    for (let i = start; i < end; i++) {
      pages.push(i);
    }
    return pages;
  };

  return (
    <div className="board-container">
      <div className="board-header">
        <h1 className="board-title">자유게시판</h1>
        <form className="search-box" onSubmit={handleSearch}>
          <select
            className="search-select"
            value={searchType}
            onChange={(e) => setSearchType(e.target.value)}
          >
            <option value="title">제목</option>
            <option value="nickname">닉네임</option>
          </select>
          <input
            type="text"
            placeholder="Search"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
          <button type="submit">🔍</button>
        </form>
      </div>

      {error && (
        <div style={{ color: "red", padding: "20px", textAlign: "center" }}>
          {error}
        </div>
      )}

      {loading ? (
        <div style={{ padding: "40px", textAlign: "center" }}>로딩 중...</div>
      ) : (
        <>
          <table className="board-table">
            <thead>
              <tr>
                <th style={{ width: "80px" }}>No</th>
                <th style={{ width: "50%" }}>제목</th>
                <th style={{ width: "120px" }}>글쓴이</th>
                <th style={{ width: "120px" }}>작성시간</th>
                <th style={{ width: "80px" }}>조회수</th>
                <th style={{ width: "80px" }}>좋아요</th>
              </tr>
            </thead>
            <tbody>
              {posts.length === 0 ? (
                <tr>
                  <td
                    colSpan="6"
                    style={{ textAlign: "center", padding: "40px" }}
                  >
                    게시글이 없습니다.
                  </td>
                </tr>
              ) : (
                posts.map((post, index) => (
                  <tr key={post.id}>
                    <td>{totalItems - (currentPage * 10 + index)}</td>
                    <td
                      className="title"
                      onClick={() => handlePostClick(post.id)}
                    >
                      {post.title}
                      {post.commentCount > 0 && (
                        <span className="comment-count-badge">
                          {" "}
                          [{post.commentCount}]
                        </span>
                      )}
                      {post.views > 50 && <span className="hot-badge">🔥</span>}
                    </td>
                    <td>{post.author?.nickname}</td>
                    <td>{new Date(post.createDate).toLocaleDateString()}</td>
                    <td>{post.view || 0}</td>
                    <td>{post.likes || 0}</td>
                  </tr>
                ))
              )}
            </tbody>
          </table>

          <div className="pagination">
            <button
              onClick={() => setCurrentPage(0)}
              disabled={currentPage === 0}
            >
              &lt;&lt;
            </button>
            <button
              onClick={() => setCurrentPage(currentPage - 1)}
              disabled={currentPage === 0}
            >
              &lt;
            </button>

            {getPageNumbers().map((num) => (
              <button
                className={num === currentPage ? "active" : ""}
                key={num}
                onClick={() => setCurrentPage(num)}
              >
                {num + 1}
              </button>
            ))}

            <button
              onClick={() => setCurrentPage(currentPage + 1)}
              disabled={currentPage === totalPages - 1 || totalPages === 0}
            >
              &gt;
            </button>
            <button
              onClick={() => setCurrentPage(totalPages - 1)}
              disabled={currentPage === totalPages - 1 || totalPages === 0}
            >
              &gt;&gt;
            </button>
          </div>
        </>
      )}

      <button className="write-btn" onClick={handleWritePost}>
        글쓰기
      </button>
    </div>
  );
}

export default Board;
