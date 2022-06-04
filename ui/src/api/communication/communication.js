import { basicGet, basicPost } from 'api/common'

export const getAllChatMessages = async () => {
  return await basicGet('/communication/api/chat/message/all')
}

export const addResponse = async (id, data) => {
  return await basicPost('/communication/api/chat/message/' + id + '/' + data)
}

export const addMessage = async (data) => {
  return await basicPost('/communication/api/chat/message', data)
}
