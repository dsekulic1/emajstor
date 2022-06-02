import React from 'react'
import ListItem from '@mui/material/ListItem'
import Button from '@material-ui/core/Button'
import Typography from '@mui/material/Typography'
import { Box } from '@material-ui/core'
import { resolveDeal } from 'api/job/job'

export default function JobCard({ deal, filter }) {
  const handleResolve = async (e) => {
    e.preventDefault()
    const response = await resolveDeal(deal.id)
    if (response) {
      filter(deal.id)
    }
  }
  return (
    <ListItem className='workerDeal' key={deal.id} alignItems='flex-start'>
      <Box style={{ padding: '10px' }}>
        <Typography component='legend'>
          <strong>Business: </strong>
          {deal.job.business.name}
        </Typography>

        <Box style={{ marginTop: '10px' }}>
          <Typography>
            <strong>Contant info about user:</strong>
          </Typography>
          <Typography>{deal.name} </Typography>
          <Typography>{deal.email}</Typography>
          <Typography>{deal.number}</Typography>
        </Box>
        <Button
          onClick={handleResolve}
          variant='contained'
          style={{
            marginTop: '10px',
            backgroundColor: 'green',
            width: '100%',
            color: '#ffff',
            borderRadius: '10',
          }}
          sx={{ mt: 3, mb: 2 }}
        >
          Mark finished
        </Button>
      </Box>
    </ListItem>
  )
}
