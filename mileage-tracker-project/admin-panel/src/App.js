import React, { useState, useEffect } from 'react';
import axios from 'axios';
import VehiclesTableCard from './components/VehiclesTableCard';
import { AppBar, Toolbar, Typography, Box } from '@mui/material';
import './App.css';

const App = () => {
  const [vehicles, setVehicles] = useState([]);

  const fetchVehicles = async () => {
    try {
      const response = await axios.get('http://localhost:8080/vehicles');
      if (!response.data.hasError) {
        setVehicles(response.data.payload);
      } else {
        console.error('Error fetching vehicles:', response.data.friendlyMessage);
      }
    } catch (error) {
      console.error('Error fetching vehicles:', error);
    }
  };

  useEffect(() => {
    fetchVehicles();
  }, []);

  return (
    <div>
      <AppBar position="sticky">
        <Toolbar>
          <Box component="img" src="https://www.largeherds.co.za/wp-content/uploads/2024/01/logo-placeholder-image.png" alt="Company Logo" sx={{ height: 50, marginRight: 2 }} />
          <Typography variant="h6">Araç KM Takip Sistemi</Typography>
        </Toolbar>
      </AppBar>
      <VehiclesTableCard vehicles={vehicles} fetchVehicles={fetchVehicles} />
    </div>
  );
};

export default App;
