import React, { createContext, useContext, useState } from 'react'
import { validToken, userRole } from 'utilities/common'

export const UserContext = createContext({})

export const useUserContext = () => useContext(UserContext)

export const AppProvider = ({ children }) => {
  const [loggedIn, setLoggedIn] = useState(validToken())

  const [role, setRole] = useState(userRole())

  return (
    <UserContext.Provider value={{ loggedIn, setLoggedIn, role, setRole }}>
      {children}
    </UserContext.Provider>
  )
}
