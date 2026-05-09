import axiosInstance from '../utils/axiosInstance';

const authApi = {
  login: async (email, password) => {
    return await axiosInstance.post('/api/auth/login', { email, password });
  },

  register: async (email, password, role) => {
    return await axiosInstance.post('/api/auth/register', { email, password, role });
  },

  validateToken: async (token) => {
    return await axiosInstance.get('/api/auth/validate', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  },

  refreshToken: async () => {
    const refreshToken = localStorage.getItem('refreshToken');
    return await axiosInstance.post('/api/auth/refresh', {}, {
      headers: {
        Authorization: `Bearer ${refreshToken}`
      }
    });
  }
};

export default authApi;
