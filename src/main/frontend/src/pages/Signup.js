import { useState } from "react";
import api from "../api/axiosConfig";
import { Link, useNavigate } from "react-router-dom";
import "./Signup.css";

function Signup() {
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");
  const [passwordCheck, setPasswordCheck] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [nickname, setNickname] = useState("");

  const [errors, setErrors] = useState({});

  const navigate = useNavigate();

  const handleSignup = async (e) => {
    e.preventDefault();
    setErrors({});
    try {
      await api.post("/api/auth/signup", {
        userId,
        password,
        email,
        phone,
        passwordCheck,
        nickname,
      });
      alert("회원 가입 성공");
      navigate("/login");
    } catch (err) {
      if (err.response && err.response.status === 400) {
        setErrors(err.response.data);
      } else {
        console.error("회원가입실패 : ", err);
        alert("회원 가입 실패");
      }
    }
  };

  return (
    <div className="signup-container">
      <div className="signup-box">
        <h2 className="signup-title">회원가입</h2>
        <form onSubmit={handleSignup} className="signup-form">
          <input
            type="text"
            className="signup-input"
            placeholder="아이디"
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
            required
          />
          {errors.userIdDuplicated && (
            <p className="signup-error">{errors.userIdDuplicated}</p>
          )}

          <input
            type="text"
            className="signup-input"
            placeholder="닉네임"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            required
          />
          {errors.nicknameDuplicated && (
            <p className="signup-error">{errors.nicknameDuplicated}</p>
          )}

          <input
            type="password"
            className="signup-input"
            placeholder="비밀번호"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <input
            type="password"
            className="signup-input"
            placeholder="비밀번호 확인"
            value={passwordCheck}
            onChange={(e) => setPasswordCheck(e.target.value)}
            required
          />
          {errors.passwordNotSame && (
            <p className="signup-error">{errors.passwordNotSame}</p>
          )}

          <input
            type="email"
            className="signup-input"
            placeholder="이메일"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          {errors.email && <p className="signup-error">{errors.email}</p>}

          <input
            type="tel"
            className="signup-input"
            placeholder="핸드폰 번호 (01012345678)"
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
            required
          />
          {errors.phone && <p className="signup-error">{errors.phone}</p>}

          <button type="submit" className="signup-submit-btn">
            회원가입
          </button>
        </form>
        <div className="signup-login-link">
          이미 계정이 있으신가요?
          <Link to="/login">로그인</Link>
        </div>
      </div>
    </div>
  );
}

export default Signup;
