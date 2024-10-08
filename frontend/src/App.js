import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, useParams } from 'react-router-dom';
import ArticleList from './components/ArticleList';
import ArticleDetail from './components/ArticleDetail';
import CategoryDetail from './components/CategoryDetail';
import EditArticle from './components/EditArticle';
import ArticleHistory from './components/ArticleHistory';
import CreateArticle from './components/CreateArticle';
import CreateArticleRoot from './components/CreateArticleRoot';
import SearchResults from './components/SearchResults';
import UserProfile from './components/UserProfile';
import EditUserProfile from './components/EditUserProfile';
import RecentChanges from './components/RecentChanges';
import ModeratorDashboard from './components/ModeratorDashboard';
import AdminDashboard from './components/AdminDashboard';
import SemiPrivateRoute from './components/SemiPrivateRoute';
import PrivateRoute from './components/PrivateRoute';
import PrivateRouteAdmin from './components/PrivateRouteAdmin';
import SignUp from './components/SignUp';
import Login from './components/LogIn';
import { AuthProvider } from './components/AuthContext';
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

  const ProfileEditRoute = () => {
    const { username } = useParams();
    return (
      <SemiPrivateRoute profileOwner={username}>
        <EditUserProfile />
      </SemiPrivateRoute>
    );
  };

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
            <Route path="/category/:categoryName" element={<CategoryDetail />} />
            <Route path="/create-article" element={<CreateArticleRoot />} />
            <Route path="/recent-changes" element={<RecentChanges />} />
            <Route path="/moderator-dashboard" element={<PrivateRoute><ModeratorDashboard /></PrivateRoute>} />
            <Route path="/admin-dashboard" element={<PrivateRouteAdmin><AdminDashboard /></PrivateRouteAdmin>} />
            <Route path="/create-article/:title" element={<CreateArticle />} />
            <Route path="/profile/:username" element={<UserProfile />} />
            <Route path="/profile/:username/edit" element={<ProfileEditRoute />} />
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