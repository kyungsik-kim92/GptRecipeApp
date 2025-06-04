package com.example.gptrecipeapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gptrecipeapp.FavoriteUiModel
import com.example.gptrecipeapp.Repository
import com.example.gptrecipeapp.room.entity.toFavoriteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiModel = MutableStateFlow(
        FavoriteUiModel(
            favoriteList = ArrayList(),
            isLoading = false
        )
    )
    val uiModel: StateFlow<FavoriteUiModel> = _uiModel

    init {
        getFavoriteList()
    }

    fun getFavoriteList() {
        _uiModel.value = _uiModel.value.copy(isLoading = true)

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val recipeList = repository.getAll()

                withContext(Dispatchers.Main) {
                    _uiModel.value = FavoriteUiModel(
                        favoriteList = ArrayList(recipeList.map { it.toFavoriteModel() }),
                        isLoading = false
                    )
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    _uiModel.value = _uiModel.value.copy(isLoading = false)
                }
            }
        }
    }

}