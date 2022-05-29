import React from 'react'
import ListItem from '@mui/material/ListItem'
import Button from '@material-ui/core/Button'
import Typography from '@mui/material/Typography'
import { Box } from '@material-ui/core'
import { resolveDeal } from 'api/job/job'

export default function DealCard({ deal, filter }) {
  const handleResolve = async (e) => {
    e.preventDefault()
    const response = await resolveDeal(deal.id)
    if (response) {
      filter(deal.id)
    }
  }
  return (
    <ListItem key={deal.id} alignItems='center'>
      <Box style={{ padding: '10px 30px' }}>
        <Typography component='legend'>
          <strong>Usluga: </strong>
          {deal.job.business.name}
        </Typography>
        <Typography component='legend'>
          <strong>Craftman: </strong>
          {deal.job.userName}
        </Typography>
        <Box style={{ marginTop: '10px' }}>
          <Typography>
            <strong>Job price:</strong>
          </Typography>
          <Typography>
            {' '}
            {deal.job?.priceType === 'PER_HOUR'
              ? deal.job.price + ' KM ' + 'per hour'
              : deal.job.price + ' KM ' + 'per day'}
          </Typography>
        </Box>
        <Button
          onClick={handleResolve}
          variant='contained'
          style={{
            marginTop: '10px',
            backgroundColor: 'red',
            width: '100%',
            color: '#ffff',
            borderRadius: '10',
          }}
          sx={{ mt: 3, mb: 2 }}
        >
          Cancel job
        </Button>
      </Box>
    </ListItem>
  )
}
