import React, { useEffect, useState } from 'react';
import { Button, Typography, List, ListItem, ListItemText, Box, Paper } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const LeaguesList = () => {
    const [leagues, setLeagues] = useState([]);
    const [currentUser, setCurrentUser] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const user = JSON.parse(localStorage.getItem('user'));
        setCurrentUser(user);
        
        const fetchLeagues = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/leagues', {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                });
                if (response.ok) {
                    const data = await response.json();
                    setLeagues(data);
                } else {
                    console.error('Failed to fetch leagues');
                }
            } catch (error) {
                console.error('Error fetching leagues:', error);
            }
        };
        fetchLeagues();
    }, []);

    const handleEdit = (leagueId) => {
        navigate(`/leagues/edit/${leagueId}`);
    };

    const handleDelete = async (leagueId) => {
        if (!currentUser) {
            alert('You must be logged in to delete a league.');
            return;
        }

        const league = leagues.find(l => l.id === leagueId);
        if (league.adminUserId !== currentUser.id) {
            alert('You can only delete leagues you created.');
            return;
        }

        if (window.confirm('Are you sure you want to delete this league?')) {
            try {
                const response = await fetch(`http://localhost:8080/api/leagues/${leagueId}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                });
                
                if (response.ok) {
                    setLeagues(leagues.filter(league => league.id !== leagueId));
                } else {
                    alert('Failed to delete league.');
                }
            } catch (error) {
                console.error('Error deleting league:', error);
                alert('Error deleting league.');
            }
        }
    };

    const canEditLeague = (league) => {
        return currentUser && league.adminUserId === currentUser.id;
    };

    return (
        <Box sx={{ p: 3 }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                <Typography variant="h4">Leagues</Typography>
                {currentUser && (
                    <Button 
                        variant="contained" 
                        color="primary" 
                        onClick={() => navigate('/leagues/create')}
                    >
                        Create New League
                    </Button>
                )}
            </Box>
            
            <List>
                {leagues.map(league => (
                    <Paper key={league.id} sx={{ mb: 2 }}>
                        <ListItem>
                            <ListItemText 
                                primary={league.name}
                                secondary={`Created by: ${league.adminUserId === currentUser?.id ? 'You' : league.adminUserId}`}
                            />
                            {canEditLeague(league) && (
                                <Box>
                                    <Button 
                                        onClick={() => handleEdit(league.id)}
                                        color="primary"
                                        sx={{ mr: 1 }}
                                    >
                                        Edit
                                    </Button>
                                    <Button 
                                        onClick={() => handleDelete(league.id)}
                                        color="error"
                                    >
                                        Delete
                                    </Button>
                                </Box>
                            )}
                        </ListItem>
                    </Paper>
                ))}
            </List>
        </Box>
    );
};

export default LeaguesList;
