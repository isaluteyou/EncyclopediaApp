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

  const handleSubmit = (e) => {
    e.preventDefault();
    if (user) {
      axios.put(`http://localhost:8000/api/articles/title/${title}?username=${user.username}`, article)
        .then(response => {
          navigate(`/wiki/${title}`);
        })
        .catch(error => {
          setError('There was an error updating the article.');
        });
    } else {
      setError('You must be logged in to edit this article.');
    }
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div>
      <h1>Editing {article.title}</h1>
      <hr />
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
            rows="30"
          />
        </div>
        <button type="submit">Save</button>
      </form>
    </div>
  );
};

export default EditArticle;