import React from 'react'
import ListItem from '@mui/material/ListItem'
import Button from '@material-ui/core/Button'
import Typography from '@mui/material/Typography'
import { Box } from '@material-ui/core'

export default function JobCard({ deal }) {
  const handleResolve = () => {}
  return (
    <ListItem alignItems='flex-start'>
      <Box style={{ padding: '10px' }}>
        <Typography component='legend'>
          <strong>Usluga: </strong>
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
          Mark resolved
        </Button>
      </Box>
    </ListItem>
  )
}
