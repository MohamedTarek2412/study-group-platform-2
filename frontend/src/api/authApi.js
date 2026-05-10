import axios from 'axios';
import axiosInstance from '../utils/axiosInstance';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';
const publicAuthClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
});

const authApi = {
  login: async (email, password) => {
    return await publicAuthClient.post('/api/auth/login', { email, password });
  },

  register: async (email, password, role) => {
    return await publicAuthClient.post('/api/auth/register', { email, password, role });
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
    return await publicAuthClient.post('/api/auth/refresh', {}, {
      headers: {
        Authorization: `Bearer ${refreshToken}`
      }
    });
  }
};

export default authApi;
