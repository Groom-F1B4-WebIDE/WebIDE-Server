
import Signup from './membercompo/Signup';
import Login from './membercompo/Login';
import { Route, Routes, Link } from 'react-router-dom';

import React, { useState } from 'react';

const Home = () => {
  return (
    <div>
      <h1>회원관리 시스템</h1>
      <Link to="/login"><button>로그인</button></Link>
      <Link to="/signup"><button>회원가입</button></Link>
    </div>
  );
};

const App = () => {
  return (

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
      </Routes>
  );
};

export default App;