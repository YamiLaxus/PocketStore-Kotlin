package com.phonedev.pocketstore.chat

import com.phonedev.pocketstore.entities.Message

interface OnChatListener {
    fun deleteMessage(message: Message)
}