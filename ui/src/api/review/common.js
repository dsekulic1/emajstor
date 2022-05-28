import { basicGet, basicPut, basicPost } from 'api/common'

const jobUrl = '/review'

export const get = async (url) => {
  return await basicGet(jobUrl + url)
}

export const post = async (url, data) => {
  return await basicPost(jobUrl + url, data)
}

export const put = async (url, data) => {
  return await basicPut(jobUrl + url, data)
}
