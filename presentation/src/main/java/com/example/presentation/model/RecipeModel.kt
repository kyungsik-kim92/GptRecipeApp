package com.example.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RecipeModel(
    val id: String,
    val recipe: String,
    val isSelected: Boolean = false
) : Parcelable