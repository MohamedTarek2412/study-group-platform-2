import React, { useState, useEffect } from 'react';
import GroupCard from './GroupCard';
import LoadingSpinner from '../common/LoadingSpinner';
import groupApi from '../../api/groupApi';

const GroupList = ({ searchQuery, subjectFilter, locationFilter, scheduleFilter, refreshKey = 0 }) => {
  const [groups, setGroups] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchGroups();
  }, [searchQuery, subjectFilter, locationFilter, scheduleFilter, refreshKey]);

  const fetchGroups = async () => {
    try {
      setLoading(true);
      let response;
      
      if (searchQuery || subjectFilter || locationFilter) {
        response = await groupApi.searchGroups(searchQuery, subjectFilter, locationFilter, scheduleFilter);
      } else {
        response = await groupApi.getAllGroups();
      }
      
      setGroups(response.data.content || response.data);
    } catch (err) {
      setError('Failed to fetch groups');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <LoadingSpinner />;
  
  if (error) {
    return (
      <div className="text-center py-8">
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded inline-block">
          {error}
        </div>
      </div>
    );
  }

  if (groups.length === 0) {
    return (
      <div className="text-center py-8">
        <p className="text-gray-500">No groups found matching your criteria.</p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {groups.map((group) => (
        <GroupCard key={group.id} group={group} />
      ))}
    </div>
  );
};

export default GroupList;
