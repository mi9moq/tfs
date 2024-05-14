package com.mironov.coursework.data.datasource.message

import com.mironov.coursework.data.database.MessageDao
import com.mironov.coursework.stub.CHANNEL_NAME
import com.mironov.coursework.stub.TOPIC_NAME
import com.mironov.coursework.stub.messagesDbModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class MessageLocalDataSourceImplTest {

    private val dao: MessageDao = mock()
    private val dataSource = MessageLocalDataSourceImpl(dao)

    @Test
    fun `get EXPECT list db model`() = runTest {
        whenever(dao.getMessages(CHANNEL_NAME, TOPIC_NAME)) doReturn messagesDbModel

        val expected = messagesDbModel
        val actual = dataSource.getMessages(CHANNEL_NAME, TOPIC_NAME)

        assertEquals(expected, actual)
    }

    @Test
    fun `insert EXPECT clear current chat & insert messages`() = runTest {

        dataSource.insertMessages(messagesDbModel, CHANNEL_NAME, TOPIC_NAME)

        verify(dao).clearTopic(CHANNEL_NAME, TOPIC_NAME)
        verify(dao).insertMessages(messagesDbModel)
    }
}