import React, { useState } from 'react';
import './Login.css';
import { useNavigate, useLocation } from 'react-router-dom';

export const Login: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const role = location.state?.role || 'Staff'; // Default to Staff if no role is passed

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Login attempt:', { email, password, role });
    
    // Here you would handle authentication
    // For demo purposes, we'll just navigate to the home page
    window.electronAPI.login(role, { email, password })
      .then((result: any) => {
        if (result.success) {
          navigate('/home');
        } else {
          // Handle login failure
          console.error('Login failed');
        }
      });
  };

  const handleBack = () => {
    navigate('/');
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  return (
    <div className="login-container">
      <div className="login-left">
        <div className="login-header">
          <button className="back-button" onClick={handleBack}>
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M19 12H5M5 12L12 19M5 12L12 5" stroke="#4A5568" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
            </svg>
          </button>
        </div>
        
        <div className="login-form-container">
          <h1 className="login-title">Sign in</h1>
          <p className="login-subtitle">
          To become an admin or product manager, you need to log in.
          </p>
          
          <form className="login-form" onSubmit={handleLogin}>
            <div className="form-group">
              <input 
                type="email" 
                placeholder="Email" 
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
            
            <div className="form-group password-group">
              <input 
                type={showPassword ? "text" : "password"} 
                placeholder="Password" 
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              <button 
                type="button" 
                className="toggle-password" 
                onClick={togglePasswordVisibility}
                aria-label={showPassword ? "Hide password" : "Show password"}
              >
                {showPassword ? (
                  <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M2.5 10C2.5 10 5 3.75 10 3.75C15 3.75 17.5 10 17.5 10C17.5 10 15 16.25 10 16.25C5 16.25 2.5 10 2.5 10Z" stroke="#A0AEC0" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
                    <path d="M10 12.5C11.3807 12.5 12.5 11.3807 12.5 10C12.5 8.61929 11.3807 7.5 10 7.5C8.61929 7.5 7.5 8.61929 7.5 10C7.5 11.3807 8.61929 12.5 10 12.5Z" stroke="#A0AEC0" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
                    <path d="M4 4L16 16" stroke="#A0AEC0" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
                  </svg>
                ) : (
                  <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M2.5 10C2.5 10 5 3.75 10 3.75C15 3.75 17.5 10 17.5 10C17.5 10 15 16.25 10 16.25C5 16.25 2.5 10 2.5 10Z" stroke="#A0AEC0" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
                    <path d="M10 12.5C11.3807 12.5 12.5 11.3807 12.5 10C12.5 8.61929 11.3807 7.5 10 7.5C8.61929 7.5 7.5 8.61929 7.5 10C7.5 11.3807 8.61929 12.5 10 12.5Z" stroke="#A0AEC0" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
                  </svg>
                )}
              </button>
            </div>
            
            <button type="submit" className="signin-button">
              Sign in
            </button>
          </form>
        </div>
      </div>
      
      <div className="login-right">
        <div className="circle-background">
          <div className="phone-container">
          </div>
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