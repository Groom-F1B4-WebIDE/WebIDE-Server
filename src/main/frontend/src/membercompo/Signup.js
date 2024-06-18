import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';
import './styles/Signup.css';

const Signup = () => {
  const [formData, setFormData] = useState({
    memberEmail: '',
    memberPassword: '',
    memberName: '',
    phoneNumber: '',
    gender: ''
  });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const response = await api.post('/member/signup', formData);
      console.log('회원가입 성공:', response.data);
      navigate('/login'); 
    } catch (error) {
      if (error.response && error.response.status === 400) {
        setError(error.response.data);
      } else {
        console.error('회원가입 실패:', error);
      }
    }
  };

  return (
    <div className="signup-container">
      <form onSubmit={handleSubmit} className="signup-form">
        <h2>회원가입</h2>
        {error && <p className="error-message">{error}</p>}
        <div className="form-group">
          <label>이메일:</label>
          <input 
            type="email" 
            name="memberEmail" 
            value={formData.memberEmail} 
            onChange={handleChange} 
            required
          />
        </div>
        <div className="form-group">
          <label>비밀번호:</label>
          <input 
            type="password" 
            name="memberPassword" 
            value={formData.memberPassword} 
            onChange={handleChange} 
            required 
            minLength="8" 
            maxLength="20" 
            pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" 
            title="비밀번호는 8자 이상, 하나 이상의 대문자, 소문자, 숫자를 포함해야 합니다." 
          />
        </div>
        <div className="form-group">
          <label>이름:</label>
          <input 
            type="text" 
            name="memberName" 
            value={formData.memberName} 
            onChange={handleChange} 
            required 
            minLength="2" 
            maxLength="50" 
          />
        </div>
        <div className="form-group">
          <label>전화번호:</label>
          <input 
            type="tel" 
            name="phoneNumber" 
            value={formData.phoneNumber} 
            onChange={handleChange} 
            required 
            pattern="[0-9]{3}-[0-9]{3,4}-[0-9]{4}" 
            title="전화번호는 000-0000-0000 형식이어야 합니다." 
          />
        </div>
        <div className="form-group">
          <label>성별:</label>
          <label>
            <input 
              type="radio" 
              name="gender" 
              value="male" 
              checked={formData.gender === 'male'} 
              onChange={handleChange} 
              required 
            />
            남성
          </label>
          <label>
            <input 
              type="radio" 
              name="gender" 
              value="female" 
              checked={formData.gender === 'female'} 
              onChange={handleChange} 
              required 
            />
            여성
          </label>
        </div>
        <button type="submit" className="signup-button">회원가입</button>
      </form>
    </div>
  );
};

export default Signup;