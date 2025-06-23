package com.example.presentation.ui.recrecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GenerateRecipeUseCase
import com.example.presentation.model.IngredientsModel
import com.example.presentation.model.UniteUiModel
import com.example.presentation.model.WellbeingRecipeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import javax.inject.Inject


@HiltViewModel
class RecRecipeViewModel @Inject constructor(
    private val generateRecipeUseCase: GenerateRecipeUseCase
) : ViewModel() {
    private val _uiModel = MutableStateFlow(
        UniteUiModel(
            isFetched = false,
            isLoading = false,
            searchKeyword = "",
            searchKeywordList = emptyList(),
            ingredientsList = emptyList(),
            recipeList = emptyList(),
            wellbeingRecipeList = emptyList()
        )
    )
    val uiModel: StateFlow<UniteUiModel> = _uiModel


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
        _uiModel.value = _uiModel.value.copy(
            isLoading = true
        )
        val searchKeyword = _uiModel.value.searchKeyword
        val keyword = getFormattedSearchKeyword(searchKeyword)

        viewModelScope.launch(Dispatchers.IO) {
            generateRecipeUseCase(keyword)
                .onSuccess { response ->
                    if (isIngredients) {
                        val ingredientsList = getIngredientsList(response.content)
                        val wellbeingRecipeList = getWellBeingRecipeList(response.content)
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

    private fun getFormattedSearchKeyword(searchKeyword: String): String {
        return "${searchKeyword}(을)를 요리하기 위한 재료와 건강한 방식(wellbeing)의 레시피를 나열해줘\n" +
                "답변은 아래와 같은 형식과 한국어만으로 표시해\n" +
                "주의사항: 두개의 JSON Array 를 생성하지말고 하나의 JSON Array 로 답변을 표시해\n" +
                "[{\"재료\":\"양파\"}, {\"재료\":\"김치\"}, {\"웰빙\":\"올리브오일에 양파를 볶는다\"}, {\"웰빙\":\"저염 김치를 사용한다\"}]"
    }

    private fun getIngredientsList(content: String): List<IngredientsModel> {
        return try {
            val jsonArray = JSONArray(content)
            (0 until jsonArray.length()).mapNotNull { i ->
                val jsonObject = jsonArray.getJSONObject(i)
                if (jsonObject.has("재료")) {
                    val ingredient = jsonObject.getString("재료")
                    IngredientsModel(
                        id = "ingredient_$i",
                        ingredients = ingredient,
                        isSelected = true
                    )
                } else null
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

private fun getWellBeingRecipeList(content: String): List<WellbeingRecipeModel> {
    return try {
        val jsonArray = JSONArray(content)
        (0 until jsonArray.length()).mapNotNull { i ->
            val jsonObject = jsonArray.getJSONObject(i)
            if (jsonObject.has("웰빙")) {
                val wellbeingRecipe = jsonObject.getString("웰빙")
                WellbeingRecipeModel(
                    id = "wellbeing_$i",
                    wellbeingRecipe = wellbeingRecipe,
                    isSelected = true
                )
            } else null
        }
    } catch (e: Exception) {
        emptyList()
    }
}
