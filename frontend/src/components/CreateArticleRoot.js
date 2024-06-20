import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../components/AuthContext';
import './EditArticle.css';

const CreateArticleRoot = () => {
    const [title, setTitle] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        navigate(`/create-article/${title}`);
    }

    const handleInputChange = (e) => {
        setTitle(e.target.value);
    };

    return (
      <div className="create-article-container">
        <h2>Insert the name of the article you want to create below</h2>
        <form className="edit-article-form" onSubmit={handleSubmit}>
          <textarea
              name="title"
              value={title}
              onChange={handleInputChange}
              rows="1"
              className="form-control"
          />
          <center><button type="submit" className="edit-article-button">Create</button></center>
        </form>
      </div>
    );
}

export default CreateArticleRoot;