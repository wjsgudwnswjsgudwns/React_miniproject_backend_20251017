import { useState, useEffect } from "react";
import "./MyOrder.css";
import api from "../api/axiosConfig";

const MyOrder = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const response = await api.get("/api/orders/myorder");
        setOrders(response.data);
      } catch (err) {
        if (err.response && err.response.status === 204) {
          setOrders([]); // 주문 목록을 비우고
          setError("아직 주문 내역이 없습니다."); // 에러 메시지 설정
        } else if (err.response && err.response.data) {
          const errorMessage =
            typeof err.response.data === "string"
              ? err.response.data
              : err.response.data.message ||
                `오류 코드 ${err.response.status}: 서버에서 상세 메시지를 가져올 수 없습니다.`;
          setError(errorMessage);
        } else {
          setError("주문 정보를 불러오는 중 예상치 못한 오류가 발생했습니다.");
        }
      } finally {
        setLoading(false);
      }
    };
    fetchOrders();
  }, []);

  if (loading) {
    return <div className="myorder-container">주문 내역을 불러오는 중...</div>;
  }

  if (error || (orders.length === 0 && !loading)) {
    const displayMessage = error || "아직 주문 내역이 없습니다.";

    return (
      <div className="myorder-container">
        <h2>나의 주문 내역</h2>
        <p className="error-message">{displayMessage}</p>
      </div>
    );
  }

  return (
    <div className="myorder-container">
      <h2>나의 주문 내역</h2>
      <table className="order-table">
        <thead>
          <tr>
            <th>주문 번호</th>
            <th>에디션</th>
            <th>플랫폼</th>
            <th>결제 금액</th>
            <th>결제 수단</th>
            <th>상태</th>
            <th>주문 일시</th>
          </tr>
        </thead>
        <tbody>
          {orders.map((order) => (
            <tr key={order.id}>
              <td>{order.id}</td>
              <td>
                {order.editionName} ({order.editionType})
              </td>
              <td>
                {order.platformName} ({order.platformCode})
              </td>
              <td>{order.price}</td>
              <td>{order.paymentMethod}</td>
              <td>{order.status}</td>
              <td>{new Date(order.createdAt).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default MyOrder;
