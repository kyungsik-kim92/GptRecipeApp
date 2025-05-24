package com.example.gptrecipeapp.ui.wellbeingrecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gptrecipeapp.WellBeingRecipeUiModel
import kotlinx.coroutines.flow.MutableStateFlow

class WellbeingRecipeViewModel() : ViewModel() {

    private val _uiModel = MutableStateFlow<WellBeingRecipeUiModel>().apply {
        value = WellBeingRecipeUiModel(
            wellBeingRecipeList = ArrayList(),
        )
    }
    val uiModel: LiveData<WellBeingRecipeUiModel> = _uiModel

    fun setWellBeingRecipeList(wellBeingRecipeList: ArrayList<WellBeingRecipeModel>) {
        _uiModel.value = _uiModel.value?.copy().apply {
            this?.wellBeingRecipeList = wellBeingRecipeList
        }
    }
}