package com.example.presentation.ui.splash

sealed class SplashUiState {
    data object Loading : SplashUiState()
    data object Ready : SplashUiState()
    data class Error(val message: String) : SplashUiState()
}


sealed class SplashUiEvent {
    data object RouteToHome : SplashUiEvent()
    data class ShowError(val message: String) : SplashUiEvent()
}