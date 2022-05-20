import { getToken, getUser } from 'utilities/localStorage'
import { decode } from 'jsonwebtoken'

export const validToken = () => {
  const token = getToken()
  if (token === null) return false
  const exp = decode(token, { complete: true }).payload.exp
  return Date.now() < exp * 1000
}

export const userRole = () => {
  const user = getUser()

  if (user != null) return user.roles[0]

  return ''
}

export const getRandom = (arr, n) => {
  let result = new Array(n),
    len = arr.length,
    taken = new Array(len)
  if (n > len) return arr
  while (n--) {
    const x = Math.floor(Math.random() * len)
    result[n] = arr[x in taken ? taken[x] : x]
    taken[x] = --len in taken ? taken[len] : len
  }
  return result
}
