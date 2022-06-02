import React, { useEffect, useState } from 'react'
import { DataGrid } from '@mui/x-data-grid'
import { getAllJobs, getAllUserFoto } from 'api/job/job'
import { getAllGallery } from 'api/job/job'
import Paper from '@material-ui/core/Paper'
import SearchBar from 'material-ui-search-bar'
import Button from '@material-ui/core/Button'
import Box from '@material-ui/core/Box'
import Grid from '@material-ui/core/Grid'
import Modal from '@material-ui/core/Modal'
import Container from '@mui/material/Container'
import ButtonGroup from '@mui/material/ButtonGroup'
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew'
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos'
import { createTheme, ThemeProvider } from '@mui/material/styles'
import PhotoCamera from '@mui/icons-material/PhotoCamera'
import CssBaseline from '@mui/material/CssBaseline'
import Avatar from '@mui/material/Avatar'
import Badge from '@mui/material/Badge'
import Alert from '@mui/material/Alert'
import { makeStyles } from '@material-ui/core/styles'
import { getUser } from '../../utilities/localStorage'
import { addGallery, addImages, getOrAddBusiness, addJob } from 'api/job/job'
import { message } from 'antd'
import ReviewCard from './ReviewCard'
import List from '@mui/material/List'
import Divider from '@mui/material/Divider'
import { getAllReviews } from 'api/review/review'
import { getAllDeals } from 'api/job/job'
import JobCard from './JobCard'
import Stack from '@mui/material/Stack'
import AddCircleIcon from '@mui/icons-material/AddCircle'
import Chart from './Chart'
import TextField from '@mui/material/TextField'
import Tab from '@mui/material/Tab';
import TabContext from '@mui/lab/TabContext';
import TabList from '@mui/lab/TabList';
import TabPanel from '@mui/lab/TabPanel';
import { addUserImages } from 'api/job/job'
import { addUserFoto, getUserFoto } from 'api/job/job'

const theme = createTheme()

function getBusinessName(params) {
  return `${params.row.business.name || ''}`
}

function getPriceType(params) {
  if (params.row.priceType === 'PER_HOUR') return 'Per hour'
  else if (params.row.priceType === 'PER_DAY') return 'Per day'
  return `${params.row.business.name || ''}`
}

const currencies = [
  {
    value: 'PER_DAY',
    label: 'Per day',
  },
  {
    value: 'PER_HOUR',
    label: 'Per hour',
  },
]

const useStyles = makeStyles((theme) => ({
  root: {
    height: '100vh',
    paddingBottom: '20px',
  },
  grid: {
    height: '100vh',
    marginTop: '10px',
  },
  paperLeft: {
    height: '90vh',
  },
  paperTop: {
    height: '20%',
  },
  paperMain: {
    height: '55vh',
  },
  paperRight: {},
  paperBottom: { height: '20%' },
  paper: {
    textAlign: 'center',
    color: theme.palette.text.primary,
    background: theme.palette.grey,
  },
}))

export default function WorkerPage() {
  const columns = [
    {
      field: 'name',
      headerName: 'Bussines',
      width: 120,
      sortable: true,
      valueGetter: getBusinessName,
    },
    { field: 'price', width: 100, headerName: 'Price', sortable: true },
    {
      field: 'priceType',
      headerName: 'Price Type',
      width: 150,

      sortable: true,
      valueGetter: getPriceType,
    },
    { field: 'userName', headerName: 'Worker', width: 150, sortable: true },
    {
      field: 'gallery',
      headerName: 'Gallery',
      renderCell: (params) => (
        <Button
          spacing={1}
          variant='contained'
          onClick={() => handleOpen(params.row)}
        >
          View gallery
        </Button>
      ),
      sortable: false,
      width: 200,
      valueGetter: (params) =>
        `${params.row.price || ''} ${params.row.userName || ''}`,
    },
    {
      field: 'image',
      headerName: 'Upload Image',
      renderCell: (params) => (
        <input
          id={params.row.id}
          style={{ padding: '2px' }}
          type='file'
          className='form-control'
          name='file'
          onChange={(event) => onFileChangeHandler(event, params.row)}
        />
      ),

      sortable: false,
      width: 250,
    },
  ]
  const [rows, setRows] = useState([])
  const [jobs, setJobs] = useState([])
  const [searched, setSearched] = useState('')
  const [open, setOpen] = useState(false)
  const [openAddJob, setOpenAddJob] = useState(false)
  const [images, setImages] = useState([])
  const [currentImg, setCurrentImg] = useState()
  const [position, setPosition] = useState(0)
  const [reviews, setReviews] = useState([])
  const [deals, setDeals] = useState([])
  const classes = useStyles()
  const [selectedPriceType, setSelectedPriceType] = useState('PER_HOUR')
  const [price, setPrice] = useState()
  const [business, setBusiness] = useState()
  const [imgs, setImgs] = useState([])
  const [value, setValue] = useState('1');
  const [file, setFile] = useState()
  const user = getUser()
  const [userProfile, setProfile] = useState(user)

  const handleChange = (event: React.SyntheticEvent, newValue: string) => {
    setValue(newValue);
  };

  const handleClickAddJob = async (e) => {
    e.preventDefault()
    const response = await getBusiness(business)
    const values = {
      user: user.id,
      userName: user.username,
      price: price,
      priceType: selectedPriceType,
      business: response,
    }
    await addJob(values)
    const resp = await getAllJobs()
    const data = resp.filter((row) => row.user === user.id)
    setJobs(data)
    setRows(data)
    handleCloseAddNewJob()
  }

  const handleCloseAddNewJob = () => {
    setOpenAddJob(false)
  }

  const handleOpenAddNewJob = () => {
    setOpenAddJob(true)
  }

  const getBusiness = async (name) => {
    const response = await getOrAddBusiness(name)
    return response
  }

  const handleChangePrice = (event) => {
    event.preventDefault()

    setPrice(event.target.value)
  }

  const handleChangeBusiness = (event) => {
    event.preventDefault()

    setBusiness(event.target.value)
  }

  const handleChangePriceType = (event) => {
    event.preventDefault()
    setSelectedPriceType(event.target.value)
  }

  const filter = (id) => {
    const newDeals = deals.filter((deal) => deal.id !== id)
    setDeals(newDeals)
  }

  const onFileChangeHandler = async (e, row) => {
    e.preventDefault()
    const formData = new FormData()

    formData.append('file', e.target.files[0])
    const response = await addImages(formData)
    const gallery = {
      fileEntity: response,
      jobId: row.id,
    }
    await addGallery(gallery)
    message.success('Successfully added image!')
    const firstNameInput = document.getElementById(row.id)
    firstNameInput.value = ''
  }

  const handlePrevious = () => {
    let value = position - 1
    if (value < 0) {
      value = images.length - 1
    }
    setCurrentImg(images[value]?.fileEntity?.data)
    setPosition(value)
  }

  const onFileChangeHandlerProfile = async (e) => {
    e.preventDefault()
    const formData = new FormData()
    const user = getUser()
    formData.append('file', e.target.files[0])
    const response = await addUserImages(formData)
    const userfoto = {
      fileEntity: response,
      userId: user.id,
    }
    const resp = await addUserFoto(userfoto)
    setFile(resp?.fileEntity?.data)
  }

  const handleNext = () => {
    let value = position + 1

    if (value > images.length - 1) {
      value = 0
    }

    setCurrentImg(images[value]?.fileEntity?.data)
    setPosition(value)
  }

  async function fetchData(jobId) {
    try {
      const response = await getAllGallery()
      const imgs = response.filter((data) => data.jobId === jobId)
      setImages(imgs)
      setCurrentImg(imgs[position]?.fileEntity?.data)
      setOpen(true)
    } catch (e) {
      console.error(e)
    }
  }

  const handleOpen = (row) => {
    fetchData(row.id)
  }
  const handleClose = () => setOpen(false)

  useEffect(() => {
    async function fetchData() {
      try {
        const response = await getAllJobs()
        const data = response.filter((row) => row.user === user.id)
        setJobs(data)
        setRows(data)
        const res = await getAllReviews()
        const arr = res.filter((row) => row.worker === user.id)
        setReviews(arr)
        const resArr = await getAllDeals()
        const newRess = resArr.filter(
          (row) => row.job.user === user.id && row.finished !== true
        )
        setDeals(newRess)
        const imgsResp = await getAllUserFoto()
        console.log(imgsResp)
        setImgs(imgsResp)
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
    <div className={classes.root} style={{ paddingBottom: '00px' }}>
      <Grid container spacing={1} className={classes.grid}>
        <Grid item container xs={12} spacing={1}>
          <Grid item xs={2}>
          <TabContext value={value}>
        <Box sx={{ borderBottom: 1, borderColor: 'divider'}}>
          <TabList onChange={handleChange} aria-label="lab API tabs example">
            <Tab sx={{ width: '50%'}} label="Reviews" value="1" />
            <Tab sx={{ width: '50%'}} label="Profile" value="2" />
          </TabList>
        </Box>
        <TabPanel value="1">
            <Paper  className={`${classes.paperLeft} ${classes.paper}`}>
              <h2 style={{ marginBottom: '10px' }}>Reviews</h2>
              <List
                sx={{
                  padding: '6px',
                  width: '100%',
                  maxWidth: 360,
                  bgcolor: 'background.paper',
                  position: 'relative',
                  overflow: 'auto',
                  maxHeight: '95vh',
                  '& ul': { padding: 0 },
                }}
                subheader={<li />}
              >
                {reviews.map((review) => (
                  <>
                    <Divider variant='inset' component='li' />
                    <ReviewCard
                      key={review.id}
                      value={review.numStars}
                      text={review.comment}
                    />
                    <Divider variant='inset' component='li' />
                  </>
                ))}
              </List>
            </Paper>
        </TabPanel>
        <TabPanel value="2">
        <Paper className={`${classes.paperLeft} ${classes.paper}`}>
              <h1>User profile</h1>
              <ThemeProvider theme={theme}>
                <Container component='main' maxWidth='xs'>
                  <CssBaseline />
                  <Box
                    sx={{
                      marginTop: 8,
                      display: 'flex',
                      flexDirection: 'column',
                      alignItems: 'center',
                    }}
                  >
                    <Stack direction='row' spacing={2}>
                      <Badge
                        overlap='circular'
                        anchorOrigin={{
                          vertical: 'bottom',
                          horizontal: 'right',
                        }}
                        badgeContent={
                          <Button
                            variant='contained'
                            component='label'
                            backgroundColor='#21b6ae'
                            style={{
                              maxWidth: '50px',
                              maxHeight: '50px',
                              minWidth: '40px',
                              minHeight: '40px',
                            }}
                          >
                            <PhotoCamera />
                            {/* <input
              type="file"
              hidden
              onChange={handleChange}
            /> */}
                            <input
                              type='file'
                              hidden
                              className='form-control'
                              name='file'
                              onChange={(event) => onFileChangeHandlerProfile(event)}
                            />
                          </Button>
                        }
                      >
                        <Avatar
                          style={{ border: '1px solid' }}
                          alt='Travis Howard'
                          src={file && `data:image/jpeg;base64,${file}`}
                          sx={{ width: 200, height: 200 }}
                        />
                      </Badge>
                    </Stack>
                    <Box
                      component='form'
                      noValidate
                      //onSubmit={handleSubmit}
                      sx={{ mt: 3 }}
                    >
                      <Grid container spacing={2}>
                        <Grid item xs={12}>
                          <div
                            className='App'
                            style={{
                              alignItems: 'center',
                            }}
                          ></div>
                        </Grid>
                        <Grid item xs={12} sm={6}>
                          <TextField
                            value={user.firstName}
                            fullWidth
                            id='firstName'
                            label='First Name'
                            name='firstName'
                            autoComplete
                          />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                          <TextField
                            value={user.lastName}
                            fullWidth
                            id='lastName'
                            label='Last Name'
                            name='lastName'
                            autoComplete
                          />
                        </Grid>
                        <Grid item xs={12}>
                          <TextField
                            value={userProfile.email}
                            fullWidth
                            id='email'
                            label='Email Address'
                            name='email'
                          />
                        </Grid>
                        <Grid item xs={12}>
                          <TextField
                            value={userProfile.number}
                            fullWidth
                            id='number'
                            label='Phone Number'
                            name='number'
                            type='tel'
                          />
                        </Grid>
                        <Grid item xs={12}>
                          <TextField
                            value={userProfile.username}
                            fullWidth
                            name='username'
                            label='Username'
                            type='username'
                            id='username'
                          />
                        </Grid>
                        <Grid item xs={12}>
                          <TextField
                            value={userProfile.city}
                            fullWidth
                            name='city'
                            label='City'
                            id='city'
                          />
                        </Grid>
                      </Grid>
                    </Box>
                  </Box>
                </Container>
              </ThemeProvider>
            </Paper>
        </TabPanel>
      </TabContext>
          </Grid>
          <Grid item xs={8}>
            <Paper className={`${classes.paperMain} ${classes.paper}`}>
              <Stack
                direction='row'
                spacing={2}
                style={{ marginBottom: '10px' }}
              >
                <SearchBar
                  style={{ width: '80%' }}
                  value={searched}
                  onChange={(searchVal) => requestSearch(searchVal)}
                  onCancelSearch={() => cancelSearch()}
                />
                <Button
                  onClick={handleOpenAddNewJob}
                  variant='outlined'
                  style={{
                    width: '20%',
                  }}
                  startIcon={<AddCircleIcon />}
                >
                  Add new job
                </Button>
              </Stack>

              <div
                style={{
                  margin: '0 auto',
                  height: '90%',
                  width: '100%',
                  fontSize: '20px',
                  paddingBottom: '18px',
                }}
              >
                <DataGrid
                  rows={rows}
                  columns={columns}
                  pageSize={5}
                  rowsPerPageOptions={[5]}
                  disableSelectionOnClick
                />
                <Chart reviews={reviews} />
              </div>
              <Modal
                style={{
                  marginTop: '40px',
                }}
                open={open}
                onClose={handleClose}
                aria-labelledby='modal-modal-title'
                aria-describedby='modal-modal-description'
                disableEscapeKeyDown
                disableEnforceFocus
              >
                <Container component='main' maxWidth='lg'>
                  <Box
                    sx={{
                      borderRadius: '5px',
                      backgroundColor: '#FFFAFA',

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
                          <Alert severity='error'>
                            There is no images for this job!
                          </Alert>
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
                      {images.length !== 0 && (
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
                      {images.length !== 0 && (
                        <ArrowForwardIosIcon onClick={handleNext} />
                      )}
                    </ButtonGroup>
                  </Box>
                </Container>
              </Modal>

              <Modal
              className ='workerNewJob'
                style={{
                  marginTop: '60px',
                }}
                open={openAddJob}
                onClose={handleCloseAddNewJob}
                aria-labelledby='modal-modal-title'
                aria-describedby='modal-modal-description'
                disableEscapeKeyDown
                disableEnforceFocus
              >
                <Container component='main' maxWidth='sm'>
                  <Box
                    component='form'
                    sx={{
                      borderRadius: '5px',
                      backgroundColor: '#FFFAFA',
                      display: 'flex',
                      flexDirection: 'column',
                      alignItems: 'center',
                      padding: '20px',
                    }}
                    noValidate
                    autoComplete='off'
                  >
                    <div style={{ margin: '10px' }}>
                      <TextField
                        id='workerBusinessname'
                        input
                        label='Input business name'
                        value={business}
                        onChange={handleChangeBusiness}
                      ></TextField>
                    </div>
                    <div style={{ margin: '10px' }}>
                      <TextField
                        id='workerPrice'
                        input
                        label='Input price'
                        value={price}
                        onChange={handleChangePrice}
                      ></TextField>
                    </div>
                    <div style={{ margin: '10px' }}>
                      <TextField
                        id='workerPricetype'
                        select
                        label='Select price type'
                        value={selectedPriceType}
                        onChange={handleChangePriceType}
                        SelectProps={{
                          native: true,
                        }}
                        helperText='Please select your type'
                        variant='filled'
                      >
                        {currencies.map((option) => (
                          <option key={option.value} value={option.value}>
                            {option.label}
                          </option>
                        ))}
                      </TextField>
                    </div>
                    <ButtonGroup
                      style={{
                        width: '50%',
                        displey: 'flex',
                        justifyContent: 'space-between',
                      }}
                    >
                      <Button
                        onClick={handleCloseAddNewJob}
                        variant='contained'
                        style={{
                          backgroundColor: 'red',
                          width: '48%',
                          color: '#ffff',
                          borderRadius: '10',
                        }}
                        sx={{ mt: 3, mb: 2 }}
                      >
                        Close
                      </Button>

                      <Button
                        onClick={(event) => handleClickAddJob(event)}
                        variant='contained'
                        style={{
                          backgroundColor: 'green',
                          width: '48%',
                          color: '#ffff',
                          borderRadius: '10',
                        }}
                        sx={{ mt: 3, mb: 2 }}
                      >
                        Submit
                      </Button>
                    </ButtonGroup>
                  </Box>
                </Container>
              </Modal>
            </Paper>
          </Grid>
          <Grid item xs={2}>
            <Paper className={`${classes.paperLeft} ${classes.paper}`}>
              <h3 style={{ marginBottom: '10px' }}>Jobs</h3>

              <List
                sx={{
                  padding: '2px',
                  width: '100%',
                  maxWidth: 360,
                  bgcolor: 'background.paper',
                  position: 'relative',
                  overflow: 'auto',
                  maxHeight: '95vh',
                  '& ul': { padding: 0 },
                }}
                subheader={<li />}
              >
                {deals.map((deal) => (
                  <>
                    <Divider variant='inset' component='li' />
                    <JobCard key={deal.id} deal={deal} filter={filter} />
                    <Divider variant='inset' component='li' />
                  </>
                ))}
              </List>
            </Paper>
          </Grid>
        </Grid>
      </Grid>
    </div>
  )
}
