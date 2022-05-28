import React, { useState, useEffect } from 'react'
import Box from '@material-ui/core/Box'
import { getAllGallery } from 'api/job/job'
import Container from '@mui/material/Container'
import Modal from '@material-ui/core/Modal'
import Button from '@material-ui/core/Button'
import Grid from '@material-ui/core/Grid'
import ButtonGroup from '@mui/material/ButtonGroup'
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew'
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos'
import Alert from '@mui/material/Alert'

export default function Gallery({ jobId, open, handleClose }) {
  const [images, setImages] = useState([])
  const [currentImg, setCurrentImg] = useState()
  const [position, setPosition] = useState(0)

  const handlePrevious = () => {
    let value = position - 1
    if (value < 0) {
      value = images.length - 1
    }
    setCurrentImg(images[value]?.fileEntity?.data)
    setPosition(value)
  }

  const handleNext = () => {
    let value = position + 1

    if (value > images.length - 1) {
      value = 0
    }

    setCurrentImg(images[value]?.fileEntity?.data)
    setPosition(value)
  }

  useEffect(() => {
    async function fetchData() {
      try {
        const response = await getAllGallery()
        const imgs = response.filter((data) => data.jobId === jobId)
        setImages(imgs)

        setCurrentImg(imgs[position]?.fileEntity?.data)
      } catch (e) {
        console.error(e)
      }
    }
    fetchData()
  }, [])

  return (
    <Modal
      style={{
        marginTop: '50px',
      }}
      open={open}
      onClose={handleClose}
      aria-labelledby='modal-modal-title'
      aria-describedby='modal-modal-description'
      disableEscapeKeyDown
      disableEnforceFocus
    >
      <Container component='main' maxWidth='md'>
        <Box
          sx={{
            borderRadius: '5px',
            backgroundColor: '#FFFAFA',
            opacity: 0.9,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            padding: '25px',
          }}
        >
          <Grid item xs={12}>
            <div
              style={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
              }}
            >
              {images.length === 0 ? (
                <Alert severity='error'>There is no images for this job!</Alert>
              ) : (
                <Box
                  component='img'
                  style={{
                    maxWidth: '90%',
                    height: '600px',
                    borderWidth: 1,
                    borderColor: 'red',
                  }}
                  src={`data:image/jpeg;base64,${currentImg}`}
                />
              )}
            </div>
          </Grid>

          <ButtonGroup
            style={{
              width: '90%',
              displey: 'flex',
              justifyContent: 'space-between',
            }}
          >
            {images.length === 0 && (
              <ArrowBackIosNewIcon onClick={handlePrevious} />
            )}
            <Button
              onClick={handleClose}
              variant='contained'
              style={{
                backgroundColor: 'red',
                width: '20%',
                color: '#ffff',
                borderRadius: '10',
              }}
              sx={{ mt: 3, mb: 2 }}
            >
              Close
            </Button>
            {images.length === 0 && (
              <ArrowForwardIosIcon onClick={handleNext} />
            )}
          </ButtonGroup>
        </Box>
      </Container>
    </Modal>
  )
}
