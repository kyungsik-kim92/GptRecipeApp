package com.example.presentation.ui.wellbeingrecipe

import androidx.lifecycle.ViewModel
import com.example.presentation.model.WellbeingRecipeModel
import com.example.presentation.model.WellbeingRecipeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WellbeingRecipeViewModel @Inject constructor() : ViewModel() {

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