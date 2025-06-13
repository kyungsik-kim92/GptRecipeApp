package com.example.presentation.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.toFavoriteModel
import com.example.domain.repo.Repository
import com.example.presentation.model.FavoriteUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
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
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            repository.getAllFavoritesFlow()
                .flowOn(Dispatchers.IO)
                .catch {
                    _uiModel.value = _uiModel.value.copy(isLoading = false)
                }
                .collect { recipeList ->
                    _uiModel.value = FavoriteUiModel(
                        favoriteList = ArrayList(recipeList.map { it.toFavoriteModel() }),
                        isLoading = false
                    )
                }
        }
    }

//    fun getFavoriteList() {
//        _uiModel.value = _uiModel.value.copy(isLoading = true)
//
//        viewModelScope.launch(Dispatchers.IO) {
//            runCatching {
//                val recipeList = repository.getAll()
//
//                withContext(Dispatchers.Main) {
//                    _uiModel.value = FavoriteUiModel(
//                        favoriteList = ArrayList(recipeList.map { it.toFavoriteModel() }),
//                        isLoading = false
//                    )
//                }
//            }.onFailure {
//                withContext(Dispatchers.Main) {
//                    _uiModel.value = _uiModel.value.copy(isLoading = false)
//                }
//            }
//        }
//    }

}