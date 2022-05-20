import { useHistory } from 'react-router-dom'
import React, { useState, useEffect } from 'react'
import Avatar from '@mui/material/Avatar'
import Button from '@mui/material/Button'
import CssBaseline from '@mui/material/CssBaseline'
import TextField from '@mui/material/TextField'
import { signUp } from 'api/user/auth'
import { message } from 'antd'
import Link from '@mui/material/Link'
import Grid from '@mui/material/Grid'
import Box from '@mui/material/Box'
import LockOutlinedIcon from '@mui/icons-material/LockOutlined'
import Typography from '@mui/material/Typography'
import Container from '@mui/material/Container'
import { createTheme, ThemeProvider } from '@mui/material/styles'
import { loginUrl, homeUrl } from 'utilities/appUrls'
import { setSession } from 'utilities/localStorage'
import { useUserContext } from 'AppContext'

function Copyright(props) {
  return (
    <Typography
      variant='body2'
      color='text.secondary'
      align='center'
      {...props}
    >
      {'Copyright © '}
      <Link color='inherit' href={homeUrl}>
        eMajstor{' '}
      </Link>
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  )
}

const theme = createTheme()

export default function Register() {
  const history = useHistory()
  const { setLoggedIn, setRole } = useUserContext()
  const [lat, setLat] = useState(null)
  const [lng, setLng] = useState(null)
  const [loading, setLoading] = useState(false)
  const [status, setStatus] = useState(null)

  useEffect(() => {
    getLocation()
  }, [])

  const onFinish = async (values) => {
    try {
      setLoading(true)
      const response = await signUp(values)
      message.success('Successfully registered')
      setLoading(false)
      setSession(response)
      setRole(response.roles[0])
      history.goBack()
      setLoggedIn(true)
    } catch (error) {
      setLoading(false)
      console.log(error)
      message.warning(error.response.data.message)
    }
  }

  const handleSubmit = (event) => {
    event.preventDefault()
    const data = new FormData(event.currentTarget)
    const values = {
      username: data.get('username'),
      password: data.get('password'),
      city: data.get('city'),
      locationLongitude: !status ? lng : -9999,
      locationLatitude: !status ? lat : -9999,
      firstName: data.get('firstName'),
      lastName: data.get('lastName'),
      email: data.get('email'),
      number: data.get('number'),
    }
    onFinish(values)
  }

  const getLocation = () => {
    if (!navigator.geolocation) {
      setStatus('Geolocation is not supported by your browser')
    } else {
      setStatus('Locating...')
      navigator.geolocation.getCurrentPosition(
        (position) => {
          setStatus(null)
          setLat(position.coords.latitude)
          setLng(position.coords.longitude)
        },
        () => {
          setStatus('Unable to retrieve your location')
        }
      )
    }
  }

  return (
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
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component='h1' variant='h5'>
            Sign up
          </Typography>
          <Box
            component='form'
            noValidate
            onSubmit={handleSubmit}
            sx={{ mt: 3 }}
          >
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <TextField
                  autoComplete='given-name'
                  name='firstName'
                  required
                  fullWidth
                  id='firstName'
                  label='First Name'
                  autoFocus
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  required
                  fullWidth
                  id='lastName'
                  label='Last Name'
                  name='lastName'
                  autoComplete
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id='email'
                  label='Email Address'
                  name='email'
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id='number'
                  label='Phone Number'
                  name='number'
                  type='tel'
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name='username'
                  label='Username'
                  type='username'
                  id='username'
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name='password'
                  label='Password'
                  type='password'
                  id='password'
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name='city'
                  label='City'
                  id='city'
                />
              </Grid>
            </Grid>
            <Button
              type='submit'
              fullWidth
              variant='contained'
              loading={loading}
              sx={{ mt: 3, mb: 2 }}
            >
              Sign Up
            </Button>
            <Grid container justifyContent='center'>
              <Grid item>
                <Link href={loginUrl} variant='body2'>
                  Already have an account? Sign in
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
        <Copyright sx={{ mt: 5 }} />
      </Container>
    </ThemeProvider>
  )
}
