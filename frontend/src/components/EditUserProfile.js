import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from './axios';
import { useAuth } from '../components/AuthContext';
import './EditUserProfile.css';

const EditUserProfile = () => {
  const { username } = useParams();
  const [profile, setProfile] = useState({
    age: '',
    gender: '',
    location: '',
    about: '',
    avatar: ''
  });
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const { user } = useAuth();

  useEffect(() => {
    axios.get(`http://localhost:8000/api/user_profile/username/${username}`)
      .then(response => {
        setProfile(response.data);
        setLoading(false);
      })
      .catch(error => {
        setError('There was an error fetching the profile.');
        setLoading(false);
      });
  }, [username]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setProfile({ ...profile, [name]: value });
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('age', profile.age);
    formData.append('gender', profile.gender);
    formData.append('location', profile.location);
    formData.append('about', profile.about);
    if (file) {
      formData.append('file', file);
    }

    try {
      await axios.put(`http://localhost:8000/api/user_profile/username/${username}/edit`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      navigate(`/profile/${username}`);
    } catch (error) {
      setError('There was an error updating the profile.');
    }
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div className="edit-user-profile">
      <h1>Edit {username}'s Profile</h1>
      <form onSubmit={handleSubmit}>
        <div className='edit-user-profile-item'>
          <label>Age:</label>
          <input
            type="number"
            name="age"
            value={profile.age}
            onChange={handleInputChange}
          />
        </div>
        <div className='edit-user-profile-item'>
          <label>Gender:</label>
          <input
            type="text"
            name="gender"
            value={profile.gender}
            onChange={handleInputChange}
          />
        </div>
        <div className='edit-user-profile-item'>
          <label>Location:</label>
          <input
            type="text"
            name="location"
            value={profile.location}
            onChange={handleInputChange}
          />
        </div>
        <div className='edit-user-profile-item'>
          <label>About:</label>
          <textarea
            name="about"
            value={profile.about}
            onChange={handleInputChange}
            maxLength="150"
          />
        </div>
        <div className='edit-user-profile-item'>
          <label>Avatar:</label>
          <input
            type="file"
            name="file"
            onChange={handleFileChange}
          />
        </div>
        <button type="submit">Save</button>
      </form>
    </div>
  );
};

export default EditUserProfile;