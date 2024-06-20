import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../components/AuthContext';
import axios from './axios';
import './EditArticle.css';

const CreateArticleRoot = () => {
    const [title, setTitle] = useState('');
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const capitalizeFirstLetter = (string) => {
      return string.charAt(0).toUpperCase() + string.slice(1);
    };

    const handleSubmit = async (e) => {
      e.preventDefault();
      if (title.trim() === '') {
        setError('The title cannot be empty.');
        return;
      }
      if (title.length > 100) {
        setError('The title cannot exceed 255 characters.');
        return;
      }

      const capitalizedTitle = capitalizeFirstLetter(title);

      try {
          const response = await axios.get(`http://localhost:8000/api/articles/exists/${capitalizedTitle}`);
          if (response.data) {
              setError('An article with this title already exists.');
          } else {
              navigate(`/create-article/${capitalizedTitle}`);
          }
      } catch (error) {
          setError('There was an error checking the article title.');
      }
  }

    const handleInputChange = (e) => {
        setTitle(e.target.value);
        setError(null);
    };

    return (
      <div className="create-article-container">
        <h2>Insert the name of the article you want to create below</h2>
        {error && <p className="error-message">{error}</p>}
        <form className="edit-article-form" onSubmit={handleSubmit}>
          <textarea
              name="title"
              value={title}
              onChange={handleInputChange}
              rows="1"
              className="form-control"
              maxLength="100"
          />
          <center><button type="submit" className="edit-article-button">Create</button></center>
        </form>
      </div>
    );
}

export default CreateArticleRoot;