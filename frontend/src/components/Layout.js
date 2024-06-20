import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUserAlt } from '@fortawesome/free-solid-svg-icons';
import './Layout.css';

const Layout = ({ children, isLoggedIn, username, onLogout }) => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    onLogout();
    navigate('/');
  };

  return (
    <div className="layout">
      <header className="header">
        <h2><Link to="/">Skyrim Encyclopedia</Link></h2>
        <div className="header-right">
          {isLoggedIn ? (
            <>
              <span className="username"><FontAwesomeIcon icon={faUserAlt} /> {username}</span>
              <button onClick={handleLogout} className="header-button">Logout</button>
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
          </ul>
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