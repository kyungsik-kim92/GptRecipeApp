package com.example.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WellbeingRecipeModel(
    val id: String,
    val wellbeingRecipe: String,
    val isSelected: Boolean = false
) : Parcelable