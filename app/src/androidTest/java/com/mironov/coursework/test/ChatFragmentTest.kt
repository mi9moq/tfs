package com.mironov.coursework.test

import android.os.Bundle
import com.github.tomakehurst.wiremock.client.WireMock
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.mironov.coursework.R
import com.mironov.coursework.screen.ChatFragmentScreen
import com.mironov.coursework.ui.chat.ChatFragment
import com.mironov.coursework.util.FragmentTestRule
import com.mironov.coursework.util.MockMessages
import com.mironov.coursework.util.MockMessages.Companion.messages
import com.mironov.coursework.util.firstMessageDate
import org.junit.Rule
import org.junit.Test
import com.github.tomakehurst.wiremock.client.WireMock.verify

class ChatFragmentTest: TestCase() {

    @get:Rule
    val rule = FragmentTestRule(ChatFragment(), Bundle().apply {
        putString("channel name", "Test Channel")
        putString("topic name", "Test topic")
    })

    @Test
    fun openChatWithContent() = run {
        rule.wiremockRule.messages {
            withGetMessages()
        }
        ChatFragmentScreen{
            step("Проверяем, что чат загрузился"){
                recycler.childAt<ChatFragmentScreen.KDateItem>(0){
                    date.hasText(firstMessageDate)
                }
                errorMessage.isGone()
            }
            step("Проверяем, что поле ввода сообщения пустое, отображается иконка загрузки фала") {
                messageInput.hasEmptyText()
                sendMessage.hasDrawable(R.drawable.ic_upload)
            }
            step("Проверяем, что поле ввода сообщения не пустое, отображается иконка отправки сообщения") {
                messageInput.replaceText(MESSAGE_TEXT)
                sendMessage.hasDrawable(R.drawable.ic_send_message)
            }
        }
    }

    @Test
    fun shouldSendMessage() = run{
        rule.wiremockRule.messages {
            withGetMessages()
            withPostMessages()
        }
        ChatFragmentScreen{
            step("Вводим сообщение"){
                messageInput.replaceText(MESSAGE_TEXT)
            }
            step("Нажимаем отправить"){
                sendMessage.click()
            }
            step("Проверяем, что сообщение отправленно"){
                verify(WireMock.getRequestedFor(MockMessages.messagesUrlPattern))
            }
            step("Проверяем, что поле ввода сообщения пустое") {
                messageInput.hasEmptyText()
            }
        }
        Thread.sleep(10000)
    }

    private companion object {
        const val MESSAGE_TEXT = "Новое сообщение"
    }
}