import React, { useEffect, useState } from 'react';
import axios from './axios';
import { useParams } from 'react-router-dom';
import './ArticleHistory.css';

const ArticleHistory = () => {
  const { title } = useParams();
  const [article, setArticle] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [expandedHistory, setExpandedHistory] = useState({});

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

  const toggleExpand = (index) => {
    setExpandedHistory(prevState => ({
      ...prevState,
      [index]: !prevState[index]
    }));
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div className="article-history">
      <h1>History for {article.title}</h1>
      <ul>
        {article.editHistory.map((history, index) => (
          <li key={index}>
            <p>{new Date(history.timestamp).toLocaleTimeString()}, {new Date(history.timestamp).toLocaleDateString('en-GB', { day: 'numeric', month: 'long', year: 'numeric'})} •
            edited by <a href={`/profile/${history.username}`}>{history.username}</a></p>
            <button onClick={() => toggleExpand(index)}>
              {expandedHistory[index] ? 'Hide Old Content' : 'Show Old Content'}
            </button>
            {expandedHistory[index] && (
              <div className="old-content">
                <div className="old-content-text">{history.oldContent}</div>
              </div>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ArticleHistory;
