import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import groupApi from '../api/groupApi';
import discussionApi from '../api/discussionApi';
import JoinRequestButton from '../components/groups/JoinRequestButton';
import LoadingSpinner from '../components/common/LoadingSpinner';
import { MapPin, Calendar, Users, BookOpen, MessageSquare, FileText } from 'lucide-react';

const GroupDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [group, setGroup] = useState(null);
  const [posts, setPosts] = useState([]);
  const [materials, setMaterials] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isMember, setIsMember] = useState(false);
  const [activeTab, setActiveTab] = useState('posts');
  const [message, setMessage] = useState('');
  const [postContent, setPostContent] = useState('');
  const [replyContent, setReplyContent] = useState({});
  const [materialData, setMaterialData] = useState({ title: '', fileUrl: '', fileType: '' });
  const [editing, setEditing] = useState(false);
  const [groupForm, setGroupForm] = useState(null);

  useEffect(() => {
    const loadGroupDetails = async () => {
      setLoading(true);
      const requests = [fetchGroupDetails()];
      if (user) {
        requests.push(fetchPosts(), fetchMaterials());
      } else {
        setPosts([]);
        setMaterials([]);
        setMessage('Log in and join this group to view discussions and materials.');
      }
      await Promise.all(requests);
      setLoading(false);
    };

    loadGroupDetails();
  }, [id, user]);

  const fetchGroupDetails = async () => {
    try {
      const response = await groupApi.getGroup(id);
      setGroup(response.data);
      setGroupForm({
        name: response.data.name || '',
        description: response.data.description || '',
        subject: response.data.subject || '',
        location: response.data.location || '',
        meetingType: response.data.meetingType || 'ONLINE',
        meetingSchedule: response.data.meetingSchedule || '',
        maxMembers: response.data.maxMembers || 10
      });
    } catch (error) {
      console.error('Failed to fetch group details:', error);
    }
  };

  const fetchPosts = async () => {
    try {
      const response = await discussionApi.getPosts(id);
      setPosts(response.data.content || response.data);
    } catch (error) {
      setMessage(error.response?.status === 403 ? 'Join this group to view and participate in discussions.' : 'Failed to fetch posts.');
    }
  };

  const fetchMaterials = async () => {
    try {
      const response = await discussionApi.getMaterials(id);
      setMaterials(response.data.content || response.data);
    } catch (error) {
      setMessage(error.response?.status === 403 ? 'Join this group to view and share materials.' : 'Failed to fetch materials.');
    }
  };

  const isCreator = user?.id && group?.creatorId === user.id;

  const handleUpdateGroup = async (event) => {
    event.preventDefault();
    try {
      const response = await groupApi.updateGroup(id, groupForm);
      setGroup(response.data);
      setEditing(false);
      setMessage('Group updated successfully.');
    } catch (error) {
      setMessage(error.response?.data?.message || 'Failed to update group.');
    }
  };

  const handleDeleteGroup = async () => {
    if (!window.confirm('Delete this group?')) return;
    try {
      await groupApi.deleteGroup(id);
      navigate('/groups');
    } catch (error) {
      setMessage(error.response?.data?.message || 'Failed to delete group.');
    }
  };

  const handleCreatePost = async (event) => {
    event.preventDefault();
    try {
      await discussionApi.createPost(id, { content: postContent });
      setPostContent('');
      await fetchPosts();
    } catch (error) {
      setMessage(error.response?.data?.message || 'Only group members can post.');
    }
  };

  const handleAddReply = async (postId) => {
    try {
      await discussionApi.addReply(id, postId, replyContent[postId] || '');
      setReplyContent({ ...replyContent, [postId]: '' });
      await fetchPosts();
    } catch (error) {
      setMessage(error.response?.data?.message || 'Only group members can comment.');
    }
  };

  const handleUploadMaterial = async (event) => {
    event.preventDefault();
    try {
      await discussionApi.uploadMaterial(id, materialData);
      setMaterialData({ title: '', fileUrl: '', fileType: '' });
      await fetchMaterials();
    } catch (error) {
      setMessage(error.response?.data?.message || 'Only group members can share materials.');
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

        {isCreator && (
          <div className="flex gap-2 mb-4">
            <button onClick={() => setEditing(!editing)} className="bg-gray-800 text-white px-3 py-2 rounded-md text-sm">
              {editing ? 'Cancel Edit' : 'Edit Group'}
            </button>
            <button onClick={handleDeleteGroup} className="bg-red-500 text-white px-3 py-2 rounded-md text-sm">
              Delete Group
            </button>
          </div>
        )}

        {message && <div className="bg-blue-50 text-blue-800 px-3 py-2 rounded mb-4">{message}</div>}

        {editing && groupForm && (
          <form onSubmit={handleUpdateGroup} className="space-y-3 mb-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              <input className="px-3 py-2 border rounded-md" value={groupForm.name} onChange={(e) => setGroupForm({ ...groupForm, name: e.target.value })} />
              <input className="px-3 py-2 border rounded-md" value={groupForm.subject} onChange={(e) => setGroupForm({ ...groupForm, subject: e.target.value })} />
              <select className="px-3 py-2 border rounded-md" value={groupForm.meetingType} onChange={(e) => setGroupForm({ ...groupForm, meetingType: e.target.value })}>
                <option value="ONLINE">Online</option>
                <option value="OFFLINE">Offline</option>
              </select>
              <input className="px-3 py-2 border rounded-md" value={groupForm.meetingSchedule} onChange={(e) => setGroupForm({ ...groupForm, meetingSchedule: e.target.value })} />
              <input className="px-3 py-2 border rounded-md" value={groupForm.location} onChange={(e) => setGroupForm({ ...groupForm, location: e.target.value })} />
              <input className="px-3 py-2 border rounded-md" type="number" value={groupForm.maxMembers} onChange={(e) => setGroupForm({ ...groupForm, maxMembers: Number(e.target.value) })} />
            </div>
            <textarea className="w-full px-3 py-2 border rounded-md" value={groupForm.description} onChange={(e) => setGroupForm({ ...groupForm, description: e.target.value })} />
            <button className="bg-green-500 text-white px-4 py-2 rounded-md">Save Changes</button>
          </form>
        )}

        <p className="text-gray-600 mb-6">{group.description}</p>
        <p className="text-sm text-gray-500 mb-4">
          Created by {group.creatorName || `Creator ${group.creatorId?.slice(0, 8)}`}
        </p>

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
              {user && (
                <form onSubmit={handleCreatePost} className="flex gap-2">
                  <input
                    value={postContent}
                    onChange={(e) => setPostContent(e.target.value)}
                    className="flex-1 px-3 py-2 border rounded-md"
                    placeholder="Start a discussion..."
                    required
                  />
                  <button className="bg-blue-500 text-white px-4 py-2 rounded-md">Post</button>
                </form>
              )}
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
                    {user && (
                      <div className="mt-3 flex gap-2">
                        <input
                          value={replyContent[post.id] || ''}
                          onChange={(e) => setReplyContent({ ...replyContent, [post.id]: e.target.value })}
                          className="flex-1 px-3 py-2 border rounded-md text-sm"
                          placeholder="Add a comment..."
                        />
                        <button onClick={() => handleAddReply(post.id)} className="bg-gray-800 text-white px-3 py-2 rounded-md text-sm">
                          Reply
                        </button>
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
              {user && (
                <form onSubmit={handleUploadMaterial} className="grid grid-cols-1 md:grid-cols-4 gap-2">
                  <input className="px-3 py-2 border rounded-md" placeholder="Title" value={materialData.title} onChange={(e) => setMaterialData({ ...materialData, title: e.target.value })} required />
                  <input className="px-3 py-2 border rounded-md md:col-span-2" placeholder="File URL" value={materialData.fileUrl} onChange={(e) => setMaterialData({ ...materialData, fileUrl: e.target.value })} required />
                  <input className="px-3 py-2 border rounded-md" placeholder="Type" value={materialData.fileType} onChange={(e) => setMaterialData({ ...materialData, fileType: e.target.value })} />
                  <button className="bg-blue-500 text-white px-4 py-2 rounded-md md:col-span-4">Share Material</button>
                </form>
              )}
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
