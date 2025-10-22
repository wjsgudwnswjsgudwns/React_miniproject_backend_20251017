// src/Home.js
import { Link } from "react-router-dom";
import "./Home.css";
// 배경 이미지 파일 경로 (src/images/Home.jpg)
import HomeBackground from "../images/Home.jpg";

function Home({ user }) {
  return (
    <div className="home-container">
      {/* 1. 히어로 섹션: 여기에만 배경 이미지를 적용합니다. */}
      <header
        className="hero-section"
        style={{ backgroundImage: `url(${HomeBackground})` }}
      >
        <h1 className="hero-title">FM26</h1>
        <p className="hero-subtitle">
          새로운 미래. 새로운 규칙. 당신의 이야기.
        </p>
        <div className="hero-actions">
          {/* 실제 페이지 경로에 맞게 Link to를 수정하세요 */}
          <Link to="/buy" className="pre-purchase-btn">
            지금 선구매
          </Link>
          <Link to="/tactics" className="learn-more-btn">
            전술 게시판
          </Link>
          <Link to="/board" className="learn-more-btn">
            자유 게시판
          </Link>
        </div>
      </header>

      {/* 2. 주요 기능 섹션: 나머지 섹션은 순수한 어두운 배경색을 유지합니다. */}
      <section className="features-section">
        <h2 className="features-title">주요 혁신</h2>
        <div className="feature-grid">
          <div className="feature-card">
            <h3>재정비된 UI ✨</h3>
            <p>더 명확하고 유동적인 인터페이스로 중요한 결정을 제어하세요.</p>
          </div>
          <div className="feature-card">
            <h3>전술 진화 ⚽</h3>
            <p>보유/비보유 전술로 현대 축구 감독처럼 팀을 설정하세요.</p>
          </div>
          <div className="feature-card">
            <h3>매치데이 경험 🏟️</h3>
            <p>Unity 엔진 기반의 향상된 모션과 디테일로 몰입감을 높입니다.</p>
          </div>
        </div>
      </section>

      {/* 3. 커뮤니티/뉴스 섹션 */}
      <section className="community-section">
        <h2>커뮤니티 및 뉴스</h2>
        <p>새로운 소식과 독점 정보를 확인하고 FMFC에 가입하세요.</p>
        {/* 실제 경로에 맞게 Link to를 수정하세요 */}

        {!user && (
          <Link to="/signup" className="join-fmfc-btn">
            FMFC 가입하기
          </Link>
        )}
      </section>
    </div>
  );
}

export default Home;
