import { basicGet } from 'api/common'

const jobApi = '/job/api/job'

export const getAllJobs = async () => {
  return await basicGet(jobApi + '/all')
}
