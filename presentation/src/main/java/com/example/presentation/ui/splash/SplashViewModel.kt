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

    private val _viewState = MutableStateFlow<SplashViewState>(SplashViewState.Loading)
    val viewState: StateFlow<SplashViewState> = _viewState.asStateFlow()


    private val _viewEvent = MutableSharedFlow<SplashViewEvent>()
    val viewEvent: SharedFlow<SplashViewEvent> = _viewEvent.asSharedFlow()

    fun onAnimationStart() {
        _viewState.value = SplashViewState.Loading
        initializeApp()
    }

    fun onAnimationEnd() {
        viewModelScope.launch {
            _viewEvent.emit(SplashViewEvent.RouteToHome)
        }
    }

    private fun initializeApp() {
        viewModelScope.launch {
            try {
                delay(2000)
                _viewState.value = SplashViewState.Ready
                delay(1000)
                _viewEvent.emit(SplashViewEvent.RouteToHome)

            } catch (e: Exception) {
                _viewState.value = SplashViewState.Error(e.message ?: "초기화 실패")
            }
        }
    }
}
