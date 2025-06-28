import React from 'react';

function PaymentResult() {
  const params = new URLSearchParams(window.location.search);
  const result = params.get('result');
  const message = params.get('message');

  return (
    <div style={{ textAlign: 'center', marginTop: 80 }}>
      <h1>Kết quả thanh toán VNPay</h1>
      <h2 style={{ color: result === 'success' ? 'green' : 'red' }}>
        {message || (result === 'success' ? 'Thanh toán thành công!' : 'Thanh toán thất bại!')}
      </h2>
      <a href="/">Quay về trang chủ</a>
    </div>
  );
}

export default PaymentResult; 