import React from 'react'
import ListItem from '@mui/material/ListItem'
import ListItemText from '@mui/material/ListItemText'
import ListItemAvatar from '@mui/material/ListItemAvatar'
import Avatar from '@mui/material/Avatar'
import Typography from '@mui/material/Typography'
import Rating from '@mui/material/Rating'

export default function ReviewCard({ value, text }) {
  return (
    <ListItem alignItems='flex-start'>
      <ListItemAvatar>
        <Avatar alt='Travis Howard' src='/static/images/avatar/2.jpg' />
      </ListItemAvatar>
      <ListItemText
        primary={
          <React.Fragment>
            <Typography component='legend'>{text}</Typography>
            <Rating name='disabled' value={value} disabled />
          </React.Fragment>
        }
      />
    </ListItem>
  )
}
