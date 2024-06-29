import React, { useState, useEffect } from 'react';
import axios from './axios';
import { useAuth } from './AuthContext';
import { toDate, formatInTimeZone } from 'date-fns-tz';
import { format } from 'date-fns';
import './AdminDashboard.css';

const AdminDashboard = () => {
  const { user } = useAuth();
  const [bannedUsers, setBannedUsers] = useState([]);
  const [banReason, setBanReason] = useState('');
  const [banExpire, setBanExpire] = useState('');
  const [selectedUsername, setSelectedUsername] = useState('');
  const [error, setError] = useState(null);

  const convertToBucharestTime = (date) => {
    const bucharestTimeZone = 'Europe/Bucharest';
    const zonedDate = toDate(date, { timeZone: bucharestTimeZone });
    const formattedDate = formatInTimeZone(zonedDate, bucharestTimeZone, 'yyyy-MM-dd\'T\'HH:mm:ssXXX');
    return formattedDate;
  };
  

  useEffect(() => {
    fetchBannedUsers();
  }, []);

  const fetchBannedUsers = async () => {
    try {
      const response = await axios.get('http://localhost:8000/api/banned-users/active', {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      });
      setBannedUsers(response.data);
    } catch (error) {
      console.error('There was an error fetching the banned users!', error);
    }
  };
  
  const handleBanUser = async () => {
    try {
      const banExpireBucharest = convertToBucharestTime(banExpire);
      console.log(banExpireBucharest);
      await axios.post('http://localhost:8000/api/banned-users/ban', {
        username: selectedUsername,
        bannedBy: user.username,
        reason: banReason,
        banExpire: banExpireBucharest
      }, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      });
      alert('User banned successfully');
      fetchBannedUsers();
    } catch (error) {
        if (error.response && error.response.data) {
            setError(`Error banning user: ${error.response.data}`);
        } else {
            console.error('There was an error banning the user!', error);
        }
    }
  };

  const handleUnbanUser = async (username) => {
    try {
      await axios.post(`http://localhost:8000/api/banned-users/unban/${username}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      });
      fetchBannedUsers();
    } catch (error) {
         console.error('There was an error banning the user!', error);
    }
  };

  const convertToBucharestTimeFormat = (date) => {
    const bucharestTimeZone = 'Europe/Bucharest';
    const zonedDate = toDate(date, { timeZone: bucharestTimeZone });
    const formattedDate = formatInTimeZone(zonedDate, bucharestTimeZone, 'yyyy-MM-dd\'T\'HH:mm:ssXXX');
    return format(new Date(formattedDate), 'PPpp');
  };

  return (
    <div className="admin-dashboard">
      <h1>Admin Dashboard</h1>
      <div className="section">
        <h2>Banned Users</h2>
        <ul>
        {bannedUsers.map((bannedUser, index) => (
            <li key={index}>
              <a href={`../profile/${bannedUser.username}`}>{bannedUser.username}</a> - 
              {bannedUser.reason} (Banned By: <a href={`../profile/${bannedUser.bannedBy}`}>{bannedUser.bannedBy}</a>, 
              Expires: {convertToBucharestTimeFormat(bannedUser.banExpire)})
              <button onClick={() => handleUnbanUser(bannedUser.username)}>Unban</button>
            </li>
          ))}
        </ul>
      </div>
      <div className="section">
        <h2>Ban User</h2>
        {error && (
            <p style={{color: "red"}}>{error}</p>
        )}
        <input
          type="text"
          placeholder="Username"
          value={selectedUsername}
          onChange={(e) => setSelectedUsername(e.target.value)}
        />
        <input
          type="text"
          placeholder="Ban Reason"
          value={banReason}
          onChange={(e) => setBanReason(e.target.value)}
        />
        <input
          type="datetime-local"
          placeholder="Ban Expiry Date"
          value={banExpire}
          onChange={(e) => setBanExpire(e.target.value)}
        />
        <button onClick={handleBanUser}>Ban User</button>
      </div>
    </div>
  );
};

export default AdminDashboard;