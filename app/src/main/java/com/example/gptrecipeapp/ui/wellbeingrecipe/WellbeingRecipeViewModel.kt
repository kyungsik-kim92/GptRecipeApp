package com.example.gptrecipeapp.ui.wellbeingrecipe

import androidx.lifecycle.ViewModel
import com.example.gptrecipeapp.WellbeingRecipeModel
import com.example.gptrecipeapp.WellbeingRecipeUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WellbeingRecipeViewModel() : ViewModel() {

    private val _uiModel = MutableStateFlow(
        WellbeingRecipeUiModel(
            wellBeingRecipeList = ArrayList()
        )
    )
    val uiModel: StateFlow<WellbeingRecipeUiModel> = _uiModel

    fun setWellBeingRecipeList(wellBeingRecipeList: ArrayList<WellbeingRecipeModel>) {
        _uiModel.value = _uiModel.value.copy().apply {
            this.wellBeingRecipeList = wellBeingRecipeList
        }
    }
}