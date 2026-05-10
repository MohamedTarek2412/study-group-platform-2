import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import userApi from '../api/userApi';
import groupApi from '../api/groupApi';
import { Users, BookOpen, CheckCircle, XCircle, Clock } from 'lucide-react';

const AdminPage = () => {
  const { user } = useAuth();
  const [pendingCreators, setPendingCreators] = useState([]);
  const [pendingGroups, setPendingGroups] = useState([]);
  const [activeTab, setActiveTab] = useState('creators');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user?.role === 'ADMIN') {
      fetchPendingCreators();
      fetchPendingGroups();
    }
  }, [user]);

  const fetchPendingCreators = async () => {
    try {
      const response = await userApi.getAllUsers();
      const creators = response.data.filter(u => u.creatorStatus === 'PENDING');
      setPendingCreators(creators);
    } catch (error) {
      console.error('Failed to fetch pending creators:', error);
    }
  };

  const fetchPendingGroups = async () => {
    try {
      const response = await groupApi.getAllGroups(0, 50, true);
      const groups = response.data.content?.filter(g => g.status === 'PENDING') || [];
      setPendingGroups(groups);
    } catch (error) {
      console.error('Failed to fetch pending groups:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleApproveCreator = async (userId) => {
    try {
      await userApi.approveCreator(userId);
      setPendingCreators(pendingCreators.filter(c => c.userId !== userId));
    } catch (error) {
      alert('Failed to approve creator');
    }
  };

  const handleRejectCreator = async (userId) => {
    try {
      await userApi.rejectCreator(userId);
      setPendingCreators(pendingCreators.filter(c => c.userId !== userId));
    } catch (error) {
      alert('Failed to reject creator');
    }
  };

  const handleApproveGroup = async (groupId) => {
    try {
      await groupApi.approveGroup(groupId);
      setPendingGroups(pendingGroups.filter(g => g.id !== groupId));
    } catch (error) {
      alert('Failed to approve group');
    }
  };

  const handleRejectGroup = async (groupId) => {
    try {
      await groupApi.rejectGroup(groupId);
      setPendingGroups(pendingGroups.filter(g => g.id !== groupId));
    } catch (error) {
      alert('Failed to reject group');
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
        <p className="text-gray-600">Manage users and study groups</p>
      </div>

      {/* Tabs */}
      <div className="bg-white rounded-lg shadow-md">
        <div className="border-b border-gray-200">
          <nav className="flex space-x-8 px-6">
            <button
              onClick={() => setActiveTab('creators')}
              className={`py-4 px-1 border-b-2 font-medium text-sm ${
                activeTab === 'creators'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              <div className="flex items-center space-x-2">
                <Users className="h-4 w-4" />
                <span>Pending Creators ({pendingCreators.length})</span>
              </div>
            </button>
            
            <button
              onClick={() => setActiveTab('groups')}
              className={`py-4 px-1 border-b-2 font-medium text-sm ${
                activeTab === 'groups'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              <div className="flex items-center space-x-2">
                <BookOpen className="h-4 w-4" />
                <span>Pending Groups ({pendingGroups.length})</span>
              </div>
            </button>
          </nav>
        </div>

        <div className="p-6">
          {activeTab === 'creators' && (
            <div className="space-y-4">
              <h3 className="text-lg font-semibold">Pending Creator Approvals</h3>
              {pendingCreators.length === 0 ? (
                <p className="text-gray-500">No pending creator requests.</p>
              ) : (
                <div className="space-y-4">
                  {pendingCreators.map((creator) => (
                    <div key={creator.id} className="border border-gray-200 rounded-lg p-4">
                      <div className="flex justify-between items-start">
                        <div>
                          <h4 className="font-medium text-gray-900">
                            {creator.fullName || `User ${creator.userId}`}
                          </h4>
                          <p className="text-sm text-gray-600">User ID: {creator.userId}</p>
                          <p className="text-sm text-gray-600">Email: user@example.com</p>
                          <p className="text-sm text-gray-500">
                            Applied: {new Date(creator.updatedAt).toLocaleDateString()}
                          </p>
                        </div>
                        <div className="flex space-x-2">
                          <button
                            onClick={() => handleApproveCreator(creator.userId)}
                            className="flex items-center space-x-1 bg-green-500 text-white px-3 py-2 rounded hover:bg-green-600"
                          >
                            <CheckCircle className="h-4 w-4" />
                            <span>Approve</span>
                          </button>
                          <button
                            onClick={() => handleRejectCreator(creator.userId)}
                            className="flex items-center space-x-1 bg-red-500 text-white px-3 py-2 rounded hover:bg-red-600"
                          >
                            <XCircle className="h-4 w-4" />
                            <span>Reject</span>
                          </button>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}

          {activeTab === 'groups' && (
            <div className="space-y-4">
              <h3 className="text-lg font-semibold">Pending Group Approvals</h3>
              {pendingGroups.length === 0 ? (
                <p className="text-gray-500">No pending group requests.</p>
              ) : (
                <div className="space-y-4">
                  {pendingGroups.map((group) => (
                    <div key={group.id} className="border border-gray-200 rounded-lg p-4">
                      <div className="flex justify-between items-start">
                        <div className="flex-1">
                          <h4 className="font-medium text-gray-900">{group.name}</h4>
                          <p className="text-sm text-gray-600 mt-1">{group.description}</p>
                          <div className="mt-2 space-y-1">
                            <p className="text-sm text-gray-600">
                              <strong>Subject:</strong> {group.subject}
                            </p>
                            <p className="text-sm text-gray-600">
                              <strong>Creator:</strong> User {group.creatorId}
                            </p>
                            <p className="text-sm text-gray-600">
                              <strong>Meeting Type:</strong> {group.meetingType}
                            </p>
                            {group.location && (
                              <p className="text-sm text-gray-600">
                                <strong>Location:</strong> {group.location}
                              </p>
                            )}
                            <p className="text-sm text-gray-600">
                              <strong>Max Members:</strong> {group.maxMembers}
                            </p>
                            <p className="text-sm text-gray-500">
                              <strong>Created:</strong> {new Date(group.createdAt).toLocaleDateString()}
                            </p>
                          </div>
                        </div>
                        <div className="flex space-x-2 ml-4">
                          <button
                            onClick={() => handleApproveGroup(group.id)}
                            className="flex items-center space-x-1 bg-green-500 text-white px-3 py-2 rounded hover:bg-green-600"
                          >
                            <CheckCircle className="h-4 w-4" />
                            <span>Approve</span>
                          </button>
                          <button
                            onClick={() => handleRejectGroup(group.id)}
                            className="flex items-center space-x-1 bg-red-500 text-white px-3 py-2 rounded hover:bg-red-600"
                          >
                            <XCircle className="h-4 w-4" />
                            <span>Reject</span>
                          </button>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default AdminPage;
