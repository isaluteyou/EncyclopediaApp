import React, { useState, useEffect } from 'react';
import { useLocation, Link } from 'react-router-dom';
import axios from './axios';
import './SearchResults.css';

const SearchResults = () => {
  const [articles, setArticles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const location = useLocation();
  const query = new URLSearchParams(location.search).get('query');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(`http://localhost:8000/api/articles/search?query=${encodeURIComponent(query)}`);
        setArticles(response.data);
      } catch (error) {
        setError('There was an error fetching the search results.');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [query]);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div className="search-results">
      <h1>Search Results for "{query}"</h1>
      <hr />
      <ul>
        {articles.map((article) => (
          <li key={article.id} className="search-element">
            <Link to={`/wiki/${article.title}`}>{article.title}</Link><br />
            Content is: "{article.content.substring(0, 100)}..."
          </li>
        ))}
      </ul>
    </div>
  );
};

export default SearchResults;