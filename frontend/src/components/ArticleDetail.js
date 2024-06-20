import React, { useEffect, useState } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
import axios from './axios';
import { useAuth } from '../components/AuthContext';
import './ArticleDetail.css';

const ArticleDetail = () => {
  const { title } = useParams();
  const [article, setArticle] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    axios.get(`http://localhost:8000/api/articles/title/${title}`)
      .then(response => {
        setArticle(response.data);
        setLoading(false);
      })
      .catch(error => {
        if (error.response && error.response.status === 404) {
          setArticle(null);
        } else {
          setError('There was an error fetching the article.');
        }
        setLoading(false);
      });
  }, [title]);

  const handleCreateArticle = () => {
    navigate(`/create-article/${title}`);
  };

  const convertToHtml = (text) => {
    const paragraphs = text.split('\n\n');
    return paragraphs.map((paragraph, index) => (
      <p key={index}>
        {paragraph.split('\n').map((line, lineIndex) => (
          <React.Fragment key={lineIndex}>
            {line}
            <br />
          </React.Fragment>
        ))}
      </p>
    ));
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div className="article-detail">
      {article ? (
        <>
          <div className="article-header">
            <h1>{article.title}</h1>
            <div className="article-actions">
              <button onClick={() => navigate(`/wiki/${title}/edit`)}>Edit</button>
              <button onClick={() => navigate(`/wiki/${title}/history`)}>History</button>
            </div>
          </div>
          <hr />
          <div className="article-content">
            {convertToHtml(article.content)}
          </div>
        </>
      ) : (
        <>
          <h1>Article Not Found</h1>
          {user && (
            <button onClick={handleCreateArticle} className="create-article-button">
              Create Article
            </button>
          )}
          {!user && (
            <p>Please <Link to="/login">log in</Link> to create this article.</p>
          )}
        </>
      )}
    </div>
  );
};

export default ArticleDetail;