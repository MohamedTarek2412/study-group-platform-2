import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import groupApi from '../api/groupApi';
import discussionApi from '../api/discussionApi';
import JoinRequestButton from '../components/groups/JoinRequestButton';
import LoadingSpinner from '../components/common/LoadingSpinner';
import { MapPin, Calendar, Users, BookOpen, MessageSquare, FileText } from 'lucide-react';

const GroupDetailPage = () => {
  const { id } = useParams();
  const { user } = useAuth();
  const [group, setGroup] = useState(null);
  const [posts, setPosts] = useState([]);
  const [materials, setMaterials] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isMember, setIsMember] = useState(false);
  const [activeTab, setActiveTab] = useState('posts');

  useEffect(() => {
    fetchGroupDetails();
    fetchPosts();
    fetchMaterials();
  }, [id]);

  const fetchGroupDetails = async () => {
    try {
      const response = await groupApi.getGroup(id);
      setGroup(response.data);
    } catch (error) {
      console.error('Failed to fetch group details:', error);
    }
  };

  const fetchPosts = async () => {
    try {
      const response = await discussionApi.getPosts(id);
      setPosts(response.data.content || response.data);
    } catch (error) {
      console.error('Failed to fetch posts:', error);
    }
  };

  const fetchMaterials = async () => {
    try {
      const response = await discussionApi.getMaterials(id);
      setMaterials(response.data.content || response.data);
    } catch (error) {
      console.error('Failed to fetch materials:', error);
    }
  };

  if (loading) return <LoadingSpinner />;

  if (!group) {
    return (
      <div className="text-center py-8">
        <p className="text-gray-500">Group not found.</p>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Group Header */}
      <div className="bg-white rounded-lg shadow-md p-6">
        <div className="flex justify-between items-start mb-4">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 mb-2">{group.name}</h1>
            <span className={`px-3 py-1 rounded-full text-sm font-medium ${
              group.status === 'APPROVED' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'
            }`}>
              {group.status}
            </span>
          </div>
          <JoinRequestButton 
            groupId={group.id} 
            isMember={isMember}
            onJoinRequestSent={() => setIsMember(false)}
          />
        </div>

        <p className="text-gray-600 mb-6">{group.description}</p>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <div className="flex items-center space-x-2 text-gray-600">
            <BookOpen className="h-5 w-5" />
            <span>{group.subject}</span>
          </div>
          
          {group.location && (
            <div className="flex items-center space-x-2 text-gray-600">
              <MapPin className="h-5 w-5" />
              <span>{group.location}</span>
            </div>
          )}
          
          <div className="flex items-center space-x-2 text-gray-600">
            <Calendar className="h-5 w-5" />
            <span>{group.meetingSchedule || 'Schedule TBD'}</span>
          </div>
          
          <div className="flex items-center space-x-2 text-gray-600">
            <Users className="h-5 w-5" />
            <span>Max {group.maxMembers} members</span>
          </div>
        </div>
      </div>

      {/* Tabs */}
      <div className="bg-white rounded-lg shadow-md">
        <div className="border-b border-gray-200">
          <nav className="flex space-x-8 px-6">
            <button
              onClick={() => setActiveTab('posts')}
              className={`py-4 px-1 border-b-2 font-medium text-sm ${
                activeTab === 'posts'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              <div className="flex items-center space-x-2">
                <MessageSquare className="h-4 w-4" />
                <span>Discussions</span>
              </div>
            </button>
            
            <button
              onClick={() => setActiveTab('materials')}
              className={`py-4 px-1 border-b-2 font-medium text-sm ${
                activeTab === 'materials'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              <div className="flex items-center space-x-2">
                <FileText className="h-4 w-4" />
                <span>Materials</span>
              </div>
            </button>
          </nav>
        </div>

        <div className="p-6">
          {activeTab === 'posts' && (
            <div className="space-y-4">
              <h3 className="text-lg font-semibold mb-4">Group Discussions</h3>
              {posts.length === 0 ? (
                <p className="text-gray-500">No discussions yet. Be the first to start one!</p>
              ) : (
                posts.map((post) => (
                  <div key={post.id} className="border-b border-gray-200 pb-4 mb-4">
                    <div className="flex justify-between items-start mb-2">
                      <h4 className="font-medium text-gray-900">Post by User {post.authorId}</h4>
                      <span className="text-sm text-gray-500">
                        {new Date(post.createdAt).toLocaleDateString()}
                      </span>
                    </div>
                    <p className="text-gray-700">{post.content}</p>
                    {post.replies && post.replies.length > 0 && (
                      <div className="mt-3 pl-4 border-l-2 border-gray-200">
                        {post.replies.map((reply) => (
                          <div key={reply.id} className="mb-2">
                            <p className="text-sm text-gray-600">
                              <strong>User {reply.authorId}:</strong> {reply.content}
                            </p>
                          </div>
                        ))}
                      </div>
                    )}
                  </div>
                ))
              )}
            </div>
          )}

          {activeTab === 'materials' && (
            <div className="space-y-4">
              <h3 className="text-lg font-semibold mb-4">Study Materials</h3>
              {materials.length === 0 ? (
                <p className="text-gray-500">No materials shared yet.</p>
              ) : (
                materials.map((material) => (
                  <div key={material.id} className="border-b border-gray-200 pb-4 mb-4">
                    <div className="flex justify-between items-start">
                      <div>
                        <h4 className="font-medium text-gray-900">{material.title}</h4>
                        <p className="text-sm text-gray-600">
                          Uploaded by User {material.uploaderId}
                        </p>
                        <p className="text-xs text-gray-500">
                          {new Date(material.createdAt).toLocaleDateString()}
                        </p>
                      </div>
                      <a
                        href={material.fileUrl}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="bg-blue-500 text-white px-3 py-1 rounded text-sm hover:bg-blue-600"
                      >
                        Download
                      </a>
                    </div>
                  </div>
                ))
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default GroupDetailPage;
