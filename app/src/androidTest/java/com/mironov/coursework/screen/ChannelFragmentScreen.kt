package com.mironov.coursework.screen

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.mironov.coursework.R
import com.mironov.coursework.ui.channels.ChannelsPageFragment
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object ChannelFragmentScreen : KScreen<ChannelFragmentScreen>() {

    override val layoutId: Int = R.layout.fragment_channels_page
    override val viewClass: Class<*> = ChannelsPageFragment::class.java

    val channels = KRecyclerView({ withId(R.id.channels) },
        {
            itemType(::KChannelItem)
            itemType(::KTopicItem)
        })

    class KChannelItem(parent: Matcher<View>) : KRecyclerItem<KChannelItem>(parent) {
        val channel = KTextView(parent) { withId(R.id.channel) }
    }

    class KTopicItem(parent: Matcher<View>) : KRecyclerItem<KTopicItem>(parent) {
        val topic = KTextView(parent) { withId(R.id.topic_name) }
    }
}