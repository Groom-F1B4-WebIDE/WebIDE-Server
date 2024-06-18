import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Home from './membercompo/Home';
import Signup from './membercompo/Signup';
import Login from './membercompo/Login';
import Main from './membercompo/main';

const App = () => {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/login" element={<Login />} />
      <Route path="/main" element={<Main />} />
    </Routes>
  );
};

export default App;