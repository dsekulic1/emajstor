import React from 'react'
import {
  AppBar,
  Toolbar,
  CssBaseline,
  makeStyles,
  useTheme,
  useMediaQuery,
} from '@material-ui/core'
import { Link } from 'react-router-dom'
import DrawerComponent from './Drawer'
import { ReactComponent as ReactLogo } from '../../images/logo.svg'
import Button from '@mui/material/Button'
import IconButton from '@mui/material/IconButton'

const useStyles = makeStyles((theme) => ({
  navlinks: {
    marginLeft: theme.spacing(5),
    display: 'flex',
  },
  logo: {
    flexGrow: '1',
    cursor: 'pointer',
  },
  icon: {
    width: '300px',
    height: '50px',
    marginRight: '2px',
    marginTop: '5px',
  },
  link: {
    textDecoration: 'none',
    color: 'white',
    fontSize: '20px',
    marginLeft: theme.spacing(20),
    '&:hover': {
      color: 'yellow',
      borderBottom: '1px solid white',
    },
  },
}))

function Navbar() {
  const classes = useStyles()
  const theme = useTheme()
  const isMobile = useMediaQuery(theme.breakpoints.down('md'))

  return (
    <AppBar position='static'>
      <CssBaseline />
      <Toolbar>
        <ReactLogo className={classes.icon} />

        {isMobile ? (
          <DrawerComponent />
        ) : (
          <div className={classes.navlinks}>
            <Link to='/' className={classes.link}>
              Home
            </Link>
            <Link to='/login' className={classes.link}>
              Login
            </Link>
            <Link to='/about' className={classes.link}>
              About
            </Link>
            <Link to='/contact' className={classes.link}>
              Contact
            </Link>
            <Link to='/faq' className={classes.link}>
              FAQ
            </Link>
          </div>
        )}
      </Toolbar>
    </AppBar>
  )
}
export default Navbar
