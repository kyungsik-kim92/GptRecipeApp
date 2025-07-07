package com.example.presentation.ui.searchingredients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GenerateRecipeUseCase
import com.example.presentation.model.IngredientsModel
import com.example.presentation.model.UniteUiState
import com.example.presentation.ui.common.RecipePromptUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchIngredientsViewModel @Inject constructor(
    private val generateRecipeUseCase: GenerateRecipeUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        UniteUiState(
            isFetched = false,
            isLoading = false,
            searchKeyword = "",
            ingredientsList = emptyList(),
            recipeList = emptyList(),
            wellbeingRecipeList = emptyList()
        )
    )
    val uiState: StateFlow<UniteUiState> = _uiState

    fun setSearchKeyword(searchKeyword: String) {
        _uiState.value = _uiState.value.copy(searchKeyword = searchKeyword)
    }

    fun setIngredientsList(ingredientsList: List<IngredientsModel>) {
        _uiState.value = _uiState.value.copy(ingredientsList = ingredientsList)
    }

    fun getRecipeByIngredients() {
        _uiState.value = _uiState.value.copy(isLoading = true)

        val searchKeyword = _uiState.value.searchKeyword
        val selectedIngredients = _uiState.value.ingredientsList.filter { it.isSelected }

        if (selectedIngredients.isEmpty()) {
            _uiState.value = _uiState.value.copy(isLoading = false)
            return
        }
        val keyword = RecipePromptUtil.createRecipePrompt(searchKeyword, selectedIngredients)
        viewModelScope.launch {
            generateRecipeUseCase(keyword)
                .onSuccess { response ->
                    val (recipeList, wellbeingRecipeList) = RecipePromptUtil.parseRecipeResponse(
                        response.content
                    )

                    _uiState.value = _uiState.value.copy(
                        searchKeyword = searchKeyword,
                        ingredientsList = _uiState.value.ingredientsList,
                        isFetched = true,
                        isLoading = false,
                        recipeList = recipeList,
                        wellbeingRecipeList = wellbeingRecipeList
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                    )
                }
        }
    }

    fun toggleIngredientSelection(ingredientId: String) {
        _uiState.value = _uiState.value.copy(
            ingredientsList = _uiState.value.ingredientsList.map { ingredient ->
                if (ingredient.id == ingredientId) {
                    ingredient.copy(isSelected = !ingredient.isSelected)
                } else ingredient
            }
        )
    }

    fun resetFetchedState() {
        _uiState.value = _uiState.value.copy(isFetched = false)
    }
}