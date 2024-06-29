import React, { useState, useEffect } from 'react';
import axios from './axios';
import { Link } from 'react-router-dom';
import './RecentChanges.css';

const RecentChanges = () => {
  const [changes, setChanges] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [visibleGroups, setVisibleGroups] = useState(3);

  useEffect(() => {
    const fetchRecentChanges = async () => {
      try {
        const response = await axios.get('http://localhost:8000/api/recent-changes');
        setChanges(response.data);
        setLoading(false);
      } catch (error) {
        setError('There was an error fetching the recent changes.');
        setLoading(false);
      }
    };

    fetchRecentChanges();
  }, []);

  const handleExpand = () => {
    setVisibleGroups(visibleGroups + 3);
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div className="recent-changes">
      <h1>Recent Changes</h1>
      {changes.map((group) => (
        <div key={group.date}>
          <h2>{new Date(group.date).toLocaleDateString('en-GB', { day: 'numeric', month: 'long', year: 'numeric' })}</h2>
          <ul>
            {group.changes.map((change, index) => (
              <li key={index}>
                {new Date(change.timestamp).toLocaleTimeString()} - 
                Article <Link to={`/wiki/${change.articleTitle}`}>{change.articleTitle}</Link> was edited by <Link to={`/profile/${change.username}`}>
                {change.username}</Link> (<Link to={`/wiki/${change.articleTitle}`}>new</Link> | <Link to={`/wiki/${change.articleTitle}/history`}>old</Link>)
              </li>
            ))}
          </ul>
        </div>
      ))}
      {visibleGroups < changes.length && (
        <button onClick={handleExpand} className="expand-button">Show More</button>
      )}
    </div>
  );
};

export default RecentChanges;
