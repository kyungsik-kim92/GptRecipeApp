package com.example.gptrecipeapp.ui.searchingredients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gptrecipeapp.GptRequestParam
import com.example.gptrecipeapp.MessageRequestParam
import com.example.gptrecipeapp.Repository
import com.example.gptrecipeapp.UniteUiModel
import com.example.gptrecipeapp.WellbeingRecipeModel
import com.example.gptrecipeapp.model.GPT
import com.example.gptrecipeapp.model.IngredientsModel
import com.example.gptrecipeapp.model.RecipeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

@HiltViewModel
class SearchIngredientsViewModel(private val repository: Repository) : ViewModel() {

    private val _originalIngredientsList =
        MutableStateFlow<ArrayList<IngredientsModel>>(ArrayList())

    private val _uiModel = MutableStateFlow(
        UniteUiModel(
            isFetched = false,
            isLoading = false,
            searchKeyword = "",
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

    fun setIngredientsList(ingredientsList: ArrayList<IngredientsModel>) {
        _originalIngredientsList.value = ArrayList(ingredientsList)
        _uiModel.value = _uiModel.value.copy().apply {
            this.ingredientsList = ingredientsList
        }
    }

    fun getRecipeByIngredients(
        ingredientsList: ArrayList<IngredientsModel>
    ) {
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
                                content = getFormattedSearchKeyword(searchKeyword, ingredientsList)
                            )
                        )
                    )
                )

                withContext(Dispatchers.Main) {
                    _uiModel.value = _uiModel.value.copy(
                        searchKeyword = searchKeyword,
                        isFetched = true,
                        isLoading = false,
                        ingredientsList = _originalIngredientsList.value,
                        recipeList = getRecipeList(response),
                        wellbeingRecipeList = getWellBeingRecipeList(response)
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

    private fun getFormattedSearchKeyword(
        searchKeyword: String,
        ingredientsList: ArrayList<IngredientsModel>,
    ): String {

        var format = ""
        ingredientsList.forEachIndexed { index, ingredientsModel ->
            format += if (index == ingredientsList.lastIndex) {
                ingredientsModel.ingredients
            } else {
                "${ingredientsModel.ingredients},"
            }
        }
        return "$format 재료들로 ${searchKeyword}(을)를 요리하기 위한 순서를 일반적인 방식(recipe)과 건강한 방식(wellbeing)을 나열해줘\n" +
                "답변은 아래와 같은 형식과 한국어만으로 표시해\n" +
                "주의사항: 두개의 JSON Array 를 생성하지말고 하나의 JSON Array 로 답변을 표시해\n" +
                "[{\"레시피\":\"기름에 돼지고기를 볶는다\"}, {\"레시피\":\"물을 붓는다\"}, {\"웰빙\":\"따뜻한 물을 붓는다\"}, {\"웰빙\":\"땅콩을 갈아 넣는다\"}]"
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