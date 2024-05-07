package com.mironov.coursework.ui.main

import androidx.fragment.app.Fragment
import vivid.money.elmslie.android.renderer.ElmRendererDelegate
import vivid.money.elmslie.core.store.Store

abstract class ElmBaseFragment<Effect : Any, State : Any, Event : Any> : Fragment(),
    ElmRendererDelegate<Effect, State> {

    abstract val store: Store<Event, Effect, State>
}
