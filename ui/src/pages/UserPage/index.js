import React, { useEffect, useState } from 'react'
import { DataGrid } from '@mui/x-data-grid'
import { getAllJobs } from 'api/job/job'
import Paper from '@material-ui/core/Paper'
import SearchBar from 'material-ui-search-bar'

function getBusinessName(params) {
  return `${params.row.business.name || ''}`
}

function getPriceType(params) {
  if (params.row.priceType === 'PER_HOUR') return 'Per hour'
  else if (params.row.priceType === 'PER_DAY') return 'Per day'
  return `${params.row.business.name || ''}`
}

const columns = [
  {
    field: 'name',
    headerName: 'Bussines',
    width: 250,
    sortable: true,
    valueGetter: getBusinessName,
  },
  { field: 'price', headerName: 'Price', width: 200, sortable: true },
  {
    field: 'priceType',
    headerName: 'Price Type',
    width: 200,
    sortable: true,
    valueGetter: getPriceType,
  },
  { field: 'userName', headerName: 'Worker', width: 200, sortable: true },
  {
    field: 'gallery',
    headerName: 'Gallery',
    renderCell: (params) => (
      <a href={params.row?.gallery?.imageUrl}>
        {params.row?.gallery?.imageUrl}
      </a>
    ),
    sortable: false,
    width: 250,
    valueGetter: (params) =>
      `${params.row.price || ''} ${params.row.userName || ''}`,
  },
]

export default function UserPage() {
  const [rows, setRows] = useState([])
  const [jobs, setJobs] = useState([])
  const [searched, setSearched] = useState('')

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
    <div style={{ margin: '0 auto', height: 600, width: '70%' }}>
      <Paper style={{ marginTop: '35px' }}>
        <SearchBar
          value={searched}
          onChange={(searchVal) => requestSearch(searchVal)}
          onCancelSearch={() => cancelSearch()}
        />
        <div
          style={{
            margin: '0 auto',
            height: 600,
            width: '100%',
            fontSize: '20px',
          }}
        >
          <DataGrid
            rows={rows}
            columns={columns}
            pageSize={5}
            rowsPerPageOptions={[15]}
            disableSelectionOnClick
          />
        </div>
      </Paper>
    </div>
  )
}
