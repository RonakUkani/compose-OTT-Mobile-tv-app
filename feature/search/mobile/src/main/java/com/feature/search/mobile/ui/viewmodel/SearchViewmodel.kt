package com.feature.search.mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.mobile.ui.util.dummyImages
import com.core.common.mobile.ui.util.dummyVideoUrl
import com.data.result.NetworkResult
import com.domain.model.Films
import com.domain.usecase.GetHomePageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewmodel @Inject constructor(private val getHomePageUseCase: GetHomePageUseCase) :
    ViewModel() {
    private val _uiState = MutableStateFlow(FilmsUiState())
    val uiState: StateFlow<FilmsUiState> = _uiState.asStateFlow()

    init {
        getHomePageData()
    }

    private fun getHomePageData() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = getHomePageUseCase.invoke()) {
                is NetworkResult.Success -> {
                    result.data.results.mapIndexed { index, films ->
                        films.thumbUrl =
                            if (dummyImages.size > index) dummyImages[index] else dummyImages[0]
                        films.videoUrl =
                            if (dummyVideoUrl.size > index) dummyVideoUrl[index] else dummyVideoUrl[0]
                        films.isLiveUrl = films.videoUrl?.contains("live") ?: false
                    }
                    _uiState.update { it.copy(isLoading = false, data = result.data) }
                }

                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }


    data class FilmsUiState(
        val isLoading: Boolean = false,
        val data: Films = Films(),
        val error: String? = null
    )
}