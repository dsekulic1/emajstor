import React from 'react'
import ListItem from '@mui/material/ListItem'
import ListItemText from '@mui/material/ListItemText'
import ListItemAvatar from '@mui/material/ListItemAvatar'
import Typography from '@mui/material/Typography'
import Rating from '@mui/material/Rating'
import Avatar from 'avataaars'
import { generateRandomAvatarOptions } from './Avatars'

export default function ReviewCard({ value, text }) {
  return (
    <ListItem alignItems='flex-start'>
      <ListItemAvatar>
        <Avatar
          style={{ width: '40px', height: '40px' }}
          avatarStyle='Circle'
          {...generateRandomAvatarOptions()}
        />
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
