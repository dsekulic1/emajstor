import React, { useEffect, useState } from 'react'
import { makeStyles } from '@material-ui/core/styles'
import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableContainer from '@material-ui/core/TableContainer'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import Select from '@mui/material/Select'
import Paper from '@material-ui/core/Paper'
import SearchBar from 'material-ui-search-bar'
import SettingsAccessibilityIcon from '@mui/icons-material/SettingsAccessibility'
import TextField from '@mui/material/TextField'
import Button from '@material-ui/core/Button'
import MenuItem from '@mui/material/MenuItem'
import Box from '@material-ui/core/Box'
import Grid from '@material-ui/core/Grid'
import Modal from '@material-ui/core/Modal'
import CssBaseline from '@mui/material/CssBaseline'
import InputLabel from '@material-ui/core/InputLabel'
import Container from '@mui/material/Container'
import { getAllUsers } from 'api/user/admin'
import { updateUser } from 'api/user/admin'

const useStyles = makeStyles({
  table: {
    minWidth: 650,
  },
})

export default function Users() {
  const [temprow, setTemprow] = useState()
  const [rows, setRows] = useState([])
  const [users, setUsers] = useState([])
  const [searched, setSearched] = useState('')
  const classes = useStyles()
  const [permission, setPermission] = useState('')

  const [open, setOpen] = useState(false)

  const getRole = (role) => {
    if (role === 'ROLE_USER') return 'User'
    else if (role === 'ROLE_WORKER') return 'Worker'
    return 'Admin'
  }

  const handleOpen = (row) => {
    setTemprow(row)
    setPermission(row.role)
    setOpen(true)
  }

  const handleClose = () => setOpen(false)

  useEffect(() => {
    async function fetchData() {
      try {
        const response = await getAllUsers()
        setUsers(response)
        setRows(response)
      } catch (e) {
        console.error(e)
      }
    }
    fetchData()
  }, [])

  const requestSearch = (searchedVal) => {
    const filteredRows = users.filter((row) => {
      return row.username.toLowerCase().includes(searchedVal.toLowerCase())
    })
    setRows(filteredRows)
  }

  const cancelSearch = () => {
    setSearched('')
    requestSearch(searched)
  }

  let inputStyle = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    borderRadius: '2px solid white',
  }

  const handlePermissionChange = (event) => {
    setPermission(event.target.value)
  }

  const onFinish = async (values, temprow) => {
    try {
      temprow.role = values

      await updateUser(temprow)
      handleClose()
    } catch (error) {
      handleClose()
      console.log(error)
    }
  }

  const handleSubmit = (event) => {
    event.preventDefault()

    onFinish(permission, temprow)
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
                <TableCell>Username</TableCell>
                <TableCell align='right'>Name</TableCell>
                <TableCell align='right'>Role</TableCell>
                <TableCell align='right'>Email</TableCell>
                <TableCell align='right'>Details</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {rows.map((row) => (
                <TableRow key={row.id}>
                  <TableCell component='th' scope='row'>
                    {row.username}
                  </TableCell>
                  <TableCell align='right'>
                    {row?.contactInfo.firstName +
                      ' ' +
                      row?.contactInfo.lastName}
                  </TableCell>
                  <TableCell align='right'>{getRole(row?.role)}</TableCell>
                  <TableCell align='right'>{row?.contactInfo.email}</TableCell>
                  <TableCell align='right'>
                    <SettingsAccessibilityIcon
                      onClick={() => handleOpen(row)}
                    ></SettingsAccessibilityIcon>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
      <div>
        <Modal
          style={{
            marginTop: '150px',
          }}
          open={open}
          onClose={handleClose}
          aria-labelledby='modal-modal-title'
          aria-describedby='modal-modal-description'
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
                  onSubmit={handleSubmit}
                  component='form'
                  noValidate
                  sx={{ mt: 3 }}
                >
                  <Grid container spacing={2} marginBottom='10px'>
                    <Grid item xs={12} sm={6}>
                      <TextField
                        disabled={true}
                        name='firstName'
                        required
                        fullWidth
                        id='firstName'
                        label='First Name'
                        value={temprow?.contactInfo.firstName}
                        autoFocus
                      />
                    </Grid>
                    <Grid item xs={12} sm={6}>
                      <TextField
                        disabled={true}
                        required
                        fullWidth
                        id='lastName'
                        label='Last Name'
                        name='lastName'
                        value={temprow?.contactInfo.lastName}
                        autoFocus
                      />
                    </Grid>
                    <Grid item xs={12}>
                      <TextField
                        disabled={true}
                        required
                        fullWidth
                        id='email'
                        label='Email Address'
                        value={temprow?.contactInfo.email}
                        name='email'
                      />
                    </Grid>
                    <Grid item xs={12}>
                      <TextField
                        disabled={true}
                        required
                        fullWidth
                        id='number'
                        label='Phone Number'
                        name='number'
                        type='tel'
                        value={temprow?.contactInfo.number}
                      />
                    </Grid>
                    <Grid item xs={12}>
                      <TextField
                        disabled={true}
                        required
                        fullWidth
                        name='username'
                        label='Username'
                        type='username'
                        id='username'
                        value={temprow?.username}
                      />
                    </Grid>
                    <Grid item xs={12}>
                      <InputLabel id='demo-simple-select-helper-label'>
                        Role
                      </InputLabel>
                      <Select
                        labelId='demo-simple-select-helper-label'
                        id='demo-simple-select-helper'
                        name='dropdown'
                        fullWidth
                        value={permission}
                        label='role'
                        onChange={handlePermissionChange}
                      >
                        <MenuItem value=''></MenuItem>
                        <MenuItem value={'ROLE_ADMIN'}>Admin</MenuItem>
                        <MenuItem value={'ROLE_WORKER'}>Worker</MenuItem>
                        <MenuItem value={'ROLE_USER'}>User</MenuItem>
                      </Select>
                    </Grid>
                    <Grid item xs={12}>
                      <TextField
                        disabled={true}
                        required
                        fullWidth
                        name='city'
                        label='City'
                        id='city'
                        value={temprow?.city}
                      />
                    </Grid>
                  </Grid>
                  <Button
                    type='submit'
                    variant='contained'
                    style={{
                      marginTop: '5%',
                      backgroundColor: '#3949AB',
                      marginLeft: '30%',
                      width: '40%',
                      color: '#ffff',
                      borderRadius: '10',
                    }}
                    sx={{ mt: 3, mb: 2 }}
                  >
                    Save Changes
                  </Button>
                  <Button
                    onClick={handleClose}
                    variant='contained'
                    style={{
                      marginTop: '2%',
                      backgroundColor: '#3949AB',
                      marginLeft: '30%',
                      width: '40%',
                      color: '#ffff',
                      borderRadius: '10',
                    }}
                    sx={{ mt: 3, mb: 2 }}
                  >
                    Close
                  </Button>
                </Box>
              </Box>
            </Container>
          </div>
        </Modal>
      </div>
    </>
  )
}
