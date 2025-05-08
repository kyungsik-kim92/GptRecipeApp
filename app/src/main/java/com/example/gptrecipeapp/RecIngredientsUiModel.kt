package com.example.gptrecipeapp

import com.example.gptrecipeapp.model.IngredientsModel

data class RecIngredientsUiModel(
    var isLoading: Boolean,
    var isFetched: Boolean,
    var searchKeywordList: ArrayList<String>,
    var ingredientsList: ArrayList<IngredientsModel>,
)