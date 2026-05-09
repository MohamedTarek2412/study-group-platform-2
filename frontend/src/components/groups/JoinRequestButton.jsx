import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import groupApi from '../../api/groupApi';
import { UserPlus, Check, X } from 'lucide-react';

const JoinRequestButton = ({ groupId, isMember, onJoinRequestSent }) => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);
  const [requested, setRequested] = useState(false);

  const handleJoinRequest = async () => {
    if (!user) {
      alert('Please login to join a group');
      return;
    }

    setLoading(true);
    try {
      await groupApi.createJoinRequest(groupId);
      setRequested(true);
      onJoinRequestSent?.();
    } catch (error) {
      alert(error.response?.data?.message || 'Failed to send join request');
    } finally {
      setLoading(false);
    }
  };

  if (isMember) {
    return (
      <span className="inline-flex items-center px-4 py-2 bg-green-100 text-green-800 rounded-md">
        <Check className="h-4 w-4 mr-2" />
        Member
      </span>
    );
  }

  if (requested) {
    return (
      <span className="inline-flex items-center px-4 py-2 bg-yellow-100 text-yellow-800 rounded-md">
        <UserPlus className="h-4 w-4 mr-2" />
        Request Sent
      </span>
    );
  }

  return (
    <button
      onClick={handleJoinRequest}
      disabled={loading}
      className="inline-flex items-center px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition disabled:bg-blue-300"
    >
      {loading ? (
        'Sending...'
      ) : (
        <>
          <UserPlus className="h-4 w-4 mr-2" />
          Join Group
        </>
      )}
    </button>
  );
};

export default JoinRequestButton;
