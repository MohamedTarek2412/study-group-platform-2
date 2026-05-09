import axiosInstance from '../utils/axiosInstance';

const discussionApi = {
  // Posts
  getPosts: async (groupId, page = 0, size = 10) => {
    return await axiosInstance.get(`/api/groups/${groupId}/posts?page=${page}&size=${size}`);
  },

  createPost: async (groupId, postData) => {
    return await axiosInstance.post(`/api/groups/${groupId}/posts`, postData);
  },

  updatePost: async (groupId, postId, postData) => {
    return await axiosInstance.put(`/api/groups/${groupId}/posts/${postId}`, postData);
  },

  deletePost: async (groupId, postId) => {
    return await axiosInstance.delete(`/api/groups/${groupId}/posts/${postId}`);
  },

  addReply: async (groupId, postId, content) => {
    return await axiosInstance.post(`/api/groups/${groupId}/posts/${postId}/replies`, content, {
      headers: {
        'Content-Type': 'text/plain'
      }
    });
  },

  // Materials
  getMaterials: async (groupId, page = 0, size = 10) => {
    return await axiosInstance.get(`/api/groups/${groupId}/materials?page=${page}&size=${size}`);
  },

  uploadMaterial: async (groupId, materialData) => {
    return await axiosInstance.post(`/api/groups/${groupId}/materials`, materialData);
  },

  deleteMaterial: async (groupId, materialId) => {
    return await axiosInstance.delete(`/api/groups/${groupId}/materials/${materialId}`);
  }
};

export default discussionApi;
