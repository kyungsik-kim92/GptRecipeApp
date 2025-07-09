package com.example.presentation.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetFavoriteRecipesFlowUseCase
import com.example.presentation.mapper.toFavoriteModel
import com.example.presentation.model.FavoriteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteRecipesFlowUseCase: GetFavoriteRecipesFlowUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<FavoriteUiEvent>()
    val events = _events.asSharedFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            _uiState.value = FavoriteUiState.Loading
            getFavoriteRecipesFlowUseCase()
                .flowOn(Dispatchers.IO)
                .catch { exception ->
                    _uiState.value = FavoriteUiState.Error(
                        exception.message ?: "Exception Message"
                    )
                    _events.emit(
                        FavoriteUiEvent.ShowError(
                            exception.message ?: "Exception Message"
                        )
                    )
                }
                .collect { recipeList ->
                    val favoriteList = recipeList.map { it.toFavoriteModel() }
                    _uiState.value = FavoriteUiState.Success(favoriteList = favoriteList)
                }
        }
    }

    fun onFavoriteItemClick(favoriteModel: FavoriteModel) {
        viewModelScope.launch {
            _events.emit(FavoriteUiEvent.RouteToRecipe(favoriteModel))
        }
    }
}