package com.mironov.coursework.test

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.mironov.coursework.screen.ChannelFragmentScreen
import com.mironov.coursework.ui.main.MainActivity
import com.mironov.coursework.util.AppTestRule
import com.mironov.coursework.util.CHANNEL_NAME
import com.mironov.coursework.util.CHANNEL_NAME_KEY
import com.mironov.coursework.util.MockChannels.Companion.channels
import com.mironov.coursework.util.MockMessages.Companion.messages
import com.mironov.coursework.util.TOPIC_NAME
import com.mironov.coursework.util.TOPIC_NAME_KEY
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ChannelsPageFragmentTest : TestCase() {

    @get:Rule
    val rule = AppTestRule<MainActivity>(getIntent())

    @Test
    fun fromChannelsToChat() = run {
        rule.wiremockRule.channels {
            withChannels()
        }
        rule.wiremockRule.messages {
            withGetMessages()
        }
        ChannelFragmentScreen {
            step("Открываем список топиков") {
                channels.childAt<ChannelFragmentScreen.KChannelItem>(0) {
                    click()
                }
            }
            step("Переходив в чат") {
                channels.childAt<ChannelFragmentScreen.KTopicItem>(1) {
                    click()
                }
            }
            step("Проверяем переданные аргументы") {
                rule.activityScenarioRule.scenario.onActivity {
                    it.supportFragmentManager.fragments[0]?.arguments?.apply {
                        assertEquals(CHANNEL_NAME, getString(CHANNEL_NAME_KEY))
                        assertEquals(TOPIC_NAME, getString(TOPIC_NAME_KEY))
                    }
                }
            }
        }
    }

    private fun getIntent(): Intent =
        Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
}