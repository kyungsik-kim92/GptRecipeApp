package com.example.gptrecipeapp

import android.os.Parcelable
import com.example.data.database.entity.WellbeingRecipeEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.parcelize.Parcelize

@Parcelize
data class WellbeingRecipeModel(
    val wellbeingRecipe: String,
    val initialIsSelected: Boolean = false
) : Parcelable {
    private val _isSelected = MutableStateFlow(initialIsSelected)
    val isSelected: StateFlow<Boolean> = _isSelected.asStateFlow()

    fun setIsSelected(selected: Boolean) {
        _isSelected.value = selected
    }
}

fun WellbeingRecipeModel.toEntity() = WellbeingRecipeEntity(
    wellbeingRecipe = this.wellbeingRecipe,
    isSelected = this.isSelected.value
)

//fun WellbeingRecipeEntity.toModel() = WellbeingRecipeModel(
//    initialIsSelected = this.isSelected,
//    wellbeingRecipe = this.wellbeingRecipe
//)
