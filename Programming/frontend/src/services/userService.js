import axios from 'axios';

// Create axios instance for user management
const userApi = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' }
});

export const userService = {
  // Create a new user (admin only)
  createUser: async (userData) => {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        throw new Error('No authentication token found');
      }

      console.log('Making API call to create user with token:', token.substring(0, 20) + '...');
      
      const response = await userApi.post('/admin/users', userData, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      console.log('API Response:', response);
      console.log('Response data:', response.data);
      
      return response.data;
    } catch (error) {
      console.error('userService.createUser error:', error);
      
      if (error.response) {
        // Server responded with error status
        console.error('Error response data:', error.response.data);
        console.error('Error response status:', error.response.status);
        throw error; // Re-throw to let the component handle it
      } else if (error.request) {
        // Network error
        console.error('Network error:', error.request);
        throw new Error('Network error. Please check your connection.');
      } else {
        // Other error
        console.error('Other error:', error.message);
        throw error;
      }
    }
  },

  // Get all available roles
  getAvailableRoles: () => {
    return [
      { value: 'ADMIN', label: 'Administrator' },
      { value: 'PRODUCT_MANAGER', label: 'Product Manager' }
    ];
  }
}; 