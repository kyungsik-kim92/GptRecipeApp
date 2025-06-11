package com.example.gptrecipeapp.model

import android.os.Parcelable
import com.example.data.database.entity.IngredientsEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.parcelize.Parcelize

@Parcelize
data class IngredientsModel(
    val ingredients: String,
    val initialIsSelected: Boolean = false
) : Parcelable {
    private val _isSelected = MutableStateFlow(initialIsSelected)
    val isSelected: StateFlow<Boolean> = _isSelected.asStateFlow()

    fun setIsSelected(selected: Boolean) {
        _isSelected.value = selected
    }
}

fun IngredientsModel.toEntity() = IngredientsEntity(
    ingredients = this.ingredients,
    isSelected = this.isSelected.value
)

fun IngredientsEntity.toModel() = IngredientsModel(
    ingredients = this.ingredients,
    initialIsSelected = this.isSelected
)
