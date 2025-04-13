import React from 'react';
import './RoleSelection.css';
import { useNavigate } from 'react-router-dom';

export const RoleSelection: React.FC = () => {
  const navigate = useNavigate();

  const handleRoleSelect = (role: string) => {
    if (role === 'Customer') {
      // Customer goes directly to home
      navigate('/home');
    } else {
      // Admin and Product Manager need to log in
      navigate('/login', { state: { role } });
    }
  };

  return (
    <div className="role-container">
      <div className="role-left">
        <h1 className="role-title">Select your role</h1>
        <p className="role-subtitle">
          Select  your role to have a full digital experience in AIMS
        </p>
        
        <div className="role-buttons">
          <button 
            className="role-button" 
            onClick={() => handleRoleSelect('Customer')}
          >
            Customer
          </button>
          <button 
            className="role-button" 
            onClick={() => handleRoleSelect('Product Manager')}
          >
            Product Manager
          </button>
          <button 
            className="role-button" 
            onClick={() => handleRoleSelect('Admin')}
          >
            Admin
          </button>
        </div>
      </div>
      
      <div className="role-right">
        <div className="circle-background">
        <img 
            src=''
            alt="AIMS App" 
            className="app-image" 
          />
        </div>
        
        <div className="feature-description">
          <h2 className="feature-title">Lorem ipsum dolor sit amet</h2>
          <p className="feature-subtitle">Lorem ipsum dolor sit amet, consectetur adipiscing elit</p>
          
          <div className="brand-logo">AIMS</div>
        </div>
      </div>
    </div>
  );
};