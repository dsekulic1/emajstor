import { basicGet } from 'api/common'

const userUrl = '/user/api'

export const getAllUsers = async () => {
  return await basicGet(personUrl + '/users/all')
}
