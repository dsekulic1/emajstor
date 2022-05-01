import React, { createContext, useContext, useState } from 'react'
import { validToken } from 'utilities/common'

export const UserContext = createContext({})

export const useUserContext = () => useContext(UserContext)

export const AppProvider = ({ children }) => {
  const [loggedIn, setLoggedIn] = useState(validToken())

  return (
    <UserContext.Provider value={{ loggedIn, setLoggedIn }}>
      {children}
    </UserContext.Provider>
  )
}
