import React from 'react';
import { useAuth } from './AuthContext';
import {Link} from 'react-router-dom';

const SemiPrivateRoute = ({ children, profileOwner }) => {
  const { user } = useAuth();

  if (!user) {
    return <div className="alert">Please <Link to="/login">log in</Link> to see this page.</div>;
  }

  const hasAccess = (user.username === profileOwner) ||  user.roles.includes('ADMIN');

  if (!hasAccess) {
    return <div className="alert">You don't have permission to see this page.</div>;
  }

  return children;
};

export default SemiPrivateRoute;
