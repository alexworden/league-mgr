import React, { useEffect, useState } from 'react';
import { Button, TextField, Typography, Box, Alert } from '@mui/material';
import { useNavigate, useParams } from 'react-router-dom';

const EditLeague = () => {
    const { leagueId } = useParams();
    const [leagueName, setLeagueName] = useState('');
    const [numberOfTeams, setNumberOfTeams] = useState(8);
    const [playersPerTeam, setPlayersPerTeam] = useState(3);
    const [signupUrl, setSignupUrl] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchLeague = async () => {
            try {
                const user = JSON.parse(localStorage.getItem('user'));
                if (!user) {
                    setError('You must be logged in to edit a league.');
                    return;
                }

                const response = await fetch(`http://localhost:8080/api/leagues/${leagueId}`, {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                });
                
                if (response.ok) {
                    const data = await response.json();
                    if (data.adminUserId !== user.id) {
                        setError('You can only edit leagues you created.');
                        return;
                    }
                    setLeagueName(data.name);
                    setNumberOfTeams(data.numberOfTeams);
                    setPlayersPerTeam(data.playersPerTeam);
                    setSignupUrl(data.signupUrl);
                } else {
                    setError('Failed to fetch league details.');
                }
            } catch (err) {
                setError('Error fetching league details.');
            }
        };
        fetchLeague();
    }, [leagueId]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`http://localhost:8080/api/leagues/${leagueId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify({ 
                    name: leagueName,
                    numberOfTeams,
                    playersPerTeam,
                    signupUrl
                }),
            });

            if (response.ok) {
                navigate('/leagues');
            } else {
                const errorData = await response.text();
                setError(`Failed to update league: ${errorData}`);
            }
        } catch (err) {
            setError('Error updating league.');
        }
    };

    if (error) {
        return (
            <Box sx={{ p: 3 }}>
                <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>
                <Button variant="contained" onClick={() => navigate('/leagues')}>Back to Leagues</Button>
            </Box>
        );
    }

    return (
        <Box sx={{ p: 3 }}>
            <Typography variant="h4" gutterBottom>Edit League</Typography>
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
                <TextField
                    label="Number of Teams"
                    type="number"
                    variant="outlined"
                    fullWidth
                    value={numberOfTeams}
                    onChange={(e) => setNumberOfTeams(e.target.value)}
                    required
                    sx={{ mb: 2 }}
                />
                <TextField
                    label="Players per Team"
                    type="number"
                    variant="outlined"
                    fullWidth
                    value={playersPerTeam}
                    onChange={(e) => setPlayersPerTeam(e.target.value)}
                    required
                    sx={{ mb: 2 }}
                />
                <TextField
                    label="Signup URL"
                    variant="outlined"
                    fullWidth
                    value={signupUrl}
                    onChange={(e) => setSignupUrl(e.target.value)}
                    required
                    sx={{ mb: 2 }}
                />
                <Box sx={{ display: 'flex', gap: 2 }}>
                    <Button type="submit" variant="contained" color="primary" fullWidth>Update League</Button>
                    <Button variant="outlined" color="secondary" fullWidth onClick={() => navigate('/leagues')}>Cancel</Button>
                </Box>
            </form>
        </Box>
    );
};

export default EditLeague;
