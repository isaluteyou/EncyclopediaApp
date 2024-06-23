import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import ArticleList from './components/ArticleList';
import ArticleDetail from './components/ArticleDetail';
import EditArticle from './components/EditArticle';
import ArticleHistory from './components/ArticleHistory';
import CreateArticle from './components/CreateArticle';
import CreateArticleRoot from './components/CreateArticleRoot';
import SearchResults from './components/SearchResults';
import SignUp from './components/SignUp';
import Login from './components/LogIn';
import { AuthProvider, useAuth } from './components/AuthContext';
import Layout from './components/Layout';

const App = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState('');

  useEffect(() => {
    const token = localStorage.getItem('token');
    const user = JSON.parse(localStorage.getItem('user'));
    if (token && user) {
      setUsername(user.username);
      setIsLoggedIn(true);
    }
  }, []);

  const handleLogout = () => {
    setUsername('');
    setIsLoggedIn(false);
  };

  return (
    <AuthProvider>
      <Router>
        <Layout isLoggedIn={isLoggedIn} username={username} onLogout={handleLogout}>
          <Routes>
            <Route path="/" element={<ArticleList />} />
            <Route path="/wiki/:title" element={<ArticleDetail />} />
            <Route path="/wiki/:title/edit" element={<EditArticle />} />
            <Route path="/wiki/:title/history" element={<ArticleHistory />} />
            <Route path="/create-article" element={<CreateArticleRoot />} />
            <Route path="/create-article/:id" element={<CreateArticle />} />
            <Route path="/search" element={<SearchResults />} />
            <Route path="/signup" element={<SignUp />} />
            <Route path="/login" element={<Login onLogin={(username) => { setUsername(username); setIsLoggedIn(true); }} />} />
          </Routes>
        </Layout>
      </Router>
    </AuthProvider>
  );
};

export default App;