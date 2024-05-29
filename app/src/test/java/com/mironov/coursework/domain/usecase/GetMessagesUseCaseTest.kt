package com.mironov.coursework.domain.usecase

import com.mironov.coursework.domain.repository.MessageRepository
import com.mironov.coursework.domain.repository.Result
import com.mironov.coursework.stub.CHANNEL_NAME
import com.mironov.coursework.stub.TOPIC_NAME
import com.mironov.coursework.stub.messages
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class GetMessagesUseCaseTest {

    private val repository: MessageRepository = mock()
    private val useCase = GetMessagesUseCase(repository)

    @Test
    fun `invoke EXPECT Result Success`() = runTest {
        whenever(repository.getMessages(CHANNEL_NAME, TOPIC_NAME)) doReturn Result.Success(messages)

        val expected = Result.Success(messages)
        val actual = useCase(CHANNEL_NAME, TOPIC_NAME)

        assertEquals(expected, actual)
    }

    @Test
    fun `invoke EXPECT Result Failure`() = runTest {
        whenever(repository.getMessages(CHANNEL_NAME, TOPIC_NAME)) doReturn Result.Failure("")

        val expected = Result.Failure("")
        val actual = useCase(CHANNEL_NAME, TOPIC_NAME)

        assertEquals(expected, actual)
    }
}