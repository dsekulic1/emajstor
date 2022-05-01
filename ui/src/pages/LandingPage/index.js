import React, { useEffect, useState } from 'react'
import { useUserContext } from 'AppContext'

const LandingPage = () => {
  const { loggedIn } = useUserContext()
  return (
    <div>
      <h1>HELLO</h1>
    </div>
  )
}

export default LandingPage
