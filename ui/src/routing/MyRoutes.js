import React from 'react'
import { Route, Switch } from 'react-router-dom'
import GuestRoute from 'routing/GuestRoute'

import LandingPage from 'pages/LandingPage'
import Login from 'pages/Login'
import Register from 'pages/Register'
import PageNotFound from 'pages/PageNotFound'
import PrivateRoute from './PrivateRoute'
import UserPage from 'pages/UserPage/UserPage'

const MyRoutes = () => {
  return (
    <Switch>
      <Route exact path='/' component={LandingPage} />
      <GuestRoute path='/login' component={Login} />
      <GuestRoute path='/register' component={Register} />
      <PrivateRoute path='/userpage' component={UserPage} />
      <Route component={PageNotFound} />
    </Switch>
  )
}

export default MyRoutes
