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
  const classes = useStyles()

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
                <TableCell align='right'>Gallery</TableCell>
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
                  <TableCell align='right'>
                    <a href={row?.gallery?.imageUrl}>
                      {row?.gallery?.imageUrl}
                    </a>
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
