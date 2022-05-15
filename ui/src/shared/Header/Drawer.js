import React, { useState } from 'react'
import {
  Drawer,
  IconButton,
  List,
  ListItem,
  ListItemText,
} from '@material-ui/core'
import MenuIcon from '@material-ui/icons/Menu'
import { Link } from 'react-router-dom'
import { removeSession } from 'utilities/localStorage'
import { useUserContext } from 'AppContext'

function DrawerComponent() {
  const [openDrawer, setOpenDrawer] = useState(false)
  const { loggedIn } = useUserContext()
  const { setLoggedIn } = useUserContext()
  const handleLogout = () => {
    setLoggedIn(false)
    removeSession()
  }
  return (
    <>
      <Drawer open={openDrawer} onClose={() => setOpenDrawer(false)}>
        <List>
          <ListItem onClick={() => setOpenDrawer(false)}>
            <ListItemText>
              <Link to='/'>Home</Link>
            </ListItemText>
          </ListItem>
          {loggedIn && (
            <ListItem onClick={() => setOpenDrawer(false)}>
              <ListItemText>
                <Link to='/userpage'>Job</Link>{' '}
              </ListItemText>
            </ListItem>
          )}
          <ListItem onClick={() => setOpenDrawer(false)}>
            <ListItemText>
              {!loggedIn ? (
                <Link to='/login'>Login</Link>
              ) : (
                <>
                  <Link to='/' onClick={handleLogout}>
                    Signout
                  </Link>
                </>
              )}
            </ListItemText>
          </ListItem>
        </List>
      </Drawer>
      <IconButton onClick={() => setOpenDrawer(!openDrawer)}>
        <MenuIcon />
      </IconButton>
    </>
  )
}
export default DrawerComponent
