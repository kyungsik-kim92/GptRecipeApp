package com.example.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.DeleteSearchHistoryUseCase
import com.example.domain.usecase.GenerateRecipeUseCase
import com.example.domain.usecase.GetRecentSearchesUseCase
import com.example.domain.usecase.InsertSearchHistoryUseCase
import com.example.presentation.model.SearchHistoryModel
import com.example.presentation.ui.common.RecipePromptUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val generateRecipeUseCase: GenerateRecipeUseCase,
    private val insertSearchHistoryUseCase: InsertSearchHistoryUseCase,
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase,
    private val deleteSearchHistoryUseCase: DeleteSearchHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SearchUiEvent>()
    val events: SharedFlow<SearchUiEvent> = _events.asSharedFlow()

    private val _recentSearches = MutableStateFlow<List<SearchHistoryModel>>(emptyList())
    val recentSearches: StateFlow<List<SearchHistoryModel>> = _recentSearches.asStateFlow()

    init {
        loadRecentSearches()
    }

    private fun loadRecentSearches() {
        viewModelScope.launch {
            getRecentSearchesUseCase()
                .collect { histories ->
                    _recentSearches.value = histories.map {
                        SearchHistoryModel(
                            id = it.id,
                            keyword = it.keyword,
                            searchedAt = it.searchedAt
                        )
                    }
                }
        }
    }

    fun getIngredientsByRecipe(searchKeyword: String) {
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading(searchKeyword)
            val keyword = RecipePromptUtil.createIngredientsPrompt(searchKeyword)
            insertSearchHistoryUseCase(searchKeyword)

            generateRecipeUseCase(keyword)
                .onSuccess { response ->
                    val ingredientsList =
                        RecipePromptUtil.parseIngredientsResponse(response.content)
                    val successState = SearchUiState.Success(
                        searchKeyword = searchKeyword,
                        ingredientsList = ingredientsList
                    )
                    _uiState.value = successState
                    _events.emit(SearchUiEvent.RouteToIngredients(successState))
                }
                .onFailure { exception ->
                    val errorState = SearchUiState.Error(
                        searchKeyword = searchKeyword,
                        message = exception.message ?: "Exception Message"
                    )
                    _uiState.value = errorState
                    _events.emit(SearchUiEvent.ShowError(errorState.message))
                }
        }
    }

    fun deleteSearchHistory(keyword: String) {
        viewModelScope.launch {
            deleteSearchHistoryUseCase(keyword)
        }
    }

    fun onRecentSearchClick(keyword: String) {
        getIngredientsByRecipe(keyword)
    }
}
