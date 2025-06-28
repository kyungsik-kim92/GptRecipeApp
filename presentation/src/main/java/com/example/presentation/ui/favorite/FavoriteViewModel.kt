package com.example.presentation.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetFavoriteRecipesFlowUseCase
import com.example.presentation.mapper.toFavoriteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteRecipesFlowUseCase: GetFavoriteRecipesFlowUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getFavoriteRecipesFlowUseCase()
                .flowOn(Dispatchers.IO)
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message
                    )
                }
                .collect { recipeList ->
                    _uiState.value = _uiState.value.copy(
                        favoriteList = recipeList.map { it.toFavoriteModel() },
                        isLoading = false
                    )
                }
        }
    }
}