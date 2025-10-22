import { Link } from "react-router-dom";
import "./Navbar.css";

function Navbar({ user, onLogout }) {
  return (
    <nav>
      <Link to="/">FM26</Link>
      <div>
        {!user && <Link to="/login">로그인</Link>}
        {user && <Link to="/tactics">전술 게시판</Link>}
        {user && <Link to="/board">자유 게시판</Link>}
        {user && <Link to="/buy">예약 구매</Link>}
        {user && <Link to="/myorder">주문 내역</Link>}
        {user && (
          <button className="logout-btn" onClick={onLogout}>
            로그아웃
          </button>
        )}
      </div>
    </nav>
  );
}

export default Navbar;
