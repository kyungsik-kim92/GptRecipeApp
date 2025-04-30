package com.example.gptrecipeapp

import com.example.gptrecipeapp.model.IngredientsModel

data class SearchUiModel(
    var searchKeyword: String,
    var isFetched: Boolean,
    var isLoading: Boolean,
    var ingredientsList: ArrayList<IngredientsModel>,
)
