package com.example.gptrecipeapp.ui.wellbeingrecipe

import androidx.lifecycle.ViewModel
import com.example.gptrecipeapp.WellBeingRecipeModel
import com.example.gptrecipeapp.WellBeingRecipeUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WellbeingRecipeViewModel() : ViewModel() {

    private val _uiModel = MutableStateFlow(
        WellBeingRecipeUiModel(
            wellBeingRecipeList = ArrayList()
        )
    )
    val uiModel: StateFlow<WellBeingRecipeUiModel> = _uiModel

    fun setWellBeingRecipeList(wellBeingRecipeList: ArrayList<WellBeingRecipeModel>) {
        _uiModel.value = _uiModel.value.copy().apply {
            this.wellBeingRecipeList = wellBeingRecipeList
        }
    }
}