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
import SettingsAccessibilityIcon from '@mui/icons-material/SettingsAccessibility'
import { getAllUsers } from 'api/user/admin'

const useStyles = makeStyles({
    table: {
      minWidth: 650,
    },
  })
  


export default function Users() {
  const [rows, setRows] = useState([])
  const [users, setUsers] = useState([])
  const [searched, setSearched] = useState('')
  const classes = useStyles()

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
                <TableCell align='right'>{row?.contactInfo.firstName+" " +row?.contactInfo.lastName}</TableCell>
                <TableCell align='right'>{row?.role}</TableCell>
                <TableCell align='right'>{row?.contactInfo.email}</TableCell>
                <TableCell align='right'><SettingsAccessibilityIcon></SettingsAccessibilityIcon>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Paper>
  </>
  )
}
