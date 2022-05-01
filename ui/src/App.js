import React from 'react'
import { BrowserRouter as Router } from 'react-router-dom'

//import Header from 'shared/Header'
import MyRoutes from 'routing/MyRoutes'
//import Footer from 'shared/Footer'
import Navbar from 'shared/Header'

import 'App.scss'

function App() {
  return (
    <div className='app-container'>
      <Router>
        <Navbar />
        <div className='main-container'>
          <MyRoutes />
        </div>
      </Router>
    </div>
  )
}

export default App
