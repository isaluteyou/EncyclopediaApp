import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from './axios';
import './UserProfile.css';

const UserProfile = () => {
  const { username } = useParams();
  const [profile, setProfile] = useState(null);
  const [userProfile, setUserProfile] = useState(null);
  const [contributions, setContributions] = useState([]);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await axios.get(`http://localhost:8000/api/user_profile/username/${username}`);
        setProfile(response.data);
      } catch (error) {
        console.error('There was an error fetching the user profile!', error);
      }
    };

    const fetchUserProfile = async () => {
        try {
          const response = await axios.get(`http://localhost:8000/api/user_profile/username/${username}/additional`);
          setUserProfile(response.data);
        } catch (error) {
          console.error('There was an error fetching the user profile!', error);
        }
      };

    const fetchContributions = async () => {
        try {
          const response = await axios.get(`http://localhost:8000/api/articles/users/${username}/contributions`);
          setContributions(response.data);
        } catch (error) {
          console.error('There was an error fetching the user contributions!', error);
        }
      };

    fetchProfile();
    fetchUserProfile();
    fetchContributions();
  }, [username]);

  return (
    <div>
        <div className="user-profile">
        {profile && userProfile && (
            <div className="user-profile-info">
                <img src={`http://localhost:8000/uploads/${profile.avatar}`} alt="Avatar" />
                <div className="info1">
                    <h1>{username} - {userProfile.numberOfEdits} edits</h1>
                    <p><strong>Joined:</strong> {new Date(userProfile.createdAt).toLocaleString()}</p>
                    <p><strong>Age:</strong> {profile.age}</p>
                    <p><strong>Gender:</strong> {profile.gender}</p>
                    <p><strong>Location:</strong> {profile.location}</p>
                    <p><strong>About:</strong> {profile.about}</p>
                </div>
            </div>
        )}
        </div>
        <h1>Contributions</h1>
        <hr />
        <ul>
            {contributions.map((edit, index) => (
            <li key={index}>
                {new Date(edit.timestamp).toLocaleString()} - <a href={`/wiki/${edit.title}`}>{edit.title}</a>
            </li>
            ))}
        </ul>
    </div>
  );
};

export default UserProfile;
