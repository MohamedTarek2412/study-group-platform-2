import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import groupApi from '../api/groupApi';
import userApi from '../api/userApi';
import { BookOpen, Users, Calendar, TrendingUp } from 'lucide-react';

const DashboardPage = () => {
  const { user } = useAuth();
  const [userProfile, setUserProfile] = useState(null);
  const [myGroups, setMyGroups] = useState([]);
  const [pendingRequests, setPendingRequests] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user) {
      fetchUserData();
      fetchMyGroups();
      if (user.role === 'CREATOR') {
        fetchPendingRequests();
      }
    }
  }, [user]);

  const fetchUserData = async () => {
    try {
      const response = await userApi.getUserProfile(user.id);
      setUserProfile(response.data);
    } catch (error) {
      console.error('Failed to fetch user profile:', error);
    }
  };

  const fetchMyGroups = async () => {
    try {
      // This would typically be a specific endpoint for user's groups
      const response = await groupApi.getAllGroups();
      setMyGroups(response.data.content?.slice(0, 3) || []);
    } catch (error) {
      console.error('Failed to fetch groups:', error);
    }
  };

  const fetchPendingRequests = async () => {
    try {
      // This would fetch pending join requests for creator's groups
      // For now, we'll use mock data
      setPendingRequests([]);
    } catch (error) {
      console.error('Failed to fetch pending requests:', error);
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
      {/* Welcome Section */}
      <div className="bg-gradient-to-r from-blue-600 to-purple-600 text-white p-8 rounded-lg">
        <h1 className="text-3xl font-bold mb-2">
          Welcome back, {userProfile?.fullName || 'Student'}!
        </h1>
        <p className="text-blue-100">
          {user?.role === 'CREATOR' ? 'Manage your groups and review join requests' : 'Track your learning progress and group activities'}
        </p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-white p-6 rounded-lg shadow-md">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">My Groups</p>
              <p className="text-2xl font-bold text-gray-900">{myGroups.length}</p>
            </div>
            <BookOpen className="h-8 w-8 text-blue-500" />
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">Total Members</p>
              <p className="text-2xl font-bold text-gray-900">
                {myGroups.reduce((sum, group) => sum + (group.currentMembers || 0), 0)}
              </p>
            </div>
            <Users className="h-8 w-8 text-green-500" />
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">Pending Requests</p>
              <p className="text-2xl font-bold text-gray-900">{pendingRequests.length}</p>
            </div>
            <Calendar className="h-8 w-8 text-yellow-500" />
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600">Activity Score</p>
              <p className="text-2xl font-bold text-gray-900">85</p>
            </div>
            <TrendingUp className="h-8 w-8 text-purple-500" />
          </div>
        </div>
      </div>

      {/* My Groups */}
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-xl font-bold text-gray-900 mb-4">My Groups</h2>
        {myGroups.length === 0 ? (
          <p className="text-gray-500">You haven't joined any groups yet.</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {myGroups.map((group) => (
              <div key={group.id} className="border border-gray-200 rounded-lg p-4">
                <h3 className="font-semibold text-gray-900 mb-2">{group.name}</h3>
                <p className="text-sm text-gray-600 mb-2">{group.subject}</p>
                <div className="flex justify-between items-center">
                  <span className="text-xs text-gray-500">
                    {group.currentMembers || 0}/{group.maxMembers} members
                  </span>
                  <span className="text-xs bg-green-100 text-green-800 px-2 py-1 rounded">
                    {group.status}
                  </span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Pending Join Requests (for creators) */}
      {user?.role === 'CREATOR' && pendingRequests.length > 0 && (
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h2 className="text-xl font-bold text-gray-900 mb-4">Pending Join Requests</h2>
          <div className="space-y-3">
            {pendingRequests.map((request) => (
              <div key={request.id} className="border border-gray-200 rounded-lg p-4">
                <div className="flex justify-between items-center">
                  <div>
                    <p className="font-medium text-gray-900">Student {request.studentId}</p>
                    <p className="text-sm text-gray-600">Wants to join: {request.groupName}</p>
                  </div>
                  <div className="space-x-2">
                    <button className="bg-green-500 text-white px-3 py-1 rounded text-sm hover:bg-green-600">
                      Accept
                    </button>
                    <button className="bg-red-500 text-white px-3 py-1 rounded text-sm hover:bg-red-600">
                      Reject
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default DashboardPage;
