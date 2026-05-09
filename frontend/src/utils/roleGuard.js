export const hasRole = (userRole, requiredRole) => {
  if (!requiredRole) return true;
  if (!userRole) return false;
  
  const roleHierarchy = {
    'ADMIN': 3,
    'CREATOR': 2,
    'STUDENT': 1
  };
  
  return roleHierarchy[userRole] >= roleHierarchy[requiredRole];
};

export const canAccessResource = (user, resourceOwnerId, requiredRole = null) => {
  if (!user) return false;
  
  // Admin can access everything
  if (user.role === 'ADMIN') return true;
  
  // Check role-based access
  if (requiredRole && !hasRole(user.role, requiredRole)) {
    return false;
  }
  
  // Check ownership
  if (resourceOwnerId && user.id !== resourceOwnerId) {
    return false;
  }
  
  return true;
};
