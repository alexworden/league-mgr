import React, { useState } from 'react';
import { 
  Box, 
  Paper, 
  TextField, 
  Button, 
  Typography, 
  Container,
  Alert,
  Snackbar
} from '@mui/material';
import { useNavigate } from 'react-router-dom';

function Signup() {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    phone: ''
  });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const response = await fetch('http://localhost:8080/api/auth/signup', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      const data = await response.json();
      
      if (!response.ok) {
        throw new Error(data.message || 'Something went wrong');
      }

      // Store token and navigate
      localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify({
        id: data.userId,
        email: data.email,
        firstName: data.firstName,
        lastName: data.lastName,
        phone: data.phone
      }));
      navigate('/');
      
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <Container component="main" maxWidth="sm">
      <Paper 
        elevation={3} 
        sx={{ 
          marginTop: 8, 
          padding: 4, 
          display: 'flex', 
          flexDirection: 'column', 
          alignItems: 'center' 
        }}
      >
        <Typography component="h1" variant="h5" sx={{ mb: 3 }}>
          Create Account
        </Typography>
        
        <Box component="form" onSubmit={handleSubmit} sx={{ width: '100%' }}>
          <Box sx={{ display: 'flex', gap: 2, mb: 2 }}>
            <TextField
              required
              fullWidth
              name="firstName"
              label="First Name"
              value={formData.firstName}
              onChange={handleChange}
            />
            <TextField
              required
              fullWidth
              name="lastName"
              label="Last Name"
              value={formData.lastName}
              onChange={handleChange}
            />
          </Box>
          
          <TextField
            margin="normal"
            required
            fullWidth
            name="email"
            label="Email Address"
            type="email"
            value={formData.email}
            onChange={handleChange}
          />
          
          <TextField
            margin="normal"
            required
            fullWidth
            name="password"
            label="Password"
            type="password"
            value={formData.password}
            onChange={handleChange}
          />
          
          <TextField
            margin="normal"
            fullWidth
            name="phone"
            label="Phone Number (Optional)"
            value={formData.phone}
            onChange={handleChange}
          />

          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2, fontSize: '1.1rem', py: 1.2 }}
          >
            Create Account
          </Button>
          
          <Button
            fullWidth
            variant="text"
            onClick={() => navigate('/login')}
            sx={{ fontSize: '1rem' }}
          >
            Already have an account? Sign in
          </Button>
        </Box>
      </Paper>

      <Snackbar 
        open={!!error} 
        autoHideDuration={6000} 
        onClose={() => setError('')}
      >
        <Alert severity="error" onClose={() => setError('')}>
          {error}
        </Alert>
      </Snackbar>
    </Container>
  );
}

export default Signup;
