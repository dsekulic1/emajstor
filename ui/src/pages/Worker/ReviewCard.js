import React, { useEffect, useState } from 'react'
import ListItem from '@mui/material/ListItem'
import ListItemText from '@mui/material/ListItemText'
import ListItemAvatar from '@mui/material/ListItemAvatar'
import Typography from '@mui/material/Typography'
import Rating from '@mui/material/Rating'
import Avatar from '@mui/material/Avatar'
import { getUserFoto } from 'api/job/job'

export default function ReviewCard({ value, text, userId }) {
  const [userImg, setUserImg] = useState()
  useEffect(async () => {
    try {
      const userFoto = await getUserFoto(userId)
      setUserImg(userFoto?.fileEntity?.data)
    } catch (error) {
      console.log(error)
    }
  }, [])
  return (
    <ListItem alignItems='flex-start'>
      <ListItemAvatar>
        <Avatar
          //  style={{ width: '40px', height: '40px' }}
          avatarStyle='Circle'
          src={userImg && `data:image/jpeg;base64,${userImg}`}
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
