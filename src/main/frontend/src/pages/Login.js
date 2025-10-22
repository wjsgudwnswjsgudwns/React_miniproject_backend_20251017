import { useState } from "react";
import api from "../api/axiosConfig";
import { Link, useNavigate } from "react-router-dom";
import "./Login.css";

function Login({ onLogin }) {
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      await api.post(
        "/api/auth/login",
        new URLSearchParams({ userId, password })
      );

      const res = await api.get("/api/auth/me");
      onLogin(res.data.userId);
      navigate("/", { replace: true });
    } catch (err) {
      console.error(err);
      alert("잘못된 아이디 또는 비밀번호 입니다.");
    }
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <div className="login-logo">FM26</div>
        <form onSubmit={handleLogin} className="login-form">
          <input
            type="text"
            className="login-input"
            value={userId}
            placeholder="아이디"
            onChange={(e) => setUserId(e.target.value)}
            required
          />
          <input
            type="password"
            className="login-input"
            value={password}
            placeholder="비밀번호"
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button type="submit" className="login-submit-btn">
            로그인
          </button>
        </form>
        <div className="login-signup-link">
          <span>아직 계정이 없으신가요? </span>
          <Link to="/signup"> 회원가입</Link>
        </div>
      </div>
    </div>
  );
}

export default Login;
