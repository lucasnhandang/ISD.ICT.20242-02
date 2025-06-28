import api from './api';
import { authService } from './authService';

const getUserId = () => {
  const user = authService.getCurrentUser();
  return user?.id;
};

// Helper: chuẩn hóa dữ liệu trước khi gửi lên backend
function normalizeProductData(data) {
  const result = {};
  for (const key in data) {
    let value = data[key];
    if (value === undefined || value === null || value === '') continue;
    // Ép kiểu double cho weight
    if (key === 'weight') {
      value = parseFloat(value);
      if (isNaN(value)) continue;
    }
    // Ép kiểu int cho các trường int
    else if ([
      'value', 'currentPrice', 'quantity', 'pages', 'runtime'
    ].includes(key) && value !== '') {
      value = parseInt(value, 10);
      if (isNaN(value)) continue;
    }
    // Ép kiểu boolean
    if (key === 'rushOrderSupported') {
      value = value === true || value === 'true';
    }
    // Ngày tháng: chỉ lấy yyyy-MM-dd
    if ((/Date$/i.test(key) || key === 'entryDate' || key === 'publicationDate' || key === 'releaseDate') && typeof value === 'string') {
      value = value.slice(0, 10);
    }
    result[key] = value;
  }
  return result;
}

export const productService = {
  // Lấy danh sách sản phẩm (search/filter/paging)
  searchProducts: async (params) => {
    try {
      const response = await api.get('/products/search', { params });
      return response.data;
    } catch (error) {
      const message = error.response?.data?.message || error.message || 'Đã xảy ra lỗi khi tìm kiếm sản phẩm';
      throw new Error(message);
    }
  },

  // Lấy chi tiết sản phẩm
  getProductDetail: async (id) => {
    try {
      const response = await api.get(`/product-manager/products/${id}`);
      return response.data;
    } catch (error) {
      const message = error.response?.data?.message || error.message || 'Đã xảy ra lỗi khi lấy chi tiết sản phẩm';
      throw new Error(message);
    }
  },

  // Tạo sản phẩm mới
  createProduct: async (data, imageFile) => {
    try {
      const formData = new FormData();
      const normalized = normalizeProductData(data);
      
      // Tạo Blob cho JSON data với Content-Type đúng
      const jsonBlob = new Blob([JSON.stringify(normalized)], {
        type: 'application/json'
      });
      
      // Append data như một file với tên "data"
      formData.append('data', jsonBlob);
      
      // Append image file nếu có
      if (imageFile instanceof File) {
        formData.append('image', imageFile);
      }
      
      const response = await api.post('/product-manager/products', formData, {
        headers: {
          // Không set Content-Type - để browser tự động set cho multipart/form-data
        },
      });
      return response.data;
    } catch (error) {
      const message = error.response?.data?.message || error.message || 'Đã xảy ra lỗi khi tạo sản phẩm';
      throw new Error(message);
    }
  },

  // Cập nhật sản phẩm
  updateProduct: async (id, data, imageFile) => {
    try {
      const userId = getUserId();
      const formData = new FormData();
      const normalized = normalizeProductData(data);
      
      // Tạo Blob cho JSON data
      const jsonBlob = new Blob([JSON.stringify(normalized)], {
        type: 'application/json'
      });
      
      formData.append('data', jsonBlob);
      
      if (imageFile instanceof File) {
        formData.append('image', imageFile);
      }
      
      const response = await api.put(`/product-manager/products/${id}`, formData, {
        headers: {
          'X-User-ID': userId,
          // Không set Content-Type
        },
      });
      return response.data;
    } catch (error) {
      const message = error.response?.data?.message || error.message || 'Đã xảy ra lỗi khi cập nhật sản phẩm';
      throw new Error(message);
    }
  },

  // Xóa nhiều sản phẩm
  deleteProducts: async (productIds) => {
    try {
      const userId = getUserId();
      const response = await api.delete('/product-manager/products', {
        data: { productIds },
        headers: {
          'Content-Type': 'application/json',
          'X-User-ID': userId,
        },
      });
      return response.data;
    } catch (error) {
      const message = error.response?.data?.message || error.message || 'Đã xảy ra lỗi khi xóa sản phẩm';
      throw new Error(message);
    }
  },
};