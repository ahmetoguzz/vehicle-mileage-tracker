import React, { useState } from 'react';
import axios from 'axios';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton, TablePagination, Box, Button } from '@mui/material';
import { Add, Delete, Edit, Send, GetApp } from '@mui/icons-material';
import VehicleModal from './VehicleModal';
import '../App.css';

const VehiclesTableCard = ({ vehicles, fetchVehicles }) => {
    const [openModal, setOpenModal] = useState(false);
    const [vehicleData, setVehicleData] = useState({
        employeeId: '',
        plateNumber: '',
        brand: '',
        model: '',
        color: '',
        hgsNumber: '',
        lastKm: '',
        createDate: '',
        modelYear: ''
    });
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(10);

    const handleOpenModal = (vehicle = {}) => {
        setVehicleData(vehicle);
        setOpenModal(true);
    };

    const handleCloseModal = () => {
        setOpenModal(false);
    };

    const handleSubmit = async () => {
        try {
            const url = vehicleData.id ? `http://localhost:8080/vehicle/update/${vehicleData.id}` : 'http://localhost:8080/vehicle/create';
            const method = vehicleData.id ? 'put' : 'post';
            const data = { ...vehicleData, employee: null };
            const response = await axios[method](url, data);

            if (response.status === 200 || response.status === 201) {
                fetchVehicles();
                handleCloseModal();
            } else {
                console.error('Error creating/updating vehicle:', response);
            }
        } catch (error) {
            console.error('Error creating/updating vehicle:', error);
        }
    };

    const handleDelete = async (id) => {
        console.log('Deleting vehicle with id:', id);
        try {
            await axios.delete(`http://localhost:8080/vehicle/delete/${id}`);
            fetchVehicles();
        } catch (error) {
            console.error('Error deleting vehicle:', error);
        }
    };

    const handleUpdateRequest = async (employeeId) => {
        try {
            const response = await axios.get(`http://localhost:8080/send-update-link/${employeeId}`);
            if (response.status === 200) {
                alert('Update link has been sent to the employee.');
            } else {
                console.error('Error sending update link:', response);
                alert('Failed to send update link.');
            }
        } catch (error) {
            console.error('Error sending update link:', error);
            alert('Failed to send update link.');
        }
    };

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    const paginatedVehicles = Array.isArray(vehicles) ? vehicles.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage) : [];

    const handleExportCSV = () => {
        const csvRows = [
            ["Kullanan Kişi", "Plaka", "Marka", "Model", "Renk", "HGS Numarası", "Son KM", "Oluşturma Tarihi", "Model Yılı"]
        ];
        vehicles.forEach(vehicle => {
            const row = [
                vehicle.employee ? `${vehicle.employee.name} ${vehicle.employee.surname}` : '-',
                vehicle.plateNumber,
                vehicle.brand,
                vehicle.model,
                vehicle.color,
                vehicle.hgsNumber,
                vehicle.lastKm,
                vehicle.createDate ? vehicle.createDate.slice(0, 10) : '',
                vehicle.modelYear
            ];
            csvRows.push(row);
        });

        const csvContent = "data:text/csv;charset=utf-8," + csvRows.map(e => e.join(",")).join("\n");
        const link = document.createElement("a");
        link.setAttribute("href", encodeURI(csvContent));
        link.setAttribute("download", "vehicles.csv");
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    };


    return (
        <div>
            <Box display="flex" justifyContent="center" mb={2} style={{ margin: 20 }}>
                <Button variant="contained" color="primary" onClick={() => handleOpenModal({})} startIcon={<Add />} style={{ marginRight: 10 }}>Araç Ekle</Button>
                <Button variant="contained" color="secondary" onClick={handleExportCSV} startIcon={<GetApp />}>Dışa aktar - CSV</Button>
            </Box>
            <TableContainer component={Paper} style={{ marginTop: 16, marginLeft: 'auto', marginRight: 'auto', width: '80%' }}>
                <Table sx={{ minWidth: 700 }} aria-label="customized table">
                    <TableHead style={{ backgroundColor: '#f5f5f5' }}>
                        <TableRow>
                            <TableCell>Kullanan Kişi</TableCell>
                            <TableCell>Plaka</TableCell>
                            <TableCell>Marka</TableCell>
                            <TableCell>Model</TableCell>
                            <TableCell>Renk</TableCell>
                            <TableCell>HGS Numarası</TableCell>
                            <TableCell>Son KM</TableCell>
                            <TableCell>Oluşturma Tarihi</TableCell>
                            <TableCell>Model Yılı</TableCell>
                            <TableCell>Aksiyonlar</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {Array.isArray(paginatedVehicles) && paginatedVehicles.map((vehicle) => (
                            <TableRow key={vehicle.id}>
                                <TableCell>{vehicle.employee ? `${vehicle.employee.name} ${vehicle.employee.surname}` : '-'}</TableCell>
                                <TableCell>{vehicle.plateNumber}</TableCell>
                                <TableCell>{vehicle.brand}</TableCell>
                                <TableCell>{vehicle.model}</TableCell>
                                <TableCell>{vehicle.color}</TableCell>
                                <TableCell>{vehicle.hgsNumber}</TableCell>
                                <TableCell>{vehicle.lastKm}</TableCell>
                                <TableCell>{vehicle.createDate ? vehicle.createDate.slice(0, 10) : ''}</TableCell> { }
                                <TableCell>{vehicle.modelYear}</TableCell>
                                <TableCell>
                                    <IconButton onClick={() => handleDelete(vehicle.id)}>
                                        <Delete color="error" />
                                    </IconButton>
                                    <IconButton onClick={() => handleUpdateRequest(vehicle.employee ? vehicle.employee.employeeId : null)}>
                                        <Send color="success" />
                                    </IconButton>
                                    <IconButton onClick={() => handleOpenModal(vehicle)}>
                                        <Edit />
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <TablePagination
                    rowsPerPageOptions={[5, 10, 25]}
                    component="div"
                    count={vehicles.length}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onPageChange={handleChangePage}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                />
            </TableContainer>
            <VehicleModal
                open={openModal}
                handleClose={handleCloseModal}
                vehicleData={vehicleData}
                setVehicleData={setVehicleData}
                handleSubmit={handleSubmit}
            />
        </div>
    );
};

export default VehiclesTableCard;
