import React, { useEffect, useState } from 'react';
import axios from './axios';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from './AuthContext';
import './EditArticle.css';

const EditArticle = () => {
  const { title } = useParams();
  const [article, setArticle] = useState({ title: '', content: '' });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const { user } = useAuth();

  useEffect(() => {
    axios.get(`http://localhost:8000/api/articles/title/${title}`)
      .then(response => {
        setArticle(response.data);
        setLoading(false);
      })
      .catch(error => {
        setError('There was an error fetching the article.');
        setLoading(false);
      });
  }, [title]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setArticle({ ...article, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (user) {
      try {
        const response = await axios.get(`http://localhost:8000/api/articles/exists/${article.title}`);
        if (response.data && article.title !== title) {
          setError('An article with this title already exists.');
        } else {
          await axios.put(`http://localhost:8000/api/articles/title/${title}?username=${user.username}`, article);
          navigate(`/wiki/${article.title}`);
        }
      } catch (error) {
        setError('There was an error updating the article.');
      }
    } else {
      setError('You must be logged in to edit this article.');
    }
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <h1>Editing {article.title}</h1>
      <hr />
      {error && <p className="error-message">{error}</p>}
      <form className="edit-article-form" onSubmit={handleSubmit}>
        <div>
          <label>Title:</label><br />
          <input
            type="text"
            name="title"
            value={article.title}
            onChange={handleInputChange}
          />
        </div>
        <div>
          <label>Content:</label><br />
          <textarea
            name="content"
            value={article.content}
            onChange={handleInputChange}
            rows="25"
          />
        </div>
        <button type="submit">Save</button>
      </form>
    </div>
  );
};

export default EditArticle;