import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ThemeProvider, CssBaseline, Container } from '@mui/material';
import NavBar from './components/NavBar';
import Login from './components/Login';
import Signup from './components/Signup';

function App() {
  return (
    <Router>
      <ThemeProvider theme={{}}>
        <CssBaseline />
        <div style={{ 
          minHeight: '100vh',
          display: 'flex',
          flexDirection: 'column',
          backgroundColor: '#f5f6fa'
        }}>
          <NavBar />
          <Container component="main" sx={{ mt: 4, mb: 4, flex: 1 }}>
            <Routes>
              <Route path="/login" element={<Login />} />
              <Route path="/signup" element={<Signup />} />
              <Route path="/" element={
                <div style={{ textAlign: 'center' }}>
                  <h1 style={{ fontSize: '2.5rem', color: '#2c3e50' }}>
                    Welcome to Snooker League Manager
                  </h1>
                  <p style={{ fontSize: '1.2rem', color: '#34495e' }}>
                    Manage your leagues, teams, and matches all in one place
                  </p>
                </div>
              } />
            </Routes>
          </Container>
        </div>
      </ThemeProvider>
    </Router>
  );
}

export default App;
