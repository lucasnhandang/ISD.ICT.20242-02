import axios from 'axios';

// Create axios instance with base URL
const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1', // Update with your backend URL
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);

export const checkBackendConnection = async () => {
  try {
    const response = await api.get('/health');
    return { connected: true, message: 'Successfully connected to backend' };
  } catch (error) {
    return { 
      connected: false, 
      message: 'Failed to connect to backend. Please check if the backend server is running.'
    };
  }
};

export const getProducts = async (page = 0, size = 12, category = null, sortBy = null, sortDirection = 'asc') => {
  try {
    const params = { page, size };
    let endpoint;

    // Determine which endpoint to use based on category and sort requirements
    if (category && category !== 'all') {
      // Category filtering - use category endpoint
      endpoint = `/products/category/${category}`;
      // Note: Category endpoint doesn't support sorting in backend
    } else if (sortBy) {
      // Sorting required - use search endpoint with empty query
      endpoint = '/products/search';
      params.query = ''; // Empty query to get all products with sorting
      params.sortBy = sortBy;
      params.sortDirection = sortDirection;
    } else {
      // No category, no sorting - use home endpoint for random products
      endpoint = '/home';
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

export const searchProducts = async (query, page = 0, size = 12, sortBy = 'title', sortDirection = 'asc') => {
  try {
    const response = await api.get('/products/search', {
      params: {
        query,
        page,
        size,
        sortBy,
        sortDirection
      }
    });
    return response.data;
  } catch (error) {
    console.error('API Error:', error);
    throw new Error('Failed to search products');
  }
};

// New function for getting products by category with potential sorting support
export const getProductsByCategory = async (category, page = 0, size = 12) => {
  try {
    const response = await api.get(`/products/category/${category}`, {
      params: { page, size }
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

// New function for getting sorted products (uses search endpoint with empty query)
export const getSortedProducts = async (page = 0, size = 12, sortBy = 'title', sortDirection = 'asc') => {
  try {
    const response = await api.get('/products/search', {
      params: {
        query: '', // Empty query to get all products
        page,
        size,
        sortBy,
        sortDirection
      }
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
    throw new Error('Failed to fetch sorted products');
  }
};

export default api;