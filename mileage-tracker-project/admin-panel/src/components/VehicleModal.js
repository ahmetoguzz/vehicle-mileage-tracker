import { Modal, Box, TextField, Button } from '@mui/material';

const VehicleModal = ({ open, handleClose, handleSubmit, vehicleData, setVehicleData }) => {
    const handleChange = (e) => {
        setVehicleData({ ...vehicleData, [e.target.name]: e.target.value });
    };

    return (
        <Modal open={open} onClose={handleClose}>
            <Box sx={style}>
                <h2>{vehicleData.id ? 'Araç Güncelle' : 'Araç Ekle'}</h2>
                <TextField
                    name="employeeId"
                    label="Çalışan ID"
                    value={vehicleData.employeeId || ''}
                    onChange={handleChange}
                    fullWidth
                    margin="normal"
                />
                <TextField
                    name="plateNumber"
                    label="Plaka"
                    value={vehicleData.plateNumber || ''}
                    onChange={handleChange}
                    fullWidth
                    margin="normal"
                />
                <TextField
                    name="brand"
                    label="Marka"
                    value={vehicleData.brand || ''}
                    onChange={handleChange}
                    fullWidth
                    margin="normal"
                />
                <TextField
                    name="model"
                    label="Model"
                    value={vehicleData.model || ''}
                    onChange={handleChange}
                    fullWidth
                    margin="normal"
                />
                <TextField
                    name="color"
                    label="Renk"
                    value={vehicleData.color || ''}
                    onChange={handleChange}
                    fullWidth
                    margin="normal"
                />
                <TextField
                    name="hgsNumber"
                    label="HGS Numarası"
                    value={vehicleData.hgsNumber || ''}
                    onChange={handleChange}
                    fullWidth
                    margin="normal"
                />
                <TextField
                    name="lastKm"
                    label="Son KM"
                    value={vehicleData.lastKm || ''}
                    onChange={handleChange}
                    fullWidth
                    margin="normal"
                />
                <TextField
                    name="createDate"
                    label="Oluşturulma Tarihi"
                    type="date"
                    value={vehicleData.createDate || ''}
                    onChange={handleChange}
                    fullWidth
                    margin="normal"
                    InputLabelProps={{ shrink: true }}
                />
                <TextField
                    name="modelYear"
                    label="Model Yılı"
                    value={vehicleData.modelYear || ''}
                    onChange={handleChange}
                    fullWidth
                    margin="normal"
                />
                <Button variant="contained" color="primary" onClick={handleSubmit} fullWidth>
                    {vehicleData.id ? 'Güncelle' : 'Araç Ekle'}
                </Button>
            </Box>
        </Modal>
    );
};


const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    boxShadow: 24,
    p: 4,
};

export default VehicleModal;
