package com.mironov.coursework.screen

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.mironov.coursework.R
import com.mironov.coursework.ui.chat.ChatFragment
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object ChatFragmentScreen : KScreen<ChatFragmentScreen>() {

    override val layoutId: Int = R.layout.fragment_chat
    override val viewClass: Class<*> = ChatFragment::class.java

    val errorMessage = KTextView { withId(R.id.chat_error_message) }
    val tryAgain = KButton { withId(R.id.try_again) }
    val messageInput = KEditText { withId(R.id.message_input) }
    val sendMessage = KImageView { withId(R.id.send_message) }

    val recycler = KRecyclerView({ withId(R.id.messages) },
        {
            itemType(::KDateItem)
            itemType(::KReceivedItem)
            itemType(::KSentItem)
        }
    )

    class KDateItem(parent: Matcher<View>) : KRecyclerItem<KDateItem>(parent) {
        val date = KTextView(parent) { withId(R.id.date) }
    }

    class KReceivedItem(parent: Matcher<View>) : KRecyclerItem<KReceivedItem>(parent) {
    }

    class KSentItem(parent: Matcher<View>) : KRecyclerItem<KSentItem>(parent) {
    }
}