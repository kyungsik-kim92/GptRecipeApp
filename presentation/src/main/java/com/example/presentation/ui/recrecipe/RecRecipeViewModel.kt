package com.example.presentation.ui.recrecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GenerateRecipeUseCase
import com.example.presentation.model.IngredientsModel
import com.example.presentation.model.UniteUiState
import com.example.presentation.ui.common.RecipePromptUtil
import com.example.presentation.ui.recingredients.RecIngredientsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
class RecRecipeViewModel @Inject constructor(
    private val generateRecipeUseCase: GenerateRecipeUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<RecRecipeUiState>(RecRecipeUiState.Idle())
    val uiState: StateFlow<RecRecipeUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RecRecipeUiEvent>()
    val events: SharedFlow<RecRecipeUiEvent> = _events.asSharedFlow()

    fun setupData(recIngredientsUiModel: RecIngredientsUiState) {
        val currentState = _uiState.value
        _uiState.value = RecRecipeUiState.Idle(
            searchKeyword = currentState.searchKeyword,
            searchKeywordList = recIngredientsUiModel.searchKeywordList,
            ingredientsList = recIngredientsUiModel.ingredientsList
        )
    }

    fun setSearchKeyword(searchKeyword: String) {
        _uiState.update { currentState ->
            when (currentState) {
                is RecRecipeUiState.Idle -> currentState.copy(searchKeyword = searchKeyword)
                is RecRecipeUiState.Loading -> currentState.copy(searchKeyword = searchKeyword)
                is RecRecipeUiState.Success -> currentState.copy(searchKeyword = searchKeyword)
                is RecRecipeUiState.Error -> currentState.copy(searchKeyword = searchKeyword)
            }
        }
    }

    fun setSearchKeywordList(searchKeywordList: List<String>) {
        _uiState.update { currentState ->
            when (currentState) {
                is RecRecipeUiState.Idle -> currentState.copy(searchKeywordList = searchKeywordList)
                is RecRecipeUiState.Loading -> currentState.copy(searchKeywordList = searchKeywordList)
                is RecRecipeUiState.Success -> currentState.copy(searchKeywordList = searchKeywordList)
                is RecRecipeUiState.Error -> currentState.copy(searchKeywordList = searchKeywordList)
            }
        }
    }

    fun setIngredientsList(ingredientsList: List<IngredientsModel>) {
        _uiState.update { currentState ->
            when (currentState) {
                is RecRecipeUiState.Idle -> currentState.copy(ingredientsList = ingredientsList)
                is RecRecipeUiState.Loading -> currentState.copy(ingredientsList = ingredientsList)
                is RecRecipeUiState.Success -> currentState.copy(ingredientsList = ingredientsList)
                is RecRecipeUiState.Error -> currentState.copy(ingredientsList = ingredientsList)
            }
        }
    }


    fun getIngredients(isIngredients: Boolean = true) {
        val currentState = _uiState.value
        if (currentState.searchKeyword.isBlank()) {
            viewModelScope.launch {
                _events.emit(RecRecipeUiEvent.ShowError("검색어를 선택해주세요"))
            }
            return
        }
        _uiState.value = RecRecipeUiState.Loading(
            searchKeyword = currentState.searchKeyword,
            searchKeywordList = currentState.searchKeywordList,
            ingredientsList = currentState.ingredientsList
        )
        val keyword = RecipePromptUtil.createIngredientsPrompt(currentState.searchKeyword)

        viewModelScope.launch(Dispatchers.IO) {
            generateRecipeUseCase(keyword)
                .onSuccess { response ->
                    if (isIngredients) {
                        val ingredientsList =
                            RecipePromptUtil.parseIngredientsResponse(response.content)
                        getRecipe(currentState.searchKeyword, ingredientsList)
                    }
                }.onFailure { exception ->
                    val errorState = RecRecipeUiState.Error(
                        searchKeyword = currentState.searchKeyword,
                        searchKeywordList = currentState.searchKeywordList,
                        ingredientsList = currentState.ingredientsList,
                        message = exception.message ?: "재료 로딩 실패"
                    )
                    _uiState.value = errorState
                    _events.emit(RecRecipeUiEvent.ShowError(errorState.message))
                }
        }
    }

    private suspend fun getRecipe(
        searchKeyword: String,
        ingredientsList: List<IngredientsModel>
    ) {
        val currentState = _uiState.value
        val recipeKeyword = RecipePromptUtil.createRecipePrompt(searchKeyword, ingredientsList)

        generateRecipeUseCase(recipeKeyword)
            .onSuccess { response ->
                val (recipeList, wellbeingRecipeList) = RecipePromptUtil.parseRecipeResponse(
                    response.content
                )
                val successState = RecRecipeUiState.Success(
                    searchKeyword = searchKeyword,
                    searchKeywordList = currentState.searchKeywordList,
                    ingredientsList = ingredientsList,
                    recipeList = recipeList,
                    wellbeingRecipeList = wellbeingRecipeList
                )
                _uiState.value = successState

                val uniteUiState = UniteUiState(
                    searchKeyword = searchKeyword,
                    ingredientsList = ingredientsList,
                    recipeList = recipeList,
                    wellbeingRecipeList = wellbeingRecipeList
                )
                _events.emit(RecRecipeUiEvent.RouteToRecipe(uniteUiState))
            }
            .onFailure { exception ->
                val errorState = RecRecipeUiState.Error(
                    searchKeyword = currentState.searchKeyword,
                    searchKeywordList = currentState.searchKeywordList,
                    ingredientsList = currentState.ingredientsList,
                    message = exception.message ?: "레시피 생성 실패"
                )
                _uiState.value = errorState
                _events.emit(RecRecipeUiEvent.ShowError(errorState.message))
            }
    }
}
