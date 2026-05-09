import axiosInstance from '../utils/axiosInstance';

const groupApi = {
  getAllGroups: async (page = 0, size = 10) => {
    return await axiosInstance.get(`/api/groups?page=${page}&size=${size}`);
  },

  getGroup: async (id) => {
    return await axiosInstance.get(`/api/groups/${id}`);
  },

  searchGroups: async (query, subject, location) => {
    const params = new URLSearchParams();
    if (query) params.append('q', query);
    if (subject) params.append('subject', subject);
    if (location) params.append('location', location);
    
    return await axiosInstance.get(`/api/groups/search?${params.toString()}`);
  },

  createGroup: async (groupData) => {
    return await axiosInstance.post('/api/groups', groupData);
  },

  updateGroup: async (id, groupData) => {
    return await axiosInstance.put(`/api/groups/${id}`, groupData);
  },

  deleteGroup: async (id) => {
    return await axiosInstance.delete(`/api/groups/${id}`);
  },

  approveGroup: async (id) => {
    return await axiosInstance.put(`/api/groups/${id}/approve`);
  },

  rejectGroup: async (id) => {
    return await axiosInstance.put(`/api/groups/${id}/reject`);
  },

  getGroupMembers: async (id) => {
    return await axiosInstance.get(`/api/groups/${id}/members`);
  },

  // Join requests
  createJoinRequest: async (groupId) => {
    return await axiosInstance.post(`/api/groups/${groupId}/join-requests`);
  },

  getJoinRequests: async (groupId) => {
    return await axiosInstance.get(`/api/groups/${groupId}/join-requests`);
  },

  acceptJoinRequest: async (groupId, requestId) => {
    return await axiosInstance.put(`/api/groups/${groupId}/join-requests/${requestId}/accept`);
  },

  rejectJoinRequest: async (groupId, requestId) => {
    return await axiosInstance.put(`/api/groups/${groupId}/join-requests/${requestId}/reject`);
  }
};

export default groupApi;
