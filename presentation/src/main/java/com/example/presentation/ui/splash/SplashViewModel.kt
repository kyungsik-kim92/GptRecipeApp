package com.example.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.presentation.ext.LottieAnimateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()


    private val _events = MutableSharedFlow<SplashUiEvent>()
    val events: SharedFlow<SplashUiEvent> = _events.asSharedFlow()

    private fun onAnimationStart() {
        _uiState.value = SplashUiState.Loading
    }

    private fun onAnimationEnd() {
        viewModelScope.launch {
            _uiState.value = SplashUiState.Ready
            _events.emit(SplashUiEvent.RouteToHome)
        }
    }

    fun onLottieAnimationState(state: Any?): Any {
        when (state) {
            is LottieAnimateState.Start -> {
                onAnimationStart()
            }

            is LottieAnimateState.End -> {
                onAnimationEnd()
            }

            is LottieAnimateState.Cancel -> {
                viewModelScope.launch {
                    _events.emit(SplashUiEvent.ShowError("Error Message"))
                }
            }
        }
        return Unit
    }
}