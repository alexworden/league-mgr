import React, { useState } from 'react';
import { Button, TextField, Typography, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const CreateLeague = () => {
    const [leagueName, setLeagueName] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const user = JSON.parse(localStorage.getItem('user'));
        
        if (!user || !user.id) {
            alert('You must be logged in to create a league.');
            navigate('/login');
            return;
        }

        const response = await fetch('http://localhost:8080/api/leagues', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify({ 
                name: leagueName,
                adminUserId: user.id
            }),
        });

        if (response.ok) {
            const leagueId = await response.text();
            navigate('/leagues');
        } else {
            const error = await response.text();
            alert(`Failed to create league: ${error}`);
        }
    };

    return (
        <Box sx={{ p: 3 }}>
            <Typography variant="h4" gutterBottom>Create New League</Typography>
            <form onSubmit={handleSubmit}>
                <TextField
                    label="League Name"
                    variant="outlined"
                    fullWidth
                    value={leagueName}
                    onChange={(e) => setLeagueName(e.target.value)}
                    required
                    sx={{ mb: 2 }}
                />
                <Button 
                    type="submit" 
                    variant="contained" 
                    color="primary"
                    fullWidth
                >
                    Create League
                </Button>
            </form>
        </Box>
    );
};

export default CreateLeague;
