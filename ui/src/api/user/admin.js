import { basicGet, basicPut } from 'api/common'

const userUrl = '/user/api'

export const getAllUsers = async () => {
  return await basicGet(userUrl + '/users/all')
}

export const updateUser = async (body) => {
  return await basicPut(userUrl + '/users', body)
}
