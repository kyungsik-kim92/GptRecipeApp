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

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()


    private val _events = MutableSharedFlow<SplashUiEvent>()
    val events: SharedFlow<SplashUiEvent> = _events.asSharedFlow()

    fun onAnimationStart() {
        _uiState.value = SplashUiState.Loading
        initializeApp()
    }

    fun onAnimationEnd() {
        viewModelScope.launch {
            _events.emit(SplashUiEvent.RouteToHome)
        }
    }

    private fun initializeApp() {
        viewModelScope.launch {
            try {
                delay(2000)
                _uiState.value = SplashUiState.Ready
                delay(1000)
                _events.emit(SplashUiEvent.RouteToHome)

            } catch (e: Exception) {
                _uiState.value = SplashUiState.Error(e.message ?: "초기화 실패")
            }
        }
    }
}
