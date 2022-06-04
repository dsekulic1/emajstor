import React from 'react'
import ListItem from '@mui/material/ListItem'
import Typography from '@mui/material/Typography'
import { Box } from '@material-ui/core'

export default function SupportMessage({ chat }) {
  return (
    <ListItem key={chat.id} alignItems='flex-start'>
      <Box style={{ padding: '10px' }}>
        <Typography component='legend'>
          <strong>Message: </strong>
          {chat.text}
        </Typography>
        {chat?.response && (
          <Box style={{ marginTop: '10px' }}>
            <Typography component='legend'>
              <strong>Response: </strong>
              {chat.response}
            </Typography>
          </Box>
        )}
      </Box>
    </ListItem>
  )
}
