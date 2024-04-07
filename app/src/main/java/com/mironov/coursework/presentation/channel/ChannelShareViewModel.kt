package com.mironov.coursework.presentation.channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ChannelShareViewModel : ViewModel() {

    private val _state = MutableStateFlow<SharedChannelState>(SharedChannelState.Initial)
    val state = _state.asStateFlow()

    val searchQuery = MutableSharedFlow<QueryItem>(extraBufferCapacity = 1)

    init {
        listenSearchQuery()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun listenSearchQuery() {
        searchQuery
            .distinctUntilChanged()
            .debounce(SEARCH_DURATION_MILLIS)
            .flatMapLatest { flow { emit(it) } }
            .onEach {
                _state.value = SharedChannelState.Content(it)
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    companion object {

        private const val SEARCH_DURATION_MILLIS = 500L
    }
}