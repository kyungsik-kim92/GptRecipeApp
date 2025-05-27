package com.example.gptrecipeapp.ui.recrecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gptrecipeapp.GptRequestParam
import com.example.gptrecipeapp.MessageRequestParam
import com.example.gptrecipeapp.Repository
import com.example.gptrecipeapp.UniteUiModel
import com.example.gptrecipeapp.WellbeingRecipeModel
import com.example.gptrecipeapp.model.GPT
import com.example.gptrecipeapp.model.IngredientsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class RecRecipeViewModel(
    private val repository: Repository
) : ViewModel() {
    private val _uiModel = MutableStateFlow(
        UniteUiModel(
            isFetched = false,
            isLoading = false,
            searchKeyword = "",
            searchKeywordList = ArrayList(),
            ingredientsList = ArrayList(),
            recipeList = ArrayList(),
            wellbeingRecipeList = ArrayList()
        )
    )
    val uiModel: StateFlow<UniteUiModel> = _uiModel


    fun setSearchKeyword(searchKeyword: String) {
        _uiModel.value = _uiModel.value.copy().apply {
            this.searchKeyword = searchKeyword
        }
    }

    fun setSearchKeywordList(searchKeywordList: ArrayList<String>) {
        _uiModel.value = _uiModel.value.copy().apply {
            this.searchKeywordList = searchKeywordList
        }
    }

    fun setIngredientsList(ingredientsList: ArrayList<IngredientsModel>) {
        _uiModel.value = _uiModel.value.copy().apply {
            this.ingredientsList = ingredientsList
        }
    }

    fun getIngredients(isIngredients: Boolean = true) {
        _uiModel.value = _uiModel.value.copy(
            isLoading = true
        )
        val searchKeyword = _uiModel.value.searchKeyword

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val response = repository.getGptResponse(
                    GptRequestParam(
                        messages = arrayListOf(
                            MessageRequestParam(
                                role = "user",
                                content = getFormattedSearchKeyword(searchKeyword)
                            )
                        )
                    )
                )
                withContext(Dispatchers.Main) {
                    if (isIngredients) {
                        _uiModel.value = _uiModel.value.copy(
                            searchKeyword = searchKeyword,
                            isFetched = true,
                            isLoading = false,
                            ingredientsList = getIngredientsList(response),
                            recipeList = ArrayList(),
                            wellbeingRecipeList = getWellBeingRecipeList(response)
                        )
                    }
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

    private fun getIngredientsList(response: GPT): ArrayList<IngredientsModel> {
        val ingredientsList = ArrayList<IngredientsModel>()
        val jsonArray = response.choices[0].message.content?.let { JSONArray(it) }

        for (i in 0 until (jsonArray?.length() ?: 0)) {
            jsonArray?.getJSONObject(i)?.apply {
                if (has("재료")) {
                    val ingredient = get("재료").toString()
                    ingredientsList.add(
                        IngredientsModel(
                            ingredients = ingredient,
                            initialIsSelected = true
                        )
                    )
                }
            }
        }
        return ingredientsList
    }

    private fun getWellBeingRecipeList(response: GPT): ArrayList<WellbeingRecipeModel> {
        val wellBeingRecipeList = ArrayList<WellbeingRecipeModel>()
        val jsonArray = response.choices[0].message.content?.let { JSONArray(it) }

        for (i in 0 until (jsonArray?.length() ?: 0)) {
            jsonArray?.getJSONObject(i)?.apply {
                if (has("웰빙")) {
                    wellBeingRecipeList.add(
                        WellbeingRecipeModel(
                            initialIsSelected = true,
                            wellbeingRecipe = get("웰빙").toString()
                        )
                    )
                }
            }
        }
        return wellBeingRecipeList
    }
}