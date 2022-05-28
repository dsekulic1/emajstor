import React from 'react'
import { Route, Switch } from 'react-router-dom'
import GuestRoute from 'routing/GuestRoute'
import Users from 'pages/Users'
import LandingPage from 'pages/LandingPage'
import Login from 'pages/Login'
import Register from 'pages/Register'
import PageNotFound from 'pages/PageNotFound'
import PrivateRoute from './PrivateRoute'
import UserPage from 'pages/UserPage'
import ChatBoxes from 'pages/Support'
import AddGallery from 'pages/Gallery/AddGallery'

const MyRoutes = () => {
  return (
    <Switch>
      <Route exact path='/' component={LandingPage} />
      <GuestRoute path='/login' component={Login} />
      <GuestRoute path='/register' component={Register} />
      <PrivateRoute path='/userpage' component={UserPage} />
      <PrivateRoute path='/users' component={Users} />
      <PrivateRoute path='/support' component={ChatBoxes} />
      <PrivateRoute path='/addgallery' component={AddGallery} />
      <Route component={PageNotFound} />
    </Switch>
  )
}

export default MyRoutes
