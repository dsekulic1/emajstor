import React, { useState } from 'react'
import Card from '@mui/material/Card'
import Box from '@mui/material/Box'
import Stack from '@mui/material/Stack'
import Typography from '@mui/material/Typography'
import { LocationOn } from '@mui/icons-material'
import { Divider } from '@material-ui/core'
import TextField from '@mui/material/TextField'
import Button from '@material-ui/core/Button'
import { addResponse } from 'api/communication/communication'

export default function ChatBox({
  id,
  senderUsername,
  senderLocation,
  text,
  response,
  updated,
}) {
  const [message, setMessage] = useState()

  const handleChange = (event) => {
    event.preventDefault()
    setMessage(event.target.value)
  }

  const handleSendResponse = async () => {
    await addResponse(id, message)
    updated()
  }

  return (
    <Card>
      <Box sx={{ p: 2, display: 'flex' }}>
        <Stack spacing={0.5} style={{ marginRight: '50px' }}>
          <Typography style={{ marginBottom: '2px' }} fontWeight={700}>
            User: {senderUsername}
          </Typography>
          <Divider />
          <Typography variant='body2' color='text.secondary'>
            <LocationOn style={{ fontSize: 15 }} sx={{ color: 'red' }} />
            From {senderLocation}
          </Typography>
        </Stack>
        <Stack>
          <Typography fontWeight={700} style={{ marginBottom: '5px' }}>
            Message:
          </Typography>
          <Divider />
          <Typography style={{ marginTop: '5px' }}>{text}</Typography>
        </Stack>
      </Box>
      <Divider />
      <Stack
        direction='row'
        alignItems='center'
        justifyContent='space-between'
        sx={{ px: 2, py: 1, bgcolor: 'background.default' }}
      >
        {response === null ? (
          <>
            <TextField
              style={{ width: '80%' }}
              id='outlined-select-currency'
              input
              label='Input response'
              value={message}
              onChange={handleChange}
            />
            <Button
             data-test-id="cancel-submit-btn"
              onClick={handleSendResponse}
              variant='contained'
              style={{
                backgroundColor: 'green',
                marginLeft: '5px',
                width: '40%',
                color: '#ffff',
                borderRadius: '10',
              }}
              sx={{ mt: 3, mb: 2 }}
            >
              Send response
            </Button>
          </>
        ) : (
          <TextField style={{ width: '100%' }} disabled value={response} />
        )}
      </Stack>
    </Card>
  )
}
