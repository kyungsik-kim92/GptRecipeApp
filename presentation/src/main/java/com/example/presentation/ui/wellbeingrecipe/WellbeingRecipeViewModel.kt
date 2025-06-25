package com.example.presentation.ui.wellbeingrecipe

import androidx.lifecycle.ViewModel
import com.example.presentation.model.WellbeingRecipeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WellbeingRecipeViewModel @Inject constructor() : ViewModel() {

    private val _uiModel = MutableStateFlow(
        WellbeingRecipeUiState(
            wellBeingRecipeList = emptyList()
        )
    )
    val uiModel: StateFlow<WellbeingRecipeUiState> = _uiModel

    fun setWellBeingRecipeList(wellBeingRecipeList: List<WellbeingRecipeModel>) {
        _uiModel.value = _uiModel.value.copy().apply {
            this.wellBeingRecipeList = wellBeingRecipeList
        }
    }
}