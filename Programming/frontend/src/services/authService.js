import api from './api';

// Authentication service
export const authService = {
  // Login user
  login: async (email, password) => {
    try {
      const response = await api.post('/auth/login', {
        email,
        password
      });
      
      // Store token in localStorage
      if (response.data.token) {
        localStorage.setItem('token', response.data.token);
        localStorage.setItem('userInfo', JSON.stringify(response.data.userInfo));
      }
      
      return response.data;
    } catch (error) {
      const message = error.response?.data?.message || error.message || 'Đã xảy ra lỗi khi đăng nhập';
      throw new Error(message);
    }
  },

  // Logout user
  logout: async () => {
    try {
      const token = localStorage.getItem('token');
      if (token) {
        await api.post('/auth/logout', {}, {
          headers: { Authorization: `Bearer ${token}` }
        });
      }
    } catch (error) {
      const message = error.response?.data?.message || error.message || 'Đã xảy ra lỗi khi đăng xuất';
      throw new Error(message);
    } finally {
      // Clear local storage regardless of API call success
      localStorage.removeItem('token');
      localStorage.removeItem('userInfo');
    }
  },

  // Validate token and get user info
  validateToken: async () => {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        throw new Error('No token found');
      }

      const response = await api.get('/auth/validate', {
        headers: { Authorization: `Bearer ${token}` }
      });
      
      return response.data;
    } catch (error) {
      localStorage.removeItem('token');
      localStorage.removeItem('userInfo');
      const message = error.response?.data?.message || error.message || 'Token không hợp lệ';
      throw new Error(message);
    }
  },

  // Get current user info from localStorage
  getCurrentUser: () => {
    try {
      const userInfo = localStorage.getItem('userInfo');
      return userInfo ? JSON.parse(userInfo) : null;
    } catch (error) {
      console.error('Error getting current user:', error);
      return null;
    }
  },

  // Check if user is authenticated
  isAuthenticated: () => {
    return !!localStorage.getItem('token');
  },

  // Get token
  getToken: () => {
    return localStorage.getItem('token');
  }
}; 