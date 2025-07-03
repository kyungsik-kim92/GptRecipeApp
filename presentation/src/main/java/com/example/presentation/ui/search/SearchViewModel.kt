package com.example.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GenerateRecipeUseCase
import com.example.presentation.model.IngredientsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
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
            val keyword = getFormattedSearchKeyword(searchKeyword)

            generateRecipeUseCase(keyword)
                .onSuccess { response ->
                    val ingredientsList = getIngredientsList(response.content)
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

    private fun getFormattedSearchKeyword(searchKeyword: String): String {
        return "${searchKeyword}(을)를 요리하기 위한 재료를 정확하고 완전하게 나열해줘\n" +
                "다음 조건을 반드시 지켜서 답변해:\n" +
                "1. ${searchKeyword}의 핵심 재료는 반드시 포함할것\n" +
                "2. 일반적으로 사용되는 기본 재료들을 모두 포함할것\n" +
                "3. 최소 8개 이상의 재료를 제시할것\n" +
                "4. 조미료와 양념류도 포함할것\n" +
                "답변은 아래와 같은 형식과 한국어만으로 표시해\n" +
                "[{\"재료\":\"양파\"}, {\"재료\":\"김치\"}, {\"재료\":\"돼지고기\"}, {\"재료\":\"마늘\"}, {\"재료\":\"대파\"}, {\"재료\":\"고춧가루\"}, {\"재료\":\"간장\"}, {\"재료\":\"참기름\"}]"
    }

    private fun getIngredientsList(response: String): List<IngredientsModel> {
        return try {
            val jsonArray = JSONArray(response)
            (0 until jsonArray.length()).mapIndexed { index, i ->
                val jsonObject = jsonArray.getJSONObject(i)
                if (jsonObject.has("재료")) {
                    val ingredientName = jsonObject.getString("재료")
                    IngredientsModel(
                        id = "search_ingredient_$index",
                        ingredients = ingredientName,
                        isSelected = true
                    )
                } else null
            }.filterNotNull()
        } catch (e: Exception) {
            emptyList()
        }
    }
}