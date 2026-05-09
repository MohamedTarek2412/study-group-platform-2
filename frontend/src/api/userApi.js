import axiosInstance from '../utils/axiosInstance';

const userApi = {
  getUserProfile: async (id) => {
    return await axiosInstance.get(`/api/users/${id}`);
  },

  updateUserProfile: async (id, profileData) => {
    return await axiosInstance.put(`/api/users/${id}`, profileData);
  },

  getAllUsers: async () => {
    return await axiosInstance.get('/api/users');
  },

  approveCreator: async (id) => {
    return await axiosInstance.put(`/api/users/${id}/approve-creator`);
  },

  rejectCreator: async (id) => {
    return await axiosInstance.put(`/api/users/${id}/reject-creator`);
  }
};

export default userApi;
