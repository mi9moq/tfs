package com.mironov.coursework.data.datasource.message

import com.mironov.coursework.data.network.api.ZulipApi
import com.mironov.coursework.data.utils.Narrow
import com.mironov.coursework.stub.CHANNEL_NAME
import com.mironov.coursework.stub.MESSAGE_CONTENT
import com.mironov.coursework.stub.TOPIC_NAME
import com.mironov.coursework.stub.messagesDto
import com.mironov.coursework.stub.messagesResponse
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class MessageRemoteDataSourceImplTest {

    private val api: ZulipApi = mock()
    private val dataSource = MessageRemoteDataSourceImpl(api)

    @Test
    fun `send EXPECT send message`() = runTest {

        dataSource.sendMessage(CHANNEL_NAME, TOPIC_NAME, MESSAGE_CONTENT)

        verify(api).sendMessage(to = CHANNEL_NAME, topic = TOPIC_NAME, content = MESSAGE_CONTENT)
    }

    @Test
    fun `get EXEPCT list messagesDto`() = runTest {
        val narrow = mutableListOf<Narrow>().apply {
            add(Narrow(Narrow.STREAM, CHANNEL_NAME))
            add(Narrow(Narrow.TOPIC, TOPIC_NAME))
        }
        whenever(api.getMessages(narrow = Json.encodeToString(narrow))) doReturn messagesResponse

        val expected = messagesDto
        val actual = dataSource.getMessages(CHANNEL_NAME, TOPIC_NAME)

        assertEquals(expected, actual)
    }
}