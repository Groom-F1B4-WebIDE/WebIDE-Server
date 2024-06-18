import React, { useState } from 'react';
import api from '../api';

const Login = () => {
  const [formData, setFormData] = useState({
    memberEmail: '',
    memberPassword: ''
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post('/member/login', formData);
      console.log('로그인 성공:', response.data);
    } catch (error) {
      console.error('로그인 실패:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>로그인</h2>
      <div>
        <label>이메일:</label>
        <input type="email" name="memberEmail" value={formData.memberEmail} onChange={handleChange} />
      </div>
      <div>
        <label>비밀번호:</label>
        <input type="password" name="memberPassword" value={formData.memberPassword} onChange={handleChange} />
      </div>
      <button type="submit">로그인</button>
    </form>
  );
};

export default Login;
