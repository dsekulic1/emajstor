import React, { useState } from 'react'
import { ChatEngine, getOrCreateChat } from 'react-chat-engine'
import { getUser } from '../../utilities/localStorage'
import { addChatMessage } from 'api/chatApi/chat'

const DirectChat = () => {
  // The useState hook initially sets the username to an empty string
  const [username, setUsername] = useState('')
  const user = getUser().username
  const password = getUser().password

  // Custom function that will implement the getOrCreateChat function that to select username to chat with
  function implementingDirectChat(creds) {
    getOrCreateChat(
      creds,
      // function will only work if the app is a Direct Messaging one, hence setting 'is_direct_chat' to true and consequentially setting a list of usernames to search from.
      { is_direct_chat: true, usernames: [username] },
      () => setUsername('')
    )
  }

  const onMessage = async (chatId, message) => {
    const payload = {
      text: message.text.replace('<p>', '').replace('</p>', ''),
      senderUsername: message.sender.username,
      chatId: chatId,
    }
    await addChatMessage(payload)
  }

  const displayChatInterface = (creds) => {
    return (
      <div>
        <input
          type='text'
          placeholder='Find username'
          value={username} //prop from the useState hook
          // A controlled function that sets the username to what the user types in the input field
          onChange={(e) => setUsername(e.target.value)}
        />

        {/* clicking button will call the implementingDirectChat function that displays a list of usernames to create or find an existing chat.  */}
        <button onClick={() => implementingDirectChat(creds)}>
          Create Chat
        </button>
      </div>
    )
  }

  return (
    <ChatEngine
      height='100vh'
      width='100vw'
      projectID={process.env.REACT_APP_PROJECT_ID}
      userName={user}
      userSecret={password}
      onNewMessage={(chatId, message) => {
        onMessage(chatId, message)
      }}
      displayNewChatInterface={(credentials) =>
        displayChatInterface(credentials)
      }
    />
  )
}
export default DirectChat
