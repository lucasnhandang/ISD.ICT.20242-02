import axios from 'axios';

// Axios instance for API calls
const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  timeout: 30000,
  // headers: { 'Content-Type': 'application/json' },
  withCredentials: true // Tạm thời comment để test CORS issue
});

// Add token to requests if available
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Global error handler
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      // Handle specific error cases
      switch (error.response.status) {
        case 401:
          // Unauthorized - chỉ redirect nếu có token (token hết hạn)
          // Không redirect cho login API vì login thất bại là bình thường
          const token = localStorage.getItem('token');
          if (token && !error.config.url.includes('/auth/login')) {
            localStorage.removeItem('token');
            localStorage.removeItem('userInfo');
            window.location.href = '/login';
          }
          break;
        case 403:
          // Forbidden - show error message
          console.error('Access denied');
          break;
        case 400:
          // Bad request - likely validation error
          console.error('Validation error:', error.response.data);
          break;
        case 404:
          // Not found
          console.error('Resource not found:', error.response.data);
          break;
        case 409:
          // Conflict - likely product availability issue
          console.error('Product availability conflict:', error.response.data);
          break;
        default:
          // Other errors
          console.error('API Error:', error.response.data);
      }
    }
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

// Product APIs
export const getProductDetails = async (id) => {
  try {
    const response = await api.get(`/products/${id}`);
    return response;
  } catch (error) {
    console.error('API Error:', error);
    throw new Error('Failed to fetch product details');
  }
};

export const getProductAvailability = async (id) => {
  try {
    console.log('Checking availability for product:', id); // Debug log
    const response = await api.get(`/products/${id}/availability`);
    console.log('Raw API response:', response); // Debug log
    return response;
  } catch (error) {
    console.error('API Error:', error);
    throw new Error('Failed to check product availability');
  }
};

// Cart APIs
export const getCart = async () => {
  try {
    const response = await api.get('/cart');
    return response;
  } catch (error) {
    console.error('API Error:', error);
    throw new Error('Failed to fetch cart');
  }
};

export const addToCart = async (item) => {
  try {
    const response = await api.post('/cart/add', item);
    return response;
  } catch (error) {
    if (error.response?.status === 409) {
      throw new Error('Product is not available in the requested quantity');
    }
    console.error('API Error:', error);
    throw new Error('Failed to add item to cart');
  }
};

export const updateCartItem = async (item) => {
  try {
    const response = await api.post('/cart/update', item);
    return response;
  } catch (error) {
    if (error.response?.status === 409) {
      throw new Error('Product is not available in the requested quantity');
    }
    console.error('API Error:', error);
    throw new Error('Failed to update cart item');
  }
};

export const removeFromCart = async (productId) => {
  try {
    const response = await api.delete(`/cart/items/${productId}`);
    return response;
  } catch (error) {
    console.error('API Error:', error);
    throw new Error('Failed to remove item from cart');
  }
};

export const clearCart = async () => {
  try {
    const response = await api.delete('/cart/clear');
    return response;
  } catch (error) {
    console.error('API Error:', error);
    throw new Error('Failed to clear cart');
  }
};

// Auth APIs
export const login = async (credentials) => {
  try {
    const response = await api.post('/auth/login', credentials);
    return response;
  } catch (error) {
    console.error('API Error:', error);
    throw new Error('Failed to login');
  }
};

export const logout = async () => {
  try {
    const response = await api.post('/auth/logout');
    return response;
  } catch (error) {
    console.error('API Error:', error);
    throw new Error('Failed to logout');
  }
};

export const validateToken = async () => {
  try {
    const response = await api.get('/auth/validate');
    return response;
  } catch (error) {
    console.error('API Error:', error);
    throw new Error('Failed to validate token');
  }
};

// Order Management APIs
export const orderManagementAPI = {
  getPendingOrders: async () => {
    try {
      console.log('🔄 Đang gọi API getPendingOrders...');
      console.log('📍 URL:', `${api.defaults.baseURL}/product-manager/orders/pending`);
      
      const response = await api.get('/product-manager/orders/pending');
      console.log('✅ Thành công getPendingOrders:', response.data);
      return response.data;
    } catch (error) {
      console.error('❌ Lỗi getPendingOrders:', error);
      console.error('📝 Chi tiết lỗi:', {
        message: error.message,
        status: error.response?.status,
        statusText: error.response?.statusText,
        data: error.response?.data,
        config: {
          url: error.config?.url,
          method: error.config?.method,
          headers: error.config?.headers,
          baseURL: error.config?.baseURL
        }
      });
      
      // Thêm thông tin lỗi chi tiết hơn
      if (error.code === 'ERR_NETWORK') {
        throw new Error('Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng.');
      } else if (error.response?.status === 401) {
        throw new Error('Bạn cần đăng nhập để xem thông tin này.');
      } else if (error.response?.status === 403) {
        throw new Error('Bạn không có quyền truy cập chức năng này.');
      } else if (error.response?.status === 404) {
        throw new Error('Không tìm thấy API endpoint.');
      } else if (error.response?.status >= 500) {
        throw new Error('Lỗi server. Vui lòng thử lại sau.');
      }
      
      throw new Error(error.response?.data?.message || 'Không thể tải danh sách đơn hàng chờ duyệt');
    }
  },

  approveOrder: async (orderId) => {
    try {
      const response = await api.put(`/product-manager/orders/${orderId}/approve`);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to approve order');
    }
  },

  rejectOrder: async (orderId) => {
    try {
      const response = await api.put(`/product-manager/orders/${orderId}/reject`);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to reject order');
    }
  }
};

// Place Order APIs
export const submitDeliveryForm = async (data) => {
  return api.post('/place-order/submit-form', data);
};

export const checkRushOrderEligibility = async () => {
  return api.post('/place-rush-order/check-eligibility');
};

export const handleNormalOrder = async (cart) => {
  return api.post('/place-order/normal-order', cart);
};

export const submitRushOrderInfo = async (data) => {
  return api.post('/place-rush-order/submit-rush-info', data);
};

export const processRushOrder = async () => {
  return api.post('/place-rush-order/process-rush-order');
};

export const saveRushOrders = async () => {
  return api.post('/place-rush-order/save-rush-orders');
};

export const requestToPlaceOrder = async (cart) => {
  return api.post('/place-order/request', cart);
};

// Pay individual invoice for rush order
export const payInvoice = async (invoiceId) => {
  return api.post('/place-rush-order/pay-invoice', null, {
    params: { invoiceId }
  });
};

// Create separate axios instance for payment APIs
const paymentApi = axios.create({
  baseURL: 'http://localhost:8080/api/v1/payment',
  timeout: 10000,
  // headers: { 'Content-Type': 'application/json' },
  withCredentials: true
});

// Add token interceptor for payment API as well
paymentApi.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// VnPay Payment APIs
export const createVnPayUrl = async (paymentData) => {
  return paymentApi.post('/vnpay-create', paymentData);
};

export const getPaymentResult = async (txnRef, responseCode) => {
  return paymentApi.get('/payment-result', {
    params: { txnRef, responseCode }
  });
};

export const getPaymentStatus = async (txnRef) => {
  return paymentApi.get('/payment-status', {
    params: { txnRef }
  });
};

export default api;