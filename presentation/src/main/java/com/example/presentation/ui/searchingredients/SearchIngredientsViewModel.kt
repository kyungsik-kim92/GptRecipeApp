package com.example.presentation.ui.searchingredients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GenerateRecipeUseCase
import com.example.presentation.model.IngredientsModel
import com.example.presentation.model.UniteUiState
import com.example.presentation.ui.common.RecipePromptUtil
import com.example.presentation.ui.search.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchIngredientsViewModel @Inject constructor(
    private val generateRecipeUseCase: GenerateRecipeUseCase
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<SearchIngredientsUiState>(SearchIngredientsUiState.Idle())
    val uiState: StateFlow<SearchIngredientsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SearchIngredientsUiEvent>()
    val events: SharedFlow<SearchIngredientsUiEvent> = _events.asSharedFlow()

    fun setSearchResult(searchUiState: SearchUiState) {
        val currentState = _uiState.value

        if (searchUiState is SearchUiState.Success) {
            _uiState.value = SearchIngredientsUiState.Idle(
                searchKeyword = searchUiState.searchKeyword,
                ingredientsList = if (currentState.ingredientsList.isEmpty()) {
                    searchUiState.ingredientsList
                } else {
                    currentState.ingredientsList
                }
            )
        } else {
            _uiState.value = SearchIngredientsUiState.Idle(
                searchKeyword = searchUiState.searchKeyword,
                ingredientsList = currentState.ingredientsList
            )
        }
    }

    fun getRecipeByIngredients() {
        val currentState = _uiState.value
        val selectedIngredients = currentState.ingredientsList.filter { it.isSelected }

        if (selectedIngredients.isEmpty()) {
            return
        }

        _uiState.value = SearchIngredientsUiState.Loading(
            searchKeyword = currentState.searchKeyword,
            ingredientsList = currentState.ingredientsList
        )

        val keyword =
            RecipePromptUtil.createRecipePrompt(currentState.searchKeyword, selectedIngredients)

        viewModelScope.launch {
            generateRecipeUseCase(keyword)
                .onSuccess { response ->
                    val (recipeList, wellbeingRecipeList) = RecipePromptUtil.parseRecipeResponse(
                        response.content
                    )

                    val successState = SearchIngredientsUiState.Success(
                        searchKeyword = currentState.searchKeyword,
                        ingredientsList = currentState.ingredientsList,
                        recipeList = recipeList,
                        wellbeingRecipeList = wellbeingRecipeList
                    )
                    _uiState.value = successState
                    val uniteUiState = UniteUiState(
                        searchKeyword = currentState.searchKeyword,
                        ingredientsList = selectedIngredients,
                        recipeList = recipeList,
                        wellbeingRecipeList = wellbeingRecipeList
                    )
                    _events.emit(SearchIngredientsUiEvent.RouteToRecipe(uniteUiState))
                }
                .onFailure { exception ->
                    val errorState = SearchIngredientsUiState.Error(
                        searchKeyword = currentState.searchKeyword,
                        ingredientsList = currentState.ingredientsList,
                        message = exception.message ?: "Exception Message"
                    )
                    _uiState.value = errorState
                    _events.emit(SearchIngredientsUiEvent.ShowError(errorState.message))
                }
        }
    }

    fun setIngredientsList(ingredientsList: List<IngredientsModel>) {
        _uiState.update { currentState ->
            when (currentState) {
                is SearchIngredientsUiState.Idle -> currentState.copy(ingredientsList = ingredientsList)
                is SearchIngredientsUiState.Loading -> currentState.copy(ingredientsList = ingredientsList)
                is SearchIngredientsUiState.Success -> currentState.copy(ingredientsList = ingredientsList)
                is SearchIngredientsUiState.Error -> currentState.copy(ingredientsList = ingredientsList)
            }
        }
    }

    fun toggleIngredientSelection(ingredientId: String) {
        _uiState.update { currentState ->
            val updatedIngredients = currentState.ingredientsList.map { ingredient ->
                if (ingredient.id == ingredientId) {
                    ingredient.copy(isSelected = !ingredient.isSelected)
                } else ingredient
            }
            when (currentState) {
                is SearchIngredientsUiState.Idle -> currentState.copy(ingredientsList = updatedIngredients)
                is SearchIngredientsUiState.Loading -> currentState.copy(ingredientsList = updatedIngredients)
                is SearchIngredientsUiState.Success -> currentState.copy(ingredientsList = updatedIngredients)
                is SearchIngredientsUiState.Error -> currentState.copy(ingredientsList = updatedIngredients)
            }
        }
    }
}