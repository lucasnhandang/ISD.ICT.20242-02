import axios from 'axios';

// Axios instance for API calls
const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
});

// Global error handler
api.interceptors.response.use(
    (response) => response,
    (error) => {
      console.error('API Error:', error);
      return Promise.reject(error);
    }
);

// Health check
export const checkBackendConnection = async () => {
  try {
    await api.get('/health');
    return { connected: true, message: 'Successfully connected to backend' };
  } catch (error) {
    return {
      connected: false,
      message: 'Failed to connect to backend. Please check if the backend server is running.'
    };
  }
};

// Get products (all/filter/sort)
export const getProducts = async (page = 0, size = 12, category = null, sortBy = 'title', sortDirection = 'asc') => {
  try {
    const params = { page, size, sortBy, sortDirection };
    let endpoint;
    if (category && category !== 'all') {
      endpoint = `/products/category/${category}`;
    } else {
      endpoint = '/products/search';
      params.query = '';
    }
    const response = await api.get(endpoint, { params });
    return {
      content: response.data.content,
      totalPages: response.data.totalPages,
      totalElements: response.data.totalElements,
      size: response.data.size,
      number: response.data.number
    };
  } catch (error) {
    console.error('API Error:', error);
    throw new Error('Failed to fetch products');
  }
};

// Search products (optionally filter by category)
export const searchProducts = async (query, page = 0, size = 12, sortBy = 'title', sortDirection = 'asc', category = null) => {
  try {
    const params = { query, page, size, sortBy, sortDirection };
    if (category && category !== 'all') params.category = category;
    const response = await api.get('/products/search', { params });
    return response.data;
  } catch (error) {
    console.error('API Error:', error);
    throw new Error('Failed to search products');
  }
};

// Get products by category (with sorting)
export const getProductsByCategory = async (category, page = 0, size = 12, sortBy = 'title', sortDirection = 'asc') => {
  try {
    const response = await api.get(`/products/category/${category}`, {
      params: { page, size, sortBy, sortDirection }
    });
    return {
      content: response.data.content,
      totalPages: response.data.totalPages,
      totalElements: response.data.totalElements,
      size: response.data.size,
      number: response.data.number
    };
  } catch (error) {
    console.error('API Error:', error);
    throw new Error('Failed to fetch products by category');
  }
};

export default api;