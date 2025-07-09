package com.example.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    private val _viewState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val viewState: StateFlow<SplashUiState> = _viewState.asStateFlow()


    private val _viewEvent = MutableSharedFlow<SplashUiEvent>()
    val viewEvent: SharedFlow<SplashUiEvent> = _viewEvent.asSharedFlow()

    fun onAnimationStart() {
        _viewState.value = SplashUiState.Loading
        initializeApp()
    }

    fun onAnimationEnd() {
        viewModelScope.launch {
            _viewEvent.emit(SplashUiEvent.RouteToHome)
        }
    }

    private fun initializeApp() {
        viewModelScope.launch {
            try {
                delay(2000)
                _viewState.value = SplashUiState.Ready
                delay(1000)
                _viewEvent.emit(SplashUiEvent.RouteToHome)

            } catch (e: Exception) {
                _viewState.value = SplashUiState.Error(e.message ?: "초기화 실패")
            }
        }
    }
}
