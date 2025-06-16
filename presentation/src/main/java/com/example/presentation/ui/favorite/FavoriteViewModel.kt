package com.example.presentation.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repo.Repository
import com.example.presentation.mapper.toFavoriteModel
import com.example.presentation.model.FavoriteUiState
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
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            repository.getAllFavoritesFlow()
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

    fun toggleIngredientSelection(ingredientId: String) {
        val currentSelected = _uiState.value.selectedIngredients
        val newSelected = if (currentSelected.contains(ingredientId)) {
            currentSelected - ingredientId
        } else {
            currentSelected + ingredientId
        }

        _uiState.value = _uiState.value.copy(
            selectedIngredients = newSelected,
            favoriteList = _uiState.value.favoriteList.map { favorite ->
                favorite.copy(
                    ingredientsList = favorite.ingredientsList.map { ingredient ->
                        if (ingredient.id == ingredientId) {
                            ingredient.copy(isSelected = !ingredient.isSelected)
                        } else ingredient
                    }
                )
            }
        )
    }

    fun toggleRecipeSelection(recipeId: String) {
        val currentSelected = _uiState.value.selectedRecipes
        val newSelected = if (currentSelected.contains(recipeId)) {
            currentSelected - recipeId
        } else {
            currentSelected + recipeId
        }

        _uiState.value = _uiState.value.copy(
            selectedRecipes = newSelected,
            favoriteList = _uiState.value.favoriteList.map { favorite ->
                favorite.copy(
                    recipeList = favorite.recipeList.map { recipe ->
                        if (recipe.id == recipeId) {
                            recipe.copy(isSelected = !recipe.isSelected)
                        } else recipe
                    }
                )
            }
        )
    }

    fun toggleWellbeingRecipeSelection(wellbeingRecipeId: String) {
        val currentSelected = _uiState.value.selectedWellbeingRecipes
        val newSelected = if (currentSelected.contains(wellbeingRecipeId)) {
            currentSelected - wellbeingRecipeId
        } else {
            currentSelected + wellbeingRecipeId
        }

        _uiState.value = _uiState.value.copy(
            selectedWellbeingRecipes = newSelected,
            favoriteList = _uiState.value.favoriteList.map { favorite ->
                favorite.copy(
                    wellbeingRecipeList = favorite.wellbeingRecipeList.map { wellbeingRecipe ->
                        if (wellbeingRecipe.id == wellbeingRecipeId) {
                            wellbeingRecipe.copy(isSelected = !wellbeingRecipe.isSelected)
                        } else wellbeingRecipe
                    }
                )
            }
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}