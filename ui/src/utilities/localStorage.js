const sessionItem = 'emajstor-session'
const usernameItem = 'emajstor-login-username'
const passwordItem = 'emajstor-login-password'

// return token from local storage
export const getToken = () => {
  const session = localStorage.getItem(sessionItem)
  return session ? JSON.parse(session).jwt : null
}

// return user from local storage
export const getUser = () => {
  const session = localStorage.getItem(sessionItem)
  if (session) {
    delete session.jwt
    return JSON.parse(session)
  } else {
    return null
  }
}

// set user to local storage
export const setUser = (user) => {
  const session = localStorage.getItem(sessionItem)
  localStorage.setItem(
    sessionItem,
    JSON.stringify({
      ...JSON.parse(session),
      ...user,
    })
  )
}

// set token and user to local storage
export const setSession = (session) => {
  localStorage.setItem(sessionItem, JSON.stringify(session))
}

// remove token and user from local storage
export const removeSession = () => {
  localStorage.removeItem(sessionItem)
}

// remember email & password info with local storage
export const setRememberInfo = (username, password) => {
  localStorage.setItem(usernameItem, username)
  localStorage.setItem(passwordItem, password)
}

// get email & password info from local storage
export const getRememberInfo = () => {
  let username = localStorage.getItem(usernameItem)
  let password = localStorage.getItem(passwordItem)
  if (username && password) {
    return { username, password }
  } else {
    return null
  }
}

// remove email & password info from local storage
export const removeRememberInfo = () => {
  localStorage.removeItem(usernameItem)
  localStorage.removeItem(passwordItem)
}
