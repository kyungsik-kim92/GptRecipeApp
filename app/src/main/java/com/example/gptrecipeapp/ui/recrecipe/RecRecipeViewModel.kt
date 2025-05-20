package com.example.gptrecipeapp.ui.recrecipe

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gptrecipeapp.GptRequestParam
import com.example.gptrecipeapp.MessageRequestParam
import com.example.gptrecipeapp.Repository
import com.example.gptrecipeapp.UniteUiModel
import com.example.gptrecipeapp.model.GPT
import com.example.gptrecipeapp.model.IngredientsModel
import com.example.gptrecipeapp.model.RecipeModel
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

    fun getRecipeByIngredients() {
        _uiModel.value = _uiModel.value.copy(
            isLoading = true
        )
        val ingredientsList = _uiModel.value.searchKeyword


        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val response = repository.getGptResponse(
                    GptRequestParam(
                        messages = arrayListOf(
                            MessageRequestParam(
                                role = "user",
                                content = getFormattedSearchKeyword(ingredientsList)
                            )
                        )
                    )
                )
                withContext(Dispatchers.Main) {
                    val parsedIngredients = getIngredientsList(response)
                    _uiModel.value = _uiModel.value.copy(
                        searchKeyword = ingredientsList,
                        isFetched = true,
                        isLoading = false,
                        ingredientsList = parsedIngredients,
                        recipeList = getRecipeList(response),
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
        return "${searchKeyword}(을)를 요리하기 위한 재료를 나열해줘\n" +
                "답변은 아래와 같은 형식과 한국어만으로 표시해\n" +
                "[{\"재료\":\"양파\"}, {\"재료\":\"김치\"}]"
    }

    private fun getRecipeList(response: GPT): ArrayList<RecipeModel> {
        val recipeList = ArrayList<RecipeModel>()
        val jsonArray = response.choices[0].message.content?.let { JSONArray(it) }

        for (i in 0 until (jsonArray?.length() ?: 0)) {
            jsonArray?.getJSONObject(i)?.apply {
                if (has("레시피")) {
                    recipeList.add(
                        RecipeModel(
                            initialIsSelected = true,
                            recipe = get("레시피").toString()
                        )
                    )
                }
            }
        }
        return recipeList
    }

    private fun getIngredientsList(response: GPT): ArrayList<IngredientsModel> {
        val ingredientsList = ArrayList<IngredientsModel>()
        val jsonArray = response.choices[0].message.content?.let { JSONArray(it) }

        for (i in 0 until (jsonArray?.length() ?: 0)) {
            jsonArray?.getJSONObject(i)?.apply {
                if (has("재료")) {
                    val ingredient = get("재료").toString()
                    Log.d("RecRecipeViewModel", "Found ingredient: $ingredient")
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

}