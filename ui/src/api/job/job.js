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

export const addJob = async (data) => {
  return await basicPost(jobApi, data)
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

export const getAllDeals = async () => {
  return await basicGet('/job/api/deal/all')
}

export const getOrAddBusiness = async (name) => {
  return await basicPost('/job/api/business/' + name)
}

export const resolveDeal = async (id) => {
  return await basicPost('/job/api/deal/resolve/' + id)
}

export const addUserImages = async (data) => {
  return await basicPostGallery('/api/userfoto/upload', data)
}

export const addUserFoto = async (data) => {
  return await basicPostGallery('/api/userfoto', data)
}

export const getUserFoto = async (id) => {
  return await basicGetGallery('/api/userfoto/' + id)
}

export const getAllUserFoto = async (id) => {
  return await basicGetGallery('/api/userfoto/all')
}
