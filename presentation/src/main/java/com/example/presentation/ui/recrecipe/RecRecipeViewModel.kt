package com.example.presentation.ui.recrecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GenerateRecipeUseCase
import com.example.presentation.model.IngredientsModel
import com.example.presentation.model.UniteUiState
import com.example.presentation.ui.common.RecipePromptUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class RecRecipeViewModel @Inject constructor(
    private val generateRecipeUseCase: GenerateRecipeUseCase
) : ViewModel() {
    private val _uiModel = MutableStateFlow(
        UniteUiState(
            isFetched = false,
            isLoading = false,
            searchKeyword = "",
            searchKeywordList = emptyList(),
            ingredientsList = emptyList(),
            recipeList = emptyList(),
            wellbeingRecipeList = emptyList()
        )
    )
    val uiModel: StateFlow<UniteUiState> = _uiModel


    fun setSearchKeyword(searchKeyword: String) {
        _uiModel.value = _uiModel.value.copy(searchKeyword = searchKeyword)
    }

    fun setSearchKeywordList(searchKeywordList: List<String>) {
        _uiModel.value = _uiModel.value.copy(searchKeywordList = searchKeywordList)
    }

    fun setIngredientsList(ingredientsList: List<IngredientsModel>) {
        _uiModel.value = _uiModel.value.copy(ingredientsList = ingredientsList)
    }

    fun getIngredients(isIngredients: Boolean = true) {
        _uiModel.value = _uiModel.value.copy(isLoading = true)
        val searchKeyword = _uiModel.value.searchKeyword
        val keyword = RecipePromptUtil.createIngredientsPrompt(searchKeyword)
        viewModelScope.launch(Dispatchers.IO) {
            generateRecipeUseCase(keyword)
                .onSuccess { response ->
                    if (isIngredients) {
                        val ingredientsList =
                            RecipePromptUtil.parseIngredientsResponse(response.content)
                        val wellbeingRecipeList =
                            RecipePromptUtil.parseWellbeingRecipeResponse(response.content)
                        _uiModel.value = _uiModel.value.copy(
                            searchKeyword = searchKeyword,
                            isFetched = true,
                            isLoading = false,
                            ingredientsList = ingredientsList,
                            recipeList = emptyList(),
                            wellbeingRecipeList = wellbeingRecipeList
                        )
                    }
                }.onFailure {
                    withContext(Dispatchers.Main) {
                        _uiModel.value = _uiModel.value.copy(
                            isLoading = false,
                        )
                    }
                }
        }
    }

}

