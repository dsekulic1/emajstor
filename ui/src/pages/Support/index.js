import React, { useEffect, useState } from 'react'
import ChatBox from './ChatBox'
import Paper from '@mui/material/Paper'
import Grid from '@mui/material/Grid'

const messages = [
  {
    senderUsername: 'dale1921',
    senderLocation: 'Sarajevo',
    text: 'Pozdrav potrebna mi je popravka cijevi u kupatilu',
  },
  {
    senderUsername: 'dale1921',
    senderLocation: 'Sarajevo',
    text: 'Pozdrav potrebna mi je popravka cijevi u kupatilu',
  },
  {
    senderUsername: 'dale1921',
    senderLocation: 'Sarajevo',
    text: 'Pozdrav potrebna mi je popravka cijevi u kupatilu',
  },
  {
    senderUsername: 'dale1921',
    senderLocation: 'Sarajevo',
    text: 'Pozdrav potrebna mi je popravka cijevi u kupatilu',
  },
  {
    senderUsername: 'dale1921',
    senderLocation: 'Sarajevo',
    text: 'Pozdrav potrebna mi je popravka cijevi u kupatilu',
  },
]
export default function ChatBoxes() {
  return (
    <Grid
      style={{ marginTop: '35px' }}
      container
      spacing={{ xs: 2, md: 3 }}
      columns={{ xs: 4, sm: 8, md: 12 }}
    >
      {messages.map((item, index) => (
        <Grid item xs={2} sm={4} md={4} key={index}>
          <Paper>
            <ChatBox
              senderUsername={item.senderUsername}
              senderLocation={item.senderLocation}
              text={item.text}
            />
          </Paper>
        </Grid>
      ))}
    </Grid>
  )
}
