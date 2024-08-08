import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUserAlt } from '@fortawesome/free-solid-svg-icons';
import { useAuth } from '../components/AuthContext'
import './Layout.css';

const Layout = ({ children, username }) => {
  const { user, logout } = useAuth();
  const [searchTerm, setSearchTerm] = useState('');
  const navigate = useNavigate();

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchTerm.trim() !== '') {
      navigate(`/search?query=${encodeURIComponent(searchTerm)}`);
    }
  };

  const userHasRole = (role) => {
    return user && user.roles && user.roles.includes(role);
  };

  return (
    <div className="layout">
      <header className="header">
        <h3 style={{fontSize: "25px"}}><Link to="/">Skyrim Encyclopedia</Link></h3>
        <form onSubmit={handleSearch} className="search-form">
            <input
              type="text"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              placeholder="Search articles..."
              className="search-input"
            />
            <button type="submit" className="header-button">Search</button>
          </form>
        <div className="header-right">
            {user ? (
            <>
              <span className="username"><FontAwesomeIcon icon={faUserAlt} /> <a href={`/profile/${user.username}`} style={{color: "white"}}>{user.username}</a></span>
              <button onClick={logout} className="header-button">Logout</button>
            </>
          ) : (
            <>
              <span className="not-logged-in"><FontAwesomeIcon icon={faUserAlt} /> Not logged in</span>
              <Link to="/signup" className="header-button">Sign up</Link>
              <Link to="/login" className="header-button">Log in</Link>
            </>
          )}
        </div>
      </header>
      <div className="main-container">
        <nav className="sidebar">
            <ul>
              <li><Link to="/">Home</Link></li>
              <li><Link to="/create-article">Create Article</Link></li>
              <li><Link to="/recent-changes">Recent Changes</Link></li>
            </ul>
              {(userHasRole('MODERATOR') || userHasRole('ADMIN')) && (
              <><h3>Moderator</h3><ul>
                <li><Link to="/moderator-dashboard">Moderator Dashboard</Link></li>

              </ul></>
              )}
              {userHasRole('ADMIN') && (
              <><h3>Admin</h3><ul>
                <li><Link to="/admin-dashboard">Admin Dashboard</Link></li>
                
              </ul></>
              )}
        </nav>
        <main className="content">
          {children}
        </main>
      </div>
      <footer className="footer">
        <p>© 2024 Skyrim Encyclopedia. All rights reserved.</p>
      </footer>
    </div>
  );
};

export default Layout;