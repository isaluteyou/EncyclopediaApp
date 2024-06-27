import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from './axios';
import './CategoryDetail.css';

const CategoryDetail = () => {
  const { categoryName } = useParams();
  const [category, setCategory] = useState(null);
  const [articles, setArticles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCategoryDetail = async () => {
      try {
        const response = await axios.get(`http://localhost:8000/api/categories/${categoryName}`);
        setCategory(response.data);
        setArticles(response.data.articles);
        setLoading(false);
      } catch (error) {
        if (error.response && error.response.status === 404) {
          setError('Category does not exist.');
        } else {
          setError('There was an error fetching the category details.');
        }
        setLoading(false);
      }
    };

    fetchCategoryDetail();
  }, [categoryName]);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div className="category-detail">
      {category ? (
        <>
          <h1>{category.name}</h1>
          <hr />
          <p>{category.description}</p>
          <h2>Articles</h2>
          <ul>
            {articles.map((article) => (
              <li key={article.id}>
                <a href={`/wiki/${article.title}`}>{article.title}</a>
              </li>
            ))}
          </ul>
        </>
      ) : (
        <div>Category not found.</div>
      )}
    </div>
  );
};

export default CategoryDetail;
