package com.example.gptrecipeapp.ui.recrecipe

import androidx.lifecycle.ViewModel
import com.example.gptrecipeapp.RecRecipeUiModel
import com.example.gptrecipeapp.Repository
import com.example.gptrecipeapp.model.GPT
import com.example.gptrecipeapp.model.IngredientsModel
import com.example.gptrecipeapp.model.RecipeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONArray

class RecRecipeViewModel(
    private val repository: Repository
) : ViewModel() {
    private val _uiModel = MutableStateFlow(
        RecRecipeUiModel(
            isFetched = false,
            isLoading = false,
            searchKeyword = "",
            searchKeywordList = ArrayList(),
            ingredientsList = ArrayList(),
            recipeList = ArrayList(),
        )
    )
    val uiModel: StateFlow<RecRecipeUiModel> = _uiModel


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
        return "$format 재료들로 ${searchKeyword}(을)를 요리하기 위한 순서를 일반적인 방식(레시피)과 건강한 방식(웰빙)을 나열해줘\n" +
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

}