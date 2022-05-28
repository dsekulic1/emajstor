import { basicGet, basicPost } from 'api/common'

const jobApi = '/review/api/review'

export const getAllReviews = async () => {
  return await basicGet(jobApi + '/all')
}

export const addReview = async (data) => {
  return await basicPost(jobApi, data)
}
