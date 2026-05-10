import React from 'react';
import { Link } from 'react-router-dom';
import { Users, MapPin, Calendar, BookOpen } from 'lucide-react';

const GroupCard = ({ group }) => {
  return (
    <div className="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow p-6">
      <div className="flex justify-between items-start mb-4">
        <h3 className="text-xl font-semibold text-gray-800">{group.name}</h3>
        <span className={`px-2 py-1 rounded-full text-xs font-medium ${
          group.status === 'APPROVED' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'
        }`}>
          {group.status}
        </span>
      </div>
      
      <p className="text-gray-600 mb-4 line-clamp-3">{group.description}</p>
      
      <div className="space-y-2 mb-4">
        <div className="flex items-center text-sm text-gray-500">
          <BookOpen className="h-4 w-4 mr-2" />
          <span>{group.subject}</span>
        </div>
        
        {group.location && (
          <div className="flex items-center text-sm text-gray-500">
            <MapPin className="h-4 w-4 mr-2" />
            <span>{group.location}</span>
          </div>
        )}
        
        <div className="flex items-center text-sm text-gray-500">
          <Calendar className="h-4 w-4 mr-2" />
          <span>{group.meetingSchedule || 'Schedule TBD'}</span>
        </div>
        
        <div className="flex items-center text-sm text-gray-500">
          <Users className="h-4 w-4 mr-2" />
          <span>Max {group.maxMembers} members</span>
        </div>

        <div className="flex items-center text-sm text-gray-500">
          <Users className="h-4 w-4 mr-2" />
          <span>{group.creatorName || `Creator ${group.creatorId?.slice(0, 8)}`}</span>
        </div>
      </div>
      
      <div className="flex justify-between items-center">
        <span className="text-xs text-gray-400">
          Created {new Date(group.createdAt).toLocaleDateString()}
        </span>
        <Link
          to={`/groups/${group.id}`}
          className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition text-sm"
        >
          View Details
        </Link>
      </div>
    </div>
  );
};

export default GroupCard;
