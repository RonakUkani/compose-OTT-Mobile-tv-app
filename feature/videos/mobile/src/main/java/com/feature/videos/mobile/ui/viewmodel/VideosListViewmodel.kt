package com.feature.videos.mobile.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.result.NetworkResult
import com.domain.model.VideosList
import com.domain.usecase.GetVideoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VideosListViewmodel @Inject constructor(private val getVideoListUseCase: GetVideoListUseCase) :
    ViewModel() {
    private val _uiState = MutableStateFlow(VideosListUiState())
    val uiState: StateFlow<VideosListUiState> = _uiState.asStateFlow()
    private var page by mutableIntStateOf(1)
    var canPaginate by mutableStateOf(false)
    val currentlyPlayingIndex = MutableStateFlow<Int?>(null)

    init {
        getVideoList()
    }

    fun getVideoList(key: String = "45797795-33f561f0a247d35ec981f414e") {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                if (page == 1) {
                    _uiState.update { it.copy(isLoading = true, listState = ListState.LOADING) }
                } else {
                    _uiState.update { it.copy(isLoading = false, listState = ListState.PAGINATING) }
                }
            }
            when (val result = getVideoListUseCase.invoke(key, page.toString())) {
                is NetworkResult.Success -> {
                    canPaginate = result.data.hits.size == 10
                    val data: VideosList = if (page == 1) { result.data } else {
                        uiState.value.data.hits.addAll(result.data.hits)
                        VideosList(
                            total = result.data.total,
                            totalHits = result.data.totalHits,
                            hits = uiState.value.data.hits
                        )
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            data = data,
                            listState = ListState.IDLE
                        )
                    }

                    if (canPaginate) {
                        withContext(Dispatchers.Main) {
                            page++
                        }
                    }
                }

                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false, error = result.message,
                            listState = if (page == 1) ListState.ERROR else ListState.PAGINATION_EXHAUST
                        )
                    }
                }
            }
        }
    }

    fun onPlayVideoClick(playbackPosition: Long, videoIndex: Int) {
        when (currentlyPlayingIndex.value) {
            null -> currentlyPlayingIndex.value = videoIndex
            videoIndex -> {
                currentlyPlayingIndex.value = null
                val data = VideosList(
                    total = uiState.value.data.total,
                    totalHits = uiState.value.data.totalHits,
                    hits = uiState.value.data.hits.toMutableList().also { list ->
                        list[videoIndex] = list[videoIndex].copy(lastPlayedPosition = playbackPosition)
                    }
                )
                _uiState.value = VideosListUiState(data = data)
            }
            else -> {
                val data = VideosList(
                    total = uiState.value.data.total,
                    totalHits = uiState.value.data.totalHits,
                    hits = uiState.value.data.hits.toMutableList().also { list ->
                        list[currentlyPlayingIndex.value!!] = list[currentlyPlayingIndex.value!!].copy(lastPlayedPosition = playbackPosition)
                    }
                )
                _uiState.value = VideosListUiState(data = data)
                currentlyPlayingIndex.value = videoIndex
            }
        }
    }


    data class VideosListUiState(
        val isLoading: Boolean = false,
        var data: VideosList = VideosList(),
        val error: String? = null,
        val listState: ListState? = null
    )

    enum class ListState {
        IDLE,
        LOADING,
        PAGINATING,
        ERROR,
        PAGINATION_EXHAUST,
    }
}