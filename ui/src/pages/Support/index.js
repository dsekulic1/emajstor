import React, { useEffect, useState } from 'react'
import ChatBox from './ChatBox'
import Paper from '@mui/material/Paper'
import Grid from '@mui/material/Grid'
import { getAllChatMessages } from 'api/communication/communication'

export default function ChatBoxes() {
  const [chatMessages, setChatMessages] = useState([])
  const [update, setUpdate] = useState()

  const updated = () => {
    setUpdate(true)
  }

  useEffect(() => {
    async function fetchData() {
      try {
        const response = await getAllChatMessages()
        setChatMessages(response)
      } catch (e) {
        console.error(e)
      }
    }
    fetchData()
  }, [])

  useEffect(() => {
    async function fetchData() {
      try {
        const response = await getAllChatMessages()
        setChatMessages(response)
      } catch (e) {
        console.error(e)
      }
    }
    fetchData()
  }, [update])

  return (
    <Grid
      style={{ marginTop: '35px' }}
      container
      spacing={{ xs: 2, md: 4 }}
      columns={{ xs: 4, sm: 8, md: 12 }}
    >
      {chatMessages.map((item, index) => (
        <Grid item xs={4} sm={4} md={4} key={index}>
          <Paper>
            <ChatBox
              id={item.id}
              senderUsername={item.senderUsername}
              senderLocation={item.senderLocation}
              text={item.text}
              response={item.response}
              updated={updated}
            />
          </Paper>
        </Grid>
      ))}
    </Grid>
  )
}
