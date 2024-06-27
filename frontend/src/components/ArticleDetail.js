import React, { useEffect, useState } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import axios from './axios';
import { useAuth } from '../components/AuthContext';
import './ArticleDetail.css';

const ArticleDetail = () => {
  const { title } = useParams();
  const [article, setArticle] = useState(null);
  const [newCommentary, setNewCommentary] = useState('');
  const [commentaries, setCommentaries] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchArticle = async () => {
      try {
        const response = await axios.get(`http://localhost:8000/api/articles/title/${title}`);
        setArticle(response.data);
        setLoading(false);
      } catch (error) {
        if (error.response && error.response.status === 404) {
          setArticle(null);
        } else {
          setError('There was an error fetching the article.');
        }
        setLoading(false);
      }
    };

    fetchArticle();
    fetchCommentaries();
  }, [title]);

  const fetchCommentaries = async () => {
    try {
      const response = await axios.get(`http://localhost:8000/api/articles/title/${title}/commentaries`);
      setCommentaries(response.data);
    } catch (error) {
      console.error('There was an error fetching the commentaries!', error);
    }
  };

  const handleDelete = async () => {
    const confirmed = window.confirm("Are you sure you want to delete this article?");
    if (!confirmed) {
      return;
    }

    try {
      await axios.delete(`http://localhost:8000/api/articles/title/${title}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      });
      navigate('/');
    } catch (error) {
      setError('There was an error deleting the article.');
    }
  };

  const handleCreateArticle = () => {
    navigate(`/create-article/${title}`);
  };

  const handleAddCommentary = async () => {
    if (!newCommentary.trim()) return;

    try {
      const commentaryDTO = {
        username: user.username,
        content: newCommentary,
        avatar: user.avatar,
        timestamp: new Date().toISOString()
      };

      await axios.post(`http://localhost:8000/api/articles/title/${title}/commentary`, commentaryDTO, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      });
      fetchCommentaries();

      setCommentaries(prevCommentaries => [...prevCommentaries, commentaryDTO]);
      setNewCommentary('');
    } catch (error) {
      setError('There was an error adding the commentary.');
    }
  };

  const handleDeleteCommentary = async (commentIndex) => {
    const confirmed = window.confirm("Are you sure you want to delete this commentary?");
    if (!confirmed) {
      return;
    }

    try {
      const commentary = commentaries[commentIndex];
      await axios.delete(`http://localhost:8000/api/articles/title/${title}/commentary`, {
        data: { username: commentary.username, timestamp: commentary.timestamp },
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      });

      fetchCommentaries();
    } catch (error) {
      setError('There was an error deleting the commentary.');
    }
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  const userHasRole = (role) => {
    return user && user.roles && user.roles.includes(role);
  };

  return (
    <div className="article-detail">
      {article ? (
        <>
          <div className="article-header">
            <h1>{article.title}</h1>
            <div className="article-actions">
              <button className="article-actions-button" onClick={() => navigate(`/wiki/${title}/edit`)}>Edit</button>
              <button className="article-actions-button" onClick={() => navigate(`/wiki/${title}/history`)}>History</button>
              {(userHasRole('ADMIN') || userHasRole('MODERATOR')) && (
                <button className="article-actions-button" onClick={handleDelete}>Delete</button>
              )}
            </div>
          </div>
          <hr />
          <div className="article-content" dangerouslySetInnerHTML={{ __html: article.content }}></div>
          <div className="article-commentaries">
            <h1>Commentaries</h1>
            <hr />
            <textarea
              value={newCommentary}
              onChange={(e) => setNewCommentary(e.target.value)}
              placeholder="Leave a comment about the article..."
            ></textarea>
            <button className="article-actions-button add-commentary" onClick={handleAddCommentary}>Add Commentary</button>
            <ul>
              {commentaries.map((comment, index) => (
                <li key={index} className="comment-item">
                <div className="comment-header">
                <img src={`http://localhost:8000/uploads/${comment.avatar}`} alt={`${comment.username}'s avatar`} className="comment-avatar" />
                    <div>
                      <div className="comment-username">{comment.username}</div>
                      <div className="comment-timestamp">{new Date(comment.timestamp).toLocaleString()}</div>
                    </div>
                    <div className="comment-delete">
                        {user && (comment.username === user.username || userHasRole('ADMIN') || userHasRole('MODERATOR')) && (
                        <button onClick={() => handleDeleteCommentary(index)} className="commentary-delete"><FontAwesomeIcon icon={faTrash}/> Delete</button>
                       )}
                    </div>
                </div>
                <div className="comment-content">
                  {comment.content}
                </div>
              </li>
              ))}
            </ul>
          </div>
        </>
      ) : (
        <>
        <div className="article-header">
          <h1>Article "{title}" Not Found</h1>
        </div>
          <hr />
          {user && (
            <div className="article-actions">
              <button onClick={handleCreateArticle} className="create-button">Create Article</button>
            </div>
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