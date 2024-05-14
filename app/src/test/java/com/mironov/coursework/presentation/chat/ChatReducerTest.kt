package com.mironov.coursework.presentation.chat

import com.mironov.coursework.navigation.router.ChatRouter
import com.mironov.coursework.stub.CHANNEL_NAME
import com.mironov.coursework.stub.EMOJI_NAME
import com.mironov.coursework.stub.MESSAGE_CONTENT
import com.mironov.coursework.stub.MESSAGE_ID
import com.mironov.coursework.stub.TOPIC_NAME
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals

@RunWith(JUnit4::class)
class ChatReducerTest {

    private val router: ChatRouter = mock()
    private val reducer = ChatReducer(router)

    @Test
    fun `event Load EXPECT Commands LoadMessageCache & LoadMessage`() {
        val event = ChatEvent.Ui.Load(CHANNEL_NAME, TOPIC_NAME)
        val state = ChatState()

        val actual = reducer.reduce(event, state)
        val expectedCommands = listOf(
            ChatCommand.LoadMessageCache(CHANNEL_NAME, TOPIC_NAME),
            ChatCommand.LoadMessage(CHANNEL_NAME, TOPIC_NAME),
        )

        assertEquals(expectedCommands, actual.commands)
    }

    @Test
    fun `event ChangeReaction EXPECT Command ChangeReaction`() {
        val event = ChatEvent.Ui.ChangeReaction(MESSAGE_ID, EMOJI_NAME, true)
        val state = ChatState()

        val actual = reducer.reduce(event, state)
        val expected = listOf(ChatCommand.ChangeReaction(MESSAGE_ID, EMOJI_NAME, true))

        assertEquals(expected, actual.commands)
    }

    @Test
    fun `event SendMessage EXPECT Command SendMessage`() {
        val event = ChatEvent.Ui.SendMessage(CHANNEL_NAME, TOPIC_NAME, MESSAGE_CONTENT)
        val state = ChatState()

        val actual = reducer.reduce(event, state)
        val expected = listOf(ChatCommand.SendMessage(CHANNEL_NAME, TOPIC_NAME, MESSAGE_CONTENT))

        assertEquals(expected, actual.commands)
    }

    @Test
    fun `event OnBackClicked EXPECT navigate back`() {
        val event = ChatEvent.Ui.OnBackClicked
        val state = ChatState()

        reducer.reduce(event, state)

        verify(router).back()
    }

    @Test
    fun `event ScrollToTop EXPECT Command LoadPrevMessages`() {
        val event = ChatEvent.Ui.ScrollToTop(CHANNEL_NAME, TOPIC_NAME)
        val state = ChatState()

        val actual = reducer.reduce(event, state)
        val expected = listOf(ChatCommand.LoadPrevMessages(CHANNEL_NAME, TOPIC_NAME))

        assertEquals(expected, actual.commands)
        assertTrue(actual.state.isNextPageLoading)
    }

    @Test
    fun `event ScrollToBottom EXPECT Command LoadNextMessages`() {
        val event = ChatEvent.Ui.ScrollToBottom(CHANNEL_NAME, TOPIC_NAME)
        val state = ChatState()

        val actual = reducer.reduce(event, state)
        val expected = listOf(ChatCommand.LoadNextMessages(CHANNEL_NAME, TOPIC_NAME))

        assertEquals(expected, actual.commands)
        assertTrue(actual.state.isNextPageLoading)
    }
}