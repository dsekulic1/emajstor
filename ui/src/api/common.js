import axios from 'axios'
import { getToken } from 'utilities/localStorage'

export const hostUrl = 'http://localhost:8086'

export const basicGet = async (url) => {
  return (await axios.get(hostUrl + url, getAuthConfig())).data
}

export const basicDelete = async (url) => {
  return (await axios.delete(hostUrl + url, getAuthConfig())).data
}

export const basicPost = async (url, data) => {
  return (await axios.post(hostUrl + url, data, getAuthConfig())).data
}

export const basicPut = async (url, data) => {
  return (await axios.put(hostUrl + url, data, getAuthConfig())).data
}

export const getAuthConfig = () => {
  let config = {}
  const token = getToken()
  if (token) {
    config = {
      headers: { Authorization: 'Bearer ' + token },
    }
  }
  return config
}
