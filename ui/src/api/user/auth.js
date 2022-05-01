import { post, put } from './common'

const authUrl = '/auth'

export const signUp = async (body) => {
  return await post(authUrl + '/signup', body)
}

export const updateProfile = async (body) => {
  return await put(authUrl + '/update-profile', body)
}

export const login = async (body) => {
  return await post(authUrl + '/login', body)
}
