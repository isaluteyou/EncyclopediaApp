import React, { useEffect, useState } from 'react';
import axios from './axios';
import { useParams } from 'react-router-dom';

const ArticleHistory = () => {
  const { title } = useParams();
  const [article, setArticle] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

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

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div>
      <h1>History for {article.title}</h1>
      <ul>
        {article.editHistory.map((history, index) => (
          <li key={index}>
            <p>{new Date(history.timestamp).toLocaleTimeString()}, {new Date(history.timestamp).toLocaleDateString('en-GB', { day: 'numeric', month: 'long', year: 'numeric'})} •
            edited by {history.username}</p>
            <p><strong>Old Content:</strong> {history.oldContent}</p>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ArticleHistory;