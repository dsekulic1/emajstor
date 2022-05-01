import React from 'react'
import { Link } from 'react-router-dom'

import './pageNotFound.scss'

const PageNotFound = () => {
  return (
    <div className='not-found-container'>
      404
      <br />
      Page not found
      <p style={{ textAlign: 'center' }}>
        <Link to='/'>Go to Home </Link>
      </p>
    </div>
  )
}

export default PageNotFound
