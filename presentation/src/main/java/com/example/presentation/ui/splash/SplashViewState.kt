package com.example.presentation.ui.splash

sealed class SplashViewState {
    data object Loading : SplashViewState()
    data object Ready : SplashViewState()
    data class Error(val message: String) : SplashViewState()
}


sealed class SplashViewEvent {
    data object RouteToHome : SplashViewEvent()
    data class ShowError(val message: String) : SplashViewEvent()
}