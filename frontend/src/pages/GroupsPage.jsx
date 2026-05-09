import React, { useState } from 'react';
import GroupList from '../components/groups/GroupList';
import { Search, Filter, Plus } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

const GroupsPage = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [subjectFilter, setSubjectFilter] = useState('');
  const [locationFilter, setLocationFilter] = useState('');
  const { user } = useAuth();

  const subjects = [
    'Mathematics', 'Physics', 'Chemistry', 'Biology', 
    'Computer Science', 'Engineering', 'Literature', 'History',
    'Economics', 'Psychology', 'Other'
  ];

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-900">Study Groups</h1>
        {user?.role === 'CREATOR' && (
          <button className="flex items-center space-x-2 bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition">
            <Plus className="h-4 w-4" />
            <span>Create Group</span>
          </button>
        )}
      </div>

      {/* Search and Filters */}
      <div className="bg-white p-6 rounded-lg shadow-md">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div className="md:col-span-2">
            <div className="relative">
              <Search className="absolute left-3 top-3 h-4 w-4 text-gray-400" />
              <input
                type="text"
                placeholder="Search groups by name or description..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>

          <div>
            <select
              value={subjectFilter}
              onChange={(e) => setSubjectFilter(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Subjects</option>
              {subjects.map((subject) => (
                <option key={subject} value={subject}>
                  {subject}
                </option>
              ))}
            </select>
          </div>

          <div>
            <input
              type="text"
              placeholder="Location..."
              value={locationFilter}
              onChange={(e) => setLocationFilter(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        </div>

        {(searchQuery || subjectFilter || locationFilter) && (
          <div className="mt-4 flex items-center space-x-2">
            <Filter className="h-4 w-4 text-gray-500" />
            <span className="text-sm text-gray-600">Active filters:</span>
            {searchQuery && (
              <span className="bg-blue-100 text-blue-800 px-2 py-1 rounded-full text-xs">
                Search: {searchQuery}
              </span>
            )}
            {subjectFilter && (
              <span className="bg-green-100 text-green-800 px-2 py-1 rounded-full text-xs">
                Subject: {subjectFilter}
              </span>
            )}
            {locationFilter && (
              <span className="bg-purple-100 text-purple-800 px-2 py-1 rounded-full text-xs">
                Location: {locationFilter}
              </span>
            )}
            <button
              onClick={() => {
                setSearchQuery('');
                setSubjectFilter('');
                setLocationFilter('');
              }}
              className="text-red-500 hover:text-red-700 text-sm"
            >
              Clear all
            </button>
          </div>
        )}
      </div>

      {/* Groups List */}
      <GroupList 
        searchQuery={searchQuery}
        subjectFilter={subjectFilter}
        locationFilter={locationFilter}
      />
    </div>
  );
};

export default GroupsPage;
