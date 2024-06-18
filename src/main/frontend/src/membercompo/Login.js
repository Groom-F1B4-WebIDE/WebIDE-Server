import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../api';
import './styles/Login.css';

const Login = () => {
  const [formData, setFormData] = useState({
    memberEmail: '',
    memberPassword: ''
  });
  const navigate = useNavigate();

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
      navigate('/main'); // 로그인 성공 후 main.js로 이동
    } catch (error) {
      console.error('로그인 실패:', error);
    }
  };

  return (
    <div className="login-container">
      <div className="login-info">
        <h2>계정을 입력해주세요!</h2>
        <p>좋은 하루 보내시길 바랍니다.</p>
      </div>
      <form onSubmit={handleSubmit} className="login-form">
        <div className="form-group">
          <label>E-mail</label>
          <input 
            type="email" 
            name="memberEmail" 
            placeholder="이메일을 입력해주세요" 
            value={formData.memberEmail} 
            onChange={handleChange} 
            required
          />
        </div>
        <div className="form-group">
          <label>Password</label>
          <input 
            type="password" 
            name="memberPassword" 
            placeholder="비밀번호를 입력해주세요" 
            value={formData.memberPassword} 
            onChange={handleChange} 
            required
          />
        </div>
        <button type="submit" className="login-button">로그인하기</button>
        <div className="signup-link">
          계정이 없으신가요? <Link to="/signup">회원가입하기</Link>
        </div>
      </form>
    </div>
  );
};

export default Login;
