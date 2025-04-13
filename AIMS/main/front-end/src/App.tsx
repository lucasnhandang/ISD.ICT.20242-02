import React from 'react';
import './index.css';
import { RoleSelection } from './pages/login/RoleSelection';
import { HashRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Login } from './pages/login/Login';


const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<RoleSelection />} />
        <Route path="/login" element={<Login />} />
      </Routes>
    </Router>
  );
};

export default App;