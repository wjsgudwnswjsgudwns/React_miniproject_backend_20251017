import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Buy.css";

function Buy({ user }) {
  const navigate = useNavigate();
  const [selectedEdition, setSelectedEdition] = useState("standard");
  const [showPlatformSelect, setShowPlatformSelect] = useState(false);

  const editions = {
    standard: {
      name: "스탠다드 에디션",
      price: "₩50,000",
      features: [
        "FM26 풀 게임",
        "온라인 경력 모드",
        "에디터 및 커스텀 데이터베이스",
        "Steam Workshop 지원",
      ],
      platforms: [
        { name: "Steam", icon: "🎮", link: "/payment?platform=steam" },
        { name: "Epic Games", icon: "🎯", link: "/payment?platform=epic" },
      ],
    },
    console: {
      name: "콘솔 에디션",
      price: "₩55,000",
      features: [
        "FM26 콘솔 버전",
        "간소화된 인터페이스",
        "컨트롤러 최적화",
        "빠른 게임 진행",
      ],
      platforms: [
        {
          name: "PlayStation",
          icon: "🎮",
          link: "/payment?platform=playstation",
        },
        { name: "Xbox", icon: "🎯", link: "/payment?platform=xbox" },
      ],
    },
    touch: {
      name: "터치 에디션",
      price: "₩35,000",
      features: [
        "FM26 터치 버전",
        "태블릿 최적화",
        "빠른 플레이 모드",
        "모바일 친화적 UI",
      ],
      platforms: [
        { name: "App Store", icon: "🍎", link: "/payment?platform=ios" },
        { name: "Google Play", icon: "📱", link: "/payment?platform=android" },
      ],
    },
  };

  const handlePurchaseClick = () => {
    if (!user) {
      alert("구매하려면 로그인이 필요합니다.");
      navigate("/login");
      return;
    }

    setShowPlatformSelect(true);
  };

  const handlePlatformSelect = (link) => {
    navigate(
      `${link}&edition=${selectedEdition}&price=${encodeURIComponent(
        editions[selectedEdition].price
      )}`
    );
  };

  return (
    <div className="buy-container">
      <div className="buy-hero">
        <h1 className="buy-title">FM26 지금 구매하기</h1>
        <p className="buy-subtitle">당신의 축구 관리 여정을 시작하세요</p>
      </div>

      <div className="editions-section">
        <h2 className="section-title">에디션 선택</h2>

        <div className="editions-grid">
          {Object.entries(editions).map(([key, edition]) => (
            <div
              key={key}
              className={`edition-card ${
                selectedEdition === key ? "selected" : ""
              }`}
              onClick={() => {
                setSelectedEdition(key);
                setShowPlatformSelect(false);
              }}
            >
              <div className="edition-header">
                <h3 className="edition-name">{edition.name}</h3>
                <p className="edition-price">{edition.price}</p>
              </div>

              <ul className="edition-features">
                {edition.features.map((feature, index) => (
                  <li key={index}>
                    <span className="check-icon">✓</span>
                    {feature}
                  </li>
                ))}
              </ul>

              {selectedEdition === key && (
                <div className="selected-badge">선택됨</div>
              )}
            </div>
          ))}
        </div>
      </div>

      <div className="purchase-action">
        <div className="selected-info">
          <h3>{editions[selectedEdition].name}</h3>
          <p className="selected-price">{editions[selectedEdition].price}</p>
        </div>
        <button className="purchase-btn" onClick={handlePurchaseClick}>
          {showPlatformSelect ? "플랫폼을 선택하세요" : "구매하기"}
        </button>
      </div>
      {showPlatformSelect && (
        <div className="platform-select-section">
          <h2 className="section-title">플랫폼 선택</h2>
          <div className="platform-buttons">
            {editions[selectedEdition].platforms.map((platform, index) => (
              <button
                key={index}
                className="platform-purchase-btn"
                onClick={() => handlePlatformSelect(platform.link)}
              >
                <span className="platform-btn-icon">{platform.icon}</span>
                <span className="platform-btn-text">
                  {platform.name}에서 구매
                </span>
              </button>
            ))}
          </div>
        </div>
      )}

      <div className="platforms-section">
        <h2 className="section-title">사용 가능한 플랫폼</h2>
        <div className="platforms-grid">
          <div className="platform-card">
            <div className="platform-icon">🖥️</div>
            <h3>PC</h3>
            <p>Steam, Epic Games</p>
          </div>
          <div className="platform-card">
            <div className="platform-icon">🎮</div>
            <h3>콘솔</h3>
            <p>PlayStation, Xbox</p>
          </div>
          <div className="platform-card">
            <div className="platform-icon">📱</div>
            <h3>모바일</h3>
            <p>iOS, Android</p>
          </div>
          <div className="platform-card">
            <div className="platform-icon">🍎</div>
            <h3>Mac</h3>
            <p>macOS 지원</p>
          </div>
        </div>
      </div>

      <div className="system-requirements">
        <h2 className="section-title">시스템 요구 사항</h2>
        <div className="requirements-grid">
          <div className="requirement-card">
            <h3>최소 사양</h3>
            <ul>
              <li>
                <strong>OS:</strong> Windows 10 (64-bit)
              </li>
              <li>
                <strong>프로세서:</strong> Intel Core i3
              </li>
              <li>
                <strong>메모리:</strong> 4 GB RAM
              </li>
              <li>
                <strong>그래픽:</strong> Intel HD Graphics 4000
              </li>
              <li>
                <strong>저장공간:</strong> 7 GB
              </li>
            </ul>
          </div>
          <div className="requirement-card">
            <h3>권장 사양</h3>
            <ul>
              <li>
                <strong>OS:</strong> Windows 11 (64-bit)
              </li>
              <li>
                <strong>프로세서:</strong> Intel Core i5
              </li>
              <li>
                <strong>메모리:</strong> 8 GB RAM
              </li>
              <li>
                <strong>그래픽:</strong> NVIDIA GTX 1050
              </li>
              <li>
                <strong>저장공간:</strong> 10 GB
              </li>
            </ul>
          </div>
        </div>
      </div>

      <div className="faq-section">
        <h2 className="section-title">자주 묻는 질문</h2>
        <div className="faq-list">
          <div className="faq-item">
            <h4>환불 정책은 어떻게 되나요?</h4>
            <p>
              구매 후 14일 이내, 플레이 시간 2시간 미만인 경우 환불이
              가능합니다.
            </p>
          </div>
          <div className="faq-item">
            <h4>에디션 간 차이점은 무엇인가요?</h4>
            <p>
              스탠다드 에디션은 PC용 풀 버전, 콘솔 에디션은 콘솔 최적화, 터치
              에디션은 모바일/태블릿용입니다.
            </p>
          </div>
          <div className="faq-item">
            <h4>멀티플레이가 가능한가요?</h4>
            <p>
              네, 온라인 경력 모드를 통해 친구들과 함께 플레이할 수 있습니다.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Buy;
