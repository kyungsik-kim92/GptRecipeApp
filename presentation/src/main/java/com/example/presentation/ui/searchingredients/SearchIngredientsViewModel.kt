package com.example.presentation.ui.searchingredients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GenerateRecipeUseCase
import com.example.presentation.model.IngredientsModel
import com.example.presentation.model.RecipeModel
import com.example.presentation.model.UniteUiState
import com.example.presentation.model.WellbeingRecipeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
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

    fun getRecipeByIngredients(
        ingredientsList: List<IngredientsModel>
    ) {
        _uiModel.value = _uiModel.value.copy(isLoading = true)

        val searchKeyword = _uiModel.value.searchKeyword
        val keyword = getFormattedSearchKeyword(searchKeyword, ingredientsList)

        viewModelScope.launch {
            generateRecipeUseCase(keyword)
                .onSuccess { response ->
                    val recipeList = getRecipeList(response.content)
                    val wellbeingRecipeList = getWellBeingRecipeList(response.content)

                    _uiModel.value = _uiModel.value.copy(
                        searchKeyword = searchKeyword,
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

    private fun getFormattedSearchKeyword(
        searchKeyword: String,
        ingredientsList: List<IngredientsModel>,
    ): String {
        val ingredients = ingredientsList.joinToString(",") { it.ingredients }
        return "$ingredients 재료들로 ${searchKeyword}(을)를 요리하기 위한 순서를 정확하고 상세하게 나열해줘\n" +
                "다음 조건을 반드시 지켜서 답변해:\n" +
                "1. 일반적인 방식(레시피)과 건강한 방식(웰빙)으로 구분\n" +
                "2. 각 단계는 구체적이고 실용적으로 작성\n" +
                "3. 조리 시간과 방법을 명확하게 포함\n" +
                "4. 각 방식당 최소 6단계 이상 제시\n" +
                "5. 단계별 번호는 표시하지 않고 순서는 지켜서 내용 작성\n" +
                "답변은 아래와 같은 형식과 한국어만으로 표시해\n" +
                "주의사항: 두개의 JSON Array 를 생성하지말고 하나의 JSON Array 로 답변을 표시해\n" +
                "번호 없이 바로 내용만 작성:\n" +
                "[{\"레시피\":\"팬에 기름을 두르고 중불에서 달군다\"}, {\"레시피\":\"돼지고기를 넣고 3분간 볶는다\"}, {\"웰빙\":\"팬에 올리브오일을 살짝 두르고 약불에서 달군다\"}, {\"웰빙\":\"돼지고기 대신 닭가슴살을 사용하여 조리한다\"}]"
    }


    private fun getRecipeList(response: String): List<RecipeModel> {
        return try {
            val jsonArray = JSONArray(response)
            (0 until jsonArray.length()).mapNotNull { i ->
                val jsonObject = jsonArray.getJSONObject(i)
                if (jsonObject.has("레시피")) {
                    RecipeModel(
                        id = "recipe_$i",
                        recipe = jsonObject.getString("레시피"),
                        isSelected = true
                    )
                } else null
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun getWellBeingRecipeList(response: String): List<WellbeingRecipeModel> {
        return try {
            val jsonArray = JSONArray(response)
            (0 until jsonArray.length()).mapNotNull { i ->
                val jsonObject = jsonArray.getJSONObject(i)
                if (jsonObject.has("웰빙")) {
                    WellbeingRecipeModel(
                        id = "wellbeing_$i",
                        wellbeingRecipe = jsonObject.getString("웰빙"),
                        isSelected = true
                    )
                } else null
            }
        } catch (e: Exception) {
            emptyList()
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
}