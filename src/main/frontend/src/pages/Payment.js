import { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import "./Payment.css";

function Payment({ user }) {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const platform = searchParams.get("platform");
  const edition = searchParams.get("edition");
  const price = searchParams.get("price");

  const [paymentMethod, setPaymentMethod] = useState("card");
  const [agree, setAgree] = useState(false);
  const [processing, setProcessing] = useState(false);

  const platformNames = {
    steam: "Steam",
    epic: "Epic Games",
    playstation: "PlayStation Store",
    xbox: "Xbox Store",
    ios: "App Store",
    android: "Google Play",
  };

  const editionNames = {
    standard: "스탠다드 에디션",
    console: "콘솔 에디션",
    touch: "터치 에디션",
  };

  useEffect(() => {
    if (!user) {
      alert("로그인이 필요합니다.");
      navigate("/login");
    }
  }, [user, navigate]);

  const handlePayment = async (e) => {
    e.preventDefault();

    if (!agree) {
      alert("구매 약관에 동의해주세요.");
      return;
    }

    setProcessing(true);

    try {
      const response = await fetch("http://localhost:8888/api/orders/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          userId: user, // 로그인한 사용자 아이디
          edition: edition,
          platform: platform,
          price: price,
          paymentMethod: paymentMethod,
        }),
      });

      if (!response.ok) throw new Error("서버 요청 실패");
      const result = await response.json();

      alert(`결제가 완료되었습니다!\n주문번호: ${result.id}`);
      navigate("/");
    } catch (error) {
      alert("결제 중 오류가 발생했습니다: " + error.message);
    } finally {
      setProcessing(false);
    }
  };

  return (
    <div className="payment-container">
      <div className="payment-content">
        <div className="payment-header">
          <h1>결제하기</h1>
          <button className="back-btn" onClick={() => navigate("/buy")}>
            ← 돌아가기
          </button>
        </div>

        <div className="payment-grid">
          {/* 주문 정보 */}
          <div className="order-summary">
            <h2>주문 정보</h2>
            <div className="order-details">
              <div className="order-item">
                <span className="order-label">상품</span>
                <span className="order-value">{editionNames[edition]}</span>
              </div>
              <div className="order-item">
                <span className="order-label">플랫폼</span>
                <span className="order-value">{platformNames[platform]}</span>
              </div>
              <div className="order-item">
                <span className="order-label">구매자</span>
                <span className="order-value">{user}</span>
              </div>
              <div className="order-divider"></div>
              <div className="order-item total">
                <span className="order-label">총 결제 금액</span>
                <span className="order-value price">{price}</span>
              </div>
            </div>
          </div>

          {/* 결제 수단 */}
          <div className="payment-method-section">
            <h2>결제 수단</h2>
            <form onSubmit={handlePayment}>
              <div className="payment-methods">
                <label
                  className={`payment-method-card ${
                    paymentMethod === "card" ? "selected" : ""
                  }`}
                >
                  <input
                    type="radio"
                    name="payment"
                    value="card"
                    checked={paymentMethod === "card"}
                    onChange={(e) => setPaymentMethod(e.target.value)}
                  />
                  <div className="method-info">
                    <span className="method-icon">💳</span>
                    <span className="method-name">신용/체크카드</span>
                  </div>
                </label>

                <label
                  className={`payment-method-card ${
                    paymentMethod === "bank" ? "selected" : ""
                  }`}
                >
                  <input
                    type="radio"
                    name="payment"
                    value="bank"
                    checked={paymentMethod === "bank"}
                    onChange={(e) => setPaymentMethod(e.target.value)}
                  />
                  <div className="method-info">
                    <span className="method-icon">🏦</span>
                    <span className="method-name">계좌이체</span>
                  </div>
                </label>

                <label
                  className={`payment-method-card ${
                    paymentMethod === "kakaopay" ? "selected" : ""
                  }`}
                >
                  <input
                    type="radio"
                    name="payment"
                    value="kakaopay"
                    checked={paymentMethod === "kakaopay"}
                    onChange={(e) => setPaymentMethod(e.target.value)}
                  />
                  <div className="method-info">
                    <span className="method-icon">💬</span>
                    <span className="method-name">카카오페이</span>
                  </div>
                </label>

                <label
                  className={`payment-method-card ${
                    paymentMethod === "toss" ? "selected" : ""
                  }`}
                >
                  <input
                    type="radio"
                    name="payment"
                    value="toss"
                    checked={paymentMethod === "toss"}
                    onChange={(e) => setPaymentMethod(e.target.value)}
                  />
                  <div className="method-info">
                    <span className="method-icon">💙</span>
                    <span className="method-name">토스페이</span>
                  </div>
                </label>
              </div>

              <div className="terms-section">
                <label className="terms-checkbox">
                  <input
                    type="checkbox"
                    checked={agree}
                    onChange={(e) => setAgree(e.target.checked)}
                  />
                  <span>구매 조건 및 환불 정책에 동의합니다.</span>
                </label>
                <p className="terms-text">
                  • 디지털 콘텐츠 특성상 구매 후 14일 이내, 플레이 시간 2시간
                  미만일 경우에만 환불 가능합니다.
                  <br />• 결제 완료 후 게임 라이브러리에 자동으로 추가됩니다.
                </p>
              </div>

              <button
                type="submit"
                className="payment-submit-btn"
                disabled={processing}
              >
                {processing ? "결제 처리 중..." : `${price} 결제하기`}
              </button>
            </form>
          </div>
        </div>

        <div className="payment-info">
          <h3>🔒 안전한 결제</h3>
          <p>모든 결제 정보는 SSL 암호화로 안전하게 보호됩니다.</p>
        </div>
      </div>
    </div>
  );
}

export default Payment;
