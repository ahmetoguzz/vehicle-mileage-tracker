import React, { useState, useEffect } from 'react';
import { Paper, Typography, TextField, Button, Modal, Box } from '@mui/material';
import { styled } from '@mui/system';


const App = () => {
  const [currentKm, setCurrentKm] = useState('');
  const [userData, setUserData] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleUpdate = () => {
    const token = new URLSearchParams(window.location.search).get('id');

    fetch(`http://localhost:8080/update-km`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ token, currentKm }),
    })
      .then((response) => response.json())
      .then((data) => {
        console.log('Update successful:', data);
        if (data && data.payload) {
          setUserData(data);
          setIsModalOpen(true);
        } else {
          console.error('Unexpected response format:', data);
        }
      })
      .catch((error) => console.error('Error updating KM:', error));
  };

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('id');

    fetch(`http://localhost:8080/user-details?token=${token}`)
      .then((response) => response.json())
      .then((data) => setUserData(data))
      .catch((error) => console.error('Error fetching user data:', error));
  }, []);

  return (
    <Container>
      <StyledPaper elevation={3}>
        <Logo
          src="https://www.largeherds.co.za/wp-content/uploads/2024/01/logo-placeholder-image.png"
          alt="Company Logo"
        />
        {userData ? (
          <>
            <Typography variant="h6">
              <strong>Araç Sahibi:</strong> {userData.payload?.name} {userData.payload?.surname}
            </Typography>
            <Typography variant="h6">
              <strong>Araç Plakası:</strong> {userData.payload?.vehicle?.plateNumber}
            </Typography>
            <Typography variant="h6">
              <strong>Son KM:</strong> {userData.payload?.vehicle?.lastKm}
            </Typography>
          </>
        ) : (
          <Typography variant="h6">Yükleniyor...</Typography>
        )}
        <TextFieldStyled
          label="Güncel KM"
          type="number"
          value={currentKm}
          onChange={(e) => setCurrentKm(e.target.value)}
          placeholder="Güncel KM giriniz."
          variant="outlined"
          fullWidth
        />
        <ButtonStyled variant="contained" color="primary" onClick={handleUpdate}>
          Güncelle
        </ButtonStyled>
      </StyledPaper>
      <Modal open={isModalOpen} onClose={() => setIsModalOpen(false)}>
        <ModalContent>
          <Typography variant="h6">Başarıyla güncellendi!</Typography>
          <ModalButton variant="contained" onClick={() => setIsModalOpen(false)}>
            Tamam
          </ModalButton>
        </ModalContent>
      </Modal>
    </Container>
  );
};

const Container = styled('div')({
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
  height: '100vh',
  backgroundColor: '#f5f5f5',
});

const StyledPaper = styled(Paper)({
  padding: '32px',
  maxWidth: '600px',
  width: '100%',
  textAlign: 'center',
});

const Logo = styled('img')({
  width: '100px',
  height: 'auto',
  marginBottom: '16px',
  display: 'block',
  marginLeft: 'auto',
  marginRight: 'auto',
});

const TextFieldStyled = styled(TextField)({
  marginBottom: '20px',
  marginTop: '20px',
});

const ButtonStyled = styled(Button)({
  display: 'block',
  margin: '0 auto',
  padding: '10px 20px',
  width: '100%',
});

const ModalContent = styled(Box)({
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  backgroundColor: '#fff',
  padding: '32px',
  boxShadow: 24,
});

const ModalButton = styled(Button)({
  marginTop: '16px',
  display: 'block',
  marginLeft: 'auto',
  marginRight: 'auto',
});

export default App;
