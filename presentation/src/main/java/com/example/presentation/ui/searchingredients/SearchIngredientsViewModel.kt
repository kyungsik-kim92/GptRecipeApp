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
    private val _uiModel = MutableStateFlow(
        UniteUiState(
            isFetched = false,
            isLoading = false,
            searchKeyword = "",
            ingredientsList = emptyList(),
            recipeList = emptyList(),
            wellbeingRecipeList = emptyList()
        )
    )
    val uiModel: StateFlow<UniteUiState> = _uiModel

    fun setSearchKeyword(searchKeyword: String) {
        _uiModel.value = _uiModel.value.copy(searchKeyword = searchKeyword)
    }

    fun setIngredientsList(ingredientsList: List<IngredientsModel>) {
        _uiModel.value = _uiModel.value.copy(ingredientsList = ingredientsList)
    }

    fun getRecipeByIngredients() {
        _uiModel.value = _uiModel.value.copy(isLoading = true)

        val searchKeyword = _uiModel.value.searchKeyword
        val selectedIngredients = _uiModel.value.ingredientsList.filter { it.isSelected }

        if (selectedIngredients.isEmpty()) {
            _uiModel.value = _uiModel.value.copy(isLoading = false)
            return
        }
        val keyword = RecipePromptUtil.createRecipePrompt(searchKeyword, selectedIngredients)
        viewModelScope.launch {
            generateRecipeUseCase(keyword)
                .onSuccess { response ->
                    val (recipeList, wellbeingRecipeList) = RecipePromptUtil.parseRecipeResponse(
                        response.content
                    )

                    _uiModel.value = _uiModel.value.copy(
                        searchKeyword = searchKeyword,
                        ingredientsList = _uiModel.value.ingredientsList,
                        isFetched = true,
                        isLoading = false,
                        recipeList = recipeList,
                        wellbeingRecipeList = wellbeingRecipeList
                    )
                }
                .onFailure { exception ->
                    _uiModel.value = _uiModel.value.copy(
                        isLoading = false,
                    )
                }
        }
    }

    fun toggleIngredientSelection(ingredientId: String) {
        _uiModel.value = _uiModel.value.copy(
            ingredientsList = _uiModel.value.ingredientsList.map { ingredient ->
                if (ingredient.id == ingredientId) {
                    ingredient.copy(isSelected = !ingredient.isSelected)
                } else ingredient
            }
        )
    }

    fun resetFetchedState() {
        _uiModel.value = _uiModel.value.copy(isFetched = false)
    }
}