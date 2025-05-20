package com.example.gptrecipeapp.recipe

import androidx.lifecycle.ViewModel
import com.example.gptrecipeapp.RecipeUiModel
import com.example.gptrecipeapp.Repository
import com.example.gptrecipeapp.model.IngredientsModel
import com.example.gptrecipeapp.model.RecipeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RecipeViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiModel = MutableStateFlow(
        RecipeUiModel(
            id = 0,
            searchKeyword = "",
            ingredientsList = ArrayList(),
            recipeList = ArrayList(),
        )
    )
    val uiModel: StateFlow<RecipeUiModel> = _uiModel

    fun setSearchKeyword(searchKeyword: String) {
        _uiModel.value = _uiModel.value.copy().apply {
            this.searchKeyword = searchKeyword
        }
    }

    fun setIngredientsList(ingredientsList: ArrayList<IngredientsModel>) {
        _uiModel.value = _uiModel.value.copy().apply {
            this.ingredientsList = ingredientsList
        }
    }

    fun setRecipeList(recipeList: ArrayList<RecipeModel>) {
        _uiModel.value = _uiModel.value.copy().apply {
            this.recipeList = recipeList
        }
    }
}