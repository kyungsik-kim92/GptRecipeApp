package com.example.presentation.ui.wellbeingrecipe

import androidx.lifecycle.ViewModel
import com.example.presentation.model.WellbeingRecipeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WellbeingRecipeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<WellbeingRecipeUiState>(WellbeingRecipeUiState.Loading)
    val uiState: StateFlow<WellbeingRecipeUiState> = _uiState.asStateFlow()

    fun setWellBeingRecipeList(wellBeingRecipeList: List<WellbeingRecipeModel>) {
        _uiState.value = if (wellBeingRecipeList.isNotEmpty()) {
            WellbeingRecipeUiState.Success(wellBeingRecipeList)
        } else {
            WellbeingRecipeUiState.Error("웰빙 레시피가 없습니다")
        }
    }
}