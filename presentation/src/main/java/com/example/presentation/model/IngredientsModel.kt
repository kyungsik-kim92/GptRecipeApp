package com.example.presentation.model

import android.os.Parcelable
import com.example.data.local.entity.IngredientsEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.parcelize.Parcelize

@Parcelize
data class IngredientsModel(
    val id: String,
    val ingredients: String,
    val isSelected: Boolean = false
) : Parcelable