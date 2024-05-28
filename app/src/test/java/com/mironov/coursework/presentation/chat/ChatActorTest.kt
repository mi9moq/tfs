package com.mironov.coursework.presentation.chat

import app.cash.turbine.test
import com.mironov.coursework.domain.repository.Result
import com.mironov.coursework.domain.usecase.AddReactionUseCase
import com.mironov.coursework.domain.usecase.DeleteMessageUseCase
import com.mironov.coursework.domain.usecase.EditMessageContentUseCase
import com.mironov.coursework.domain.usecase.EditMessageTopicUseCase
import com.mironov.coursework.domain.usecase.GetMessageByIdUseCase
import com.mironov.coursework.domain.usecase.GetMessagesCacheUseCase
import com.mironov.coursework.domain.usecase.GetMessagesUseCase
import com.mironov.coursework.domain.usecase.GetNextMessagesUseCase
import com.mironov.coursework.domain.usecase.GetPrevMessagesUseCase
import com.mironov.coursework.domain.usecase.GetTopicsUseCase
import com.mironov.coursework.domain.usecase.RemoveReactionUseCase
import com.mironov.coursework.domain.usecase.SendMessageUseCase
import com.mironov.coursework.stub.CHANNEL_NAME
import com.mironov.coursework.stub.EMOJI_NAME
import com.mironov.coursework.stub.MESSAGE_CONTENT
import com.mironov.coursework.stub.MESSAGE_ID
import com.mironov.coursework.stub.TOPIC_NAME
import com.mironov.coursework.stub.messages
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class ChatActorTest {

    private val sendMessageUseCase: SendMessageUseCase = mock()
    private val getMessagesUseCase: GetMessagesUseCase = mock()
    private val addReactionUseCase: AddReactionUseCase = mock()
    private val removeReactionUseCase: RemoveReactionUseCase = mock()
    private val getMessageByIdUseCase: GetMessageByIdUseCase = mock()
    private val getNextMessagesUseCase: GetNextMessagesUseCase = mock()
    private val getPrevMessagesUseCase: GetPrevMessagesUseCase = mock()
    private val getMessagesCacheUseCase: GetMessagesCacheUseCase = mock()
    private val getTopicsUseCase: GetTopicsUseCase = mock()
    private val editMessageTopicUseCase: EditMessageTopicUseCase = mock()
    private val editMessageContentUseCase: EditMessageContentUseCase = mock()
    private val deleteMessageUseCase: DeleteMessageUseCase = mock()

    val actor = ChatActor(
        sendMessageUseCase,
        getMessagesUseCase,
        addReactionUseCase,
        removeReactionUseCase,
        getMessageByIdUseCase,
        getNextMessagesUseCase,
        getPrevMessagesUseCase,
        getMessagesCacheUseCase,
        getTopicsUseCase,
        editMessageTopicUseCase,
        editMessageContentUseCase,
        deleteMessageUseCase,
    )

    @Test
    fun `Command LoadMessage & load success EXPECT event LoadMessagesSuccess`() = runTest {
        whenever(getMessagesUseCase(CHANNEL_NAME, TOPIC_NAME)) doReturn Result.Success(messages)
        val command = ChatCommand.LoadMessage(CHANNEL_NAME, TOPIC_NAME)

        val actual = actor.execute(command)

        actual.test {
            assertTrue((awaitItem() is ChatEvent.Domain.LoadMessagesSuccess))
            awaitComplete()
        }
    }

    @Test
    fun `Command LoadMessage & load failure EXPECT event LoadMessagesFailure`() = runTest {
        whenever(getMessagesUseCase(CHANNEL_NAME, TOPIC_NAME)) doReturn Result.Failure("")
        val command = ChatCommand.LoadMessage(CHANNEL_NAME, TOPIC_NAME)

        val actual = actor.execute(command)

        actual.test {
            assertEquals(ChatEvent.Domain.LoadMessagesFailure, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `Command SendMessage & send success EXPECT event SendMessageSuccess`() = runTest {
        whenever(
            sendMessageUseCase(
                CHANNEL_NAME,
                TOPIC_NAME,
                MESSAGE_CONTENT
            )
        ) doReturn Result.Success(true)
        val command = ChatCommand.SendMessage(CHANNEL_NAME, TOPIC_NAME, MESSAGE_CONTENT)

        val actual = actor.execute(command)

        actual.test {
            assertEquals(ChatEvent.Domain.SendMessageSuccess, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `Command SendMessage & send failure EXPECT event SendMessageFailure`() = runTest {
        whenever(
            sendMessageUseCase(
                CHANNEL_NAME,
                TOPIC_NAME,
                MESSAGE_CONTENT
            )
        ) doReturn Result.Failure("")
        val command = ChatCommand.SendMessage(CHANNEL_NAME, TOPIC_NAME, MESSAGE_CONTENT)

        val actual = actor.execute(command)

        actual.test {
            assertEquals(ChatEvent.Domain.SendMessageFailure, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `Command ChangeReaction, emoji selected & result success EXPECT event ChangeReactionSuccess`() =
        runTest {
            whenever(removeReactionUseCase(MESSAGE_ID, EMOJI_NAME)) doReturn Result.Success(true)
            val command = ChatCommand.ChangeReaction(MESSAGE_ID, EMOJI_NAME, true)

            val actual = actor.execute(command)

            actual.test {
                assertEquals(ChatEvent.Domain.ChangeReactionSuccess, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `Command ChangeReaction, emoji selected & result failure EXPECT event ChangeReactionFailure`() =
        runTest {
            whenever(removeReactionUseCase(MESSAGE_ID, EMOJI_NAME)) doReturn Result.Failure("")
            val command = ChatCommand.ChangeReaction(MESSAGE_ID, EMOJI_NAME, true)

            val actual = actor.execute(command)

            actual.test {
                assertEquals(ChatEvent.Domain.ChangeReactionFailure, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `Command ChangeReaction, emoji not selected & result success EXPECT event ChangeReactionSuccess`() =
        runTest {
            whenever(addReactionUseCase(MESSAGE_ID, EMOJI_NAME)) doReturn Result.Success(true)
            val command = ChatCommand.ChangeReaction(MESSAGE_ID, EMOJI_NAME, false)

            val actual = actor.execute(command)

            actual.test {
                assertEquals(ChatEvent.Domain.ChangeReactionSuccess, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `Command ChangeReaction, emoji not selected & result failure EXPECT event ChangeReactionFailure`() =
        runTest {
            whenever(addReactionUseCase(MESSAGE_ID, EMOJI_NAME)) doReturn Result.Failure("")
            val command = ChatCommand.ChangeReaction(MESSAGE_ID, EMOJI_NAME, false)

            val actual = actor.execute(command)

            actual.test {
                assertEquals(ChatEvent.Domain.ChangeReactionFailure, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `Command LoadMessageCache, result success & cache not empty EXPECT event LoadMessagesSuccess`() =
        runTest {
            whenever(getMessagesCacheUseCase(CHANNEL_NAME, TOPIC_NAME)) doReturn Result.Success(
                messages
            )
            val command = ChatCommand.LoadMessageCache(CHANNEL_NAME, TOPIC_NAME)

            val actual = actor.execute(command)

            actual.test {
                assertTrue((awaitItem() is ChatEvent.Domain.LoadMessagesSuccess))
                awaitComplete()
            }
        }

    @Test
    fun `Command LoadMessageCache, result success & cache empty EXPECT event EmptyCache`() =
        runTest {
            whenever(getMessagesCacheUseCase(CHANNEL_NAME, TOPIC_NAME)) doReturn Result.Success(
                emptyList()
            )
            val command = ChatCommand.LoadMessageCache(CHANNEL_NAME, TOPIC_NAME)

            val actual = actor.execute(command)

            actual.test {
                assertEquals(ChatEvent.Domain.EmptyCache, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `Command LoadMessageCache & result failure EXPECT event LoadMessagesFailure`() = runTest {
        whenever(getMessagesCacheUseCase(CHANNEL_NAME, TOPIC_NAME)) doReturn Result.Failure("")
        val command = ChatCommand.LoadMessageCache(CHANNEL_NAME, TOPIC_NAME)

        val actual = actor.execute(command)

        actual.test {
            assertEquals(ChatEvent.Domain.LoadMessagesFailure, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `Command LoadPrevMessages & result success EXPECT event LoadMessagesSuccess`() =
        runTest {
            whenever(getPrevMessagesUseCase(
                CHANNEL_NAME,
                TOPIC_NAME,
                0
            )) doReturn Result.Success(messages)
            val command = ChatCommand.LoadPrevMessages(CHANNEL_NAME, TOPIC_NAME)

            val actual = actor.execute(command)

            actual.test {
                assertTrue((awaitItem() is ChatEvent.Domain.LoadMessagesSuccess))
                awaitComplete()
            }
        }

    @Test
    fun `Command LoadPrevMessages & result failure EXPECT event LoadMessagesFailure`() =
        runTest {
            whenever(getPrevMessagesUseCase(
                CHANNEL_NAME,
                TOPIC_NAME,
                0
            )) doReturn Result.Failure("")
            val command = ChatCommand.LoadPrevMessages(CHANNEL_NAME, TOPIC_NAME)

            val actual = actor.execute(command)

            actual.test {
                assertTrue((awaitItem() is ChatEvent.Domain.LoadMessagesFailure))
                awaitComplete()
            }
        }



    @Test
    fun `Command LoadNextMessages & result success EXPECT event LoadMessagesSuccess`() =
        runTest {
            whenever(getNextMessagesUseCase(
                CHANNEL_NAME,
                TOPIC_NAME,
                0
            )) doReturn Result.Success(messages)
            val command = ChatCommand.LoadNextMessages(CHANNEL_NAME, TOPIC_NAME)

            val actual = actor.execute(command)

            actual.test {
                assertTrue((awaitItem() is ChatEvent.Domain.LoadMessagesSuccess))
                awaitComplete()
            }
        }

    @Test
    fun `Command LoadNextMessages & result failure EXPECT event LoadMessagesFailure`() =
        runTest {
            whenever(getNextMessagesUseCase(
                CHANNEL_NAME,
                TOPIC_NAME,
                0
            )) doReturn Result.Failure("")
            val command = ChatCommand.LoadNextMessages(CHANNEL_NAME, TOPIC_NAME)

            val actual = actor.execute(command)

            actual.test {
                assertTrue((awaitItem() is ChatEvent.Domain.LoadMessagesFailure))
                awaitComplete()
            }
        }
}