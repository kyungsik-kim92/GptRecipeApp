package com.example.presentation.ui.wellbeingrecipe

import androidx.lifecycle.ViewModel
import com.example.presentation.model.WellbeingRecipeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WellbeingRecipeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(
        WellbeingRecipeUiState(
            wellBeingRecipeList = emptyList()
        )
    )
    val uiModel: StateFlow<WellbeingRecipeUiState> = _uiState

    fun setWellBeingRecipeList(wellBeingRecipeList: List<WellbeingRecipeModel>) {
        _uiState.value = _uiState.value.copy().apply {
            this.wellBeingRecipeList = wellBeingRecipeList
        }
    }
}