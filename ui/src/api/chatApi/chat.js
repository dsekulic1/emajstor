import { post } from './common'

const chatUrl = '/chat/message'

export const addChatMessage = async (body) => {
  return await post(chatUrl, body)
}
