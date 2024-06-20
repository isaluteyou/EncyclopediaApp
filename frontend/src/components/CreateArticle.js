import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axios from './axios';
import { useAuth } from '../components/AuthContext';
import './EditArticle.css';

const CreateArticle = () => {
  const { id } = useParams(); 
  const [title, setTitle] = useState(id || ''); 
  const [content, setContent] = useState('');
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const { user } = useAuth();

  const capitalizeFirstLetter = (string) => {
    return string.charAt(0).toUpperCase() + string.slice(1);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const capitalizedTitle = capitalizeFirstLetter(title);
    if (user) {
      try {
        await axios.post('http://localhost:8000/api/articles', {
          article: { title, content },
          username: user.username
        });
        navigate(`/wiki/${capitalizedTitle}`);
      } catch (error) {
        if (error.response && error.response.data) {
            setError(error.response.data); // display backend error
        } else {
            setError('There was an error creating the article.');
        }
      }
    } else {
      setError('You must be logged in to create an article.');
    }
  };

  return (
    <div>
      <h1>Creating {title}</h1>
      <hr />
      {error && <p className="error-message">{error}</p>}
      <form onSubmit={handleSubmit} className="edit-article-form">
        <div className="form-group">
          <label>Title:</label><br />
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
            className="form-control"
            maxLength="100"
          />
        </div>
        <div className="form-group">
          <label>Content:</label><br />
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
            required
            className="form-control"
            rows="25"
          />
        </div>
        <button type="submit" className="edit-article-button">Create Article</button>
      </form>
    </div>
  );
};

export default CreateArticle;
