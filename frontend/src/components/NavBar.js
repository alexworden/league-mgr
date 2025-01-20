import React from 'react';
import { AppBar, Toolbar, Typography, Button, Box, Avatar } from '@mui/material';
import { useNavigate } from 'react-router-dom';

function NavBar() {
  const navigate = useNavigate();
  const isAuthenticated = !!localStorage.getItem('token');
  const user = isAuthenticated ? JSON.parse(localStorage.getItem('user')) : null;

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    navigate('/login');
  };

  const handleLogin = () => {
    navigate('/login');
  };

  return (
    <AppBar position="static" sx={{ backgroundColor: '#2c3e50' }}>
      <Toolbar>
        <Typography 
          variant="h6" 
          component="div" 
          sx={{ 
            flexGrow: 1, 
            cursor: 'pointer',
            fontSize: '1.5rem'
          }}
          onClick={() => navigate('/')}
        >
          Snooker League Manager
        </Typography>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          {isAuthenticated ? (
            <>
              <Button 
                color="inherit" 
                onClick={() => navigate('/leagues')}
                sx={{ fontSize: '1.1rem' }}
              >
                Leagues
              </Button>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <Avatar sx={{ bgcolor: '#34495e', width: 32, height: 32 }}>
                  {user && user.firstName && user.lastName ? 
                    (user.firstName[0] + user.lastName[0]).toUpperCase() : 
                    'U'}
                </Avatar>
                <Typography variant="body1" sx={{ color: 'white' }}>
                  {user && user.firstName && user.lastName ? 
                    `${user.firstName} ${user.lastName}` : 
                    'User'}
                </Typography>
              </Box>
              <Button 
                color="inherit" 
                onClick={handleLogout}
                sx={{ fontSize: '1.1rem' }}
              >
                Logout
              </Button>
            </>
          ) : (
            <>
              <Button 
                color="inherit" 
                onClick={handleLogin}
                sx={{ fontSize: '1.1rem' }}
              >
                Login
              </Button>
              <Button 
                color="inherit" 
                onClick={() => navigate('/signup')}
                sx={{ fontSize: '1.1rem' }}
              >
                Sign Up
              </Button>
            </>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
}

export default NavBar;
