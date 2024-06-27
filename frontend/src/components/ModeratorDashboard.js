import React, { useEffect, useState } from 'react';
import axios from './axios';
import { useAuth } from '../components/AuthContext';
import './ModeratorDashboard.css';

const ModeratorDashboard = () => {
  const { user } = useAuth();
  const [categories, setCategories] = useState([]);
  const [newCategory, setNewCategory] = useState({ name: '', description: '' });
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await axios.get('http://localhost:8000/api/categories');
        setCategories(response.data);
      } catch (error) {
        console.error('There was an error fetching the categories!', error);
      }
    };

    fetchCategories();
  }, []);

  const handleCreateCategory = async () => {
    if (!newCategory.name.trim()) {
      setError('Category name is required');
      return;
    }

    try {
      const response = await axios.post('http://localhost:8000/api/categories', newCategory, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      });
      setCategories([...categories, response.data]);
      setNewCategory({ name: '', description: '' });
      setSuccess('Category created successfully');
      setError(null);
    } catch (error) {
      setError('There was an error creating the category');
      setSuccess(null);
    }
  };

  return (
    <div className="moderator-dashboard">
      <h1>Moderator Dashboard</h1>
      <div className="create-category">
        <h2>Create Category</h2>
        <div className="flex">
          <div>
            <input
              type="text"
              placeholder="Category Name"
              value={newCategory.name}
              onChange={(e) => setNewCategory({ ...newCategory, name: e.target.value })}
            />
            <textarea
              placeholder="Category Description"
              value={newCategory.description}
              onChange={(e) => setNewCategory({ ...newCategory, description: e.target.value })}
            />
          </div>
          <div>
            <button onClick={handleCreateCategory}>Create Category</button>
            {error && <p className="error">{error}</p>}
            {success && <p className="success">{success}</p>}
          </div>
        </div>
      </div>
      <div className="categories-list">
        <h2>Existing Categories</h2>
        <ul>
          {categories.map((category) => (
            <li key={category.id}>
              <strong>{category.name}</strong>: {category.description}
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default ModeratorDashboard;