import {
  basicGet,
  basicPost,
  basicGetGallery,
  basicPostGallery,
} from 'api/common'

const jobApi = '/job/api/job'

export const getAllJobs = async () => {
  return await basicGet(jobApi + '/all')
}

export const getAllGallery = async () => {
  return await basicGetGallery('/api/gallery/all')
}

export const getAllGalleryImages = async () => {
  return await basicGetGallery('/api/gallery/allimages')
}

export const addImages = async (data) => {
  return await basicPostGallery('/api/gallery/upload', data)
}

export const addGallery = async (data) => {
  return await basicPostGallery('/api/gallery', data)
}

export const addDeal = async (data) => {
  return await basicPost('/job/api/deal', data)
}
