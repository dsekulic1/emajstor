import React, { useEffect, useState } from 'react'
import { makeStyles } from '@material-ui/core/styles'
import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableContainer from '@material-ui/core/TableContainer'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import Paper from '@material-ui/core/Paper'
import SearchBar from 'material-ui-search-bar'
import TextField from '@mui/material/TextField'
import Button from "@material-ui/core/Button";
import MenuItem from '@mui/material/MenuItem';
import TextareaAutosize from "@material-ui/core/TextareaAutosize";
import Box from "@material-ui/core/Box";
import Grid from "@material-ui/core/Grid";
import Modal from '@material-ui/core/Modal';
import CssBaseline from '@mui/material/CssBaseline' 
import MuiImageSlider from 'mui-image-slider';
import Container from '@mui/material/Container'
import Alert from '@mui/material/Alert';
import AlertTitle from '@mui/material/AlertTitle';
import Rating from '@mui/material/Rating';
import StarIcon from '@mui/icons-material/Star';
import { getAllJobs } from 'api/job/job'

const useStyles = makeStyles({
  table: {
    minWidth: 650,
  },
})


export default function UserPage1() {
  const [rows, setRows] = useState([])
  const [jobs, setJobs] = useState([])
  const [searched, setSearched] = useState('')
  
  
  const images = [
      'https://i.imgur.com/I23zIsA.jpg',
      'https://i.imgur.com/fpFfwUu.jpg',
      'https://i.imgur.com/xEkG5tz.jpg',
  ];

  const labels = {
    0.5: 'Useless',
    1: 'Useless+',
    1.5: 'Poor',
    2: 'Poor+',
    2.5: 'Ok',
    3: 'Ok+',
    3.5: 'Good',
    4: 'Good+',
    4.5: 'Excellent',
    5: 'Excellent+',
  };
  
  let inputStyle = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    borderRadius: '2px solid white',
  }

  const useStyles = makeStyles({
    root: {
      width: 200,
      display: 'flex',
      alignItems: 'center',
    },
  });

  const [value, setValue] = React.useState(2);
  const [hover, setHover] = React.useState(-1);
  const clas = useStyles();
   const classes = useStyles()
  
  const [temprow, setTemprow] = useState()
  const [open, setOpen] = useState(false);
  const handleOpen = (row)=>{
    console.log(row)
    setTemprow(row)
    setOpen(true);}
   const handleClose = () => setOpen(false);



  const [openMessage, setOpenMessage] = useState(false);
  const handleOpenMessage = (row)=>{
    console.log(row)
    setTemprow(row)
    setOpenMessage(true);}
  const handleCloseMessage = () => setOpenMessage(false);
  const [text, setText] = useState("");
  const handleChange = event => {
      setText(event.target.value);
  };

  const [openReview, setOpenReview] = useState(false);
  const handleOpenReview = (row)=>{
    console.log(row)
    setTemprow(row)
    setOpenReview(true);}
  const handleCloseReview = () => setOpenReview(false);
  const [textReview, setTextReview] = useState("");
  const handleChangeReview = event => {
      setTextReview(event.target.value);
  };

  

  useEffect(() => {
    async function fetchData() {
      try {
        const response = await getAllJobs()
        setJobs(response)
        setRows(response)
      } catch (e) {
        console.error(e)
      }
    }
    fetchData()
  }, [])

  const requestSearch = (searchedVal) => {
    const filteredRows = jobs.filter((row) => {
      return row.business.name.toLowerCase().includes(searchedVal.toLowerCase())
    })
    setRows(filteredRows)
  }

  const cancelSearch = () => {
    setSearched('')
    requestSearch(searched)
  }

  return (
    <>
      <Paper style={{ marginTop: '35px' }}>
        <SearchBar
          value={searched}
          onChange={(searchVal) => requestSearch(searchVal)}
          onCancelSearch={() => cancelSearch()}
        />
        <TableContainer>
          <Table className={classes.table} aria-label='simple table'>
            <TableHead>
              <TableRow>
                <TableCell>Bussines</TableCell>
                <TableCell align='right'>Price</TableCell>
                <TableCell align='right'>Price Type</TableCell>
                <TableCell align='right'>Worker</TableCell>
                <TableCell align='right'>Message</TableCell>
                <TableCell align='right'>Gallery</TableCell>
                <TableCell align='right'>Review</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {rows.map((row) => (
                <TableRow key={row.id}>
                  <TableCell component='th' scope='row'>
                    {row.business.name}
                  </TableCell>
                  <TableCell align='right'>{row.price}</TableCell>
                  <TableCell align='right'>{row.priceType}</TableCell>
                  <TableCell align='right'>{row.userName}</TableCell>
                  <TableCell align='right'><Button spacing={1} variant="contained" onClick={()=>handleOpenMessage(row)}>Send message</Button></TableCell>
                  <TableCell align='right'><Button spacing={1} variant="contained"  onClick={()=>handleOpen(row)} >View gallery</Button></TableCell>
                  <TableCell align='right'><Button spacing={1} variant="contained" onClick={()=>handleOpenReview(row)}>Make review</Button></TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
      <div>
      </div>
      <div>
            <Modal
                open={openReview}
                onClose={handleCloseReview}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
                disableEscapeKeyDown
                disableEnforceFocus
            >
                <div style={inputStyle}>
      <Container component='main' maxWidth='md'>
        <CssBaseline />
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
          <Box
            component='form'
            noValidate
            sx={{ mt: 3 }}
          >
            <Grid container spacing={2} marginBottom='10px' >
              <Grid item xs={12}>
              <Alert severity="info">
                <AlertTitle>Info</AlertTitle>
                Type your review into the textarea — <strong>we will notify your craftsman!</strong>
                </Alert>
              </Grid>
              <div className={clas.root}
              style={{
                marginLeft: '20%',
                width: '80%',
                borderRadius: '10',
              }}>
                <h4>Rate your craftsman</h4>
      <Rating
      style={{
        marginLeft: '5%',
        borderRadius: '10',
      }}
        name="hover-feedback"
        value={value}
        precision={0.5}
        onChange={(event, newValue) => {
          setValue(newValue);
        }}
        onChangeActive={(event, newHover) => {
          setHover(newHover);
        }}
      />
      {value !== null && <Box ml={2}>{labels[hover !== -1 ? hover : value]}</Box>}
    </div>
              <Grid item xs={12}> 
              <TextField
                        fullWidth
                        multiline
                        label="Review"
                        InputProps={{
                            inputComponent: TextareaAutosize,
                            rows: 3
                        }}
                        value={textReview}
                        onChange={handleChangeReview}
                    />
              </Grid>
            </Grid>
            <Button
            onClick={handleCloseReview}
            variant='contained'
            style={{
              backgroundColor: '#3949AB',
              marginTop: '1%',
              marginLeft: '30%',
              width: '40%',
              color: '#ffff',
              borderRadius: '10',
            }}
            sx={{ mt: 3, mb: 2 }}>Close</Button>
          </Box>
        </Box>
      </Container>
    </div>
            </Modal>
            
        </div>
      <div>
            <Modal
                open={openMessage}
                onClose={handleCloseMessage}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
                disableEscapeKeyDown
                disableEnforceFocus
            >
                <div style={inputStyle}>
      <Container component='main' maxWidth='md'>
        <CssBaseline />
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
          <Box
            component='form'
            noValidate
            sx={{ mt: 3 }}
          >
            <Grid container spacing={2} marginBottom='10px' >
              <Grid item xs={12}>
              <Alert severity="info">
                <AlertTitle>Info</AlertTitle>
                Type your message into the textarea — <strong>we will notify your craftsman!</strong>
                </Alert>
              </Grid>
              <Grid item xs={12}> 
              <TextField
                        fullWidth
                        multiline
                        label="Message"
                        InputProps={{
                            inputComponent: TextareaAutosize,
                            rows: 3
                        }}
                        value={text}
                        onChange={handleChange}
                    />
              </Grid>
            </Grid>
            <Button
            onClick={handleCloseMessage}
            variant='contained'
            style={{
              backgroundColor: '#3949AB',
              marginTop: '1%',
              marginLeft: '30%',
              width: '40%',
              color: '#ffff',
              borderRadius: '10',
            }}
            sx={{ mt: 3, mb: 2 }}>Close</Button>
          </Box>
        </Box>
      </Container>
    </div>
            </Modal>
            
        </div>
        <div>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
                disableEscapeKeyDown
                disableEnforceFocus
            >
                <div style={inputStyle}>
      <Container component='main' maxWidth='md'>
        <CssBaseline />
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
          <Box
            component='form'
            noValidate
            sx={{ mt: 3 }}
          >
            <Grid container spacing={2} marginBottom='10px' >
              <Grid item xs={12}>
              <Alert severity="info">
                <AlertTitle>Info</AlertTitle>
                Below you can see the craftsman work — <strong>if you like it send him a message!</strong>
                </Alert>
              </Grid>
              <Grid item xs={12}> 
              <div style={{display: 'flex',  justifyContent:'center', alignItems:'center',}}>
                <MuiImageSlider 
                alwaysShowArrows
                arrows
                images={images}/>
                </div>
              </Grid>
            </Grid>
            <Button
            onClick={handleClose}
            variant='contained'
            style={{
              backgroundColor: '#3949AB',
              marginLeft: '30%',
              width: '40%',
              color: '#ffff',
              borderRadius: '10',
            }}
            sx={{ mt: 3, mb: 2 }}>Close</Button>
          </Box>
        </Box>
      </Container>
    </div>
            </Modal>
            
        </div>

        
    </>
  )
}
