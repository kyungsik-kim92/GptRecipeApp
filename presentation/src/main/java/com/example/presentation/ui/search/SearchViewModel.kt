package com.example.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GenerateRecipeUseCase
import com.example.presentation.ui.common.RecipePromptUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val generateRecipeUseCase: GenerateRecipeUseCase
) : ViewModel() {

    private val _uiModel = MutableStateFlow(
        SearchUiState(
            searchKeyword = "",
            isFetched = false,
            isLoading = false,
            ingredientsList = emptyList()
        )
    )
    val uiModel: StateFlow<SearchUiState> = _uiModel

    fun getIngredientsByRecipe(searchKeyword: String) {
        viewModelScope.launch {
            _uiModel.update { it.copy(isLoading = true) }
            val keyword = RecipePromptUtil.createIngredientsPrompt(searchKeyword)

            generateRecipeUseCase(keyword)
                .onSuccess { response ->
                    val ingredientsList = RecipePromptUtil.parseIngredientsResponse(response.content)
                    _uiModel.update {
                        it.copy(
                            searchKeyword = searchKeyword,
                            isFetched = true,
                            isLoading = false,
                            ingredientsList = ingredientsList
                        )
                    }
                }
                .onFailure { exception ->
                    _uiModel.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                }
        }
    }
}