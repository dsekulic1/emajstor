import React, { useEffect, useState } from 'react'
import Card from '@mui/material/Card'
import Box from '@mui/material/Box'
import Stack from '@mui/material/Stack'
import Typography from '@mui/material/Typography'
import { LocationOn } from '@mui/icons-material'
import { Chip } from '@mui/material'
import { Switch } from '@mui/material'
import { Divider } from '@material-ui/core'

export default function ChatBox({ senderUsername, senderLocation, text }) {
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
        <Chip>Active account</Chip>
        <Switch />
      </Stack>
    </Card>
  )
}
