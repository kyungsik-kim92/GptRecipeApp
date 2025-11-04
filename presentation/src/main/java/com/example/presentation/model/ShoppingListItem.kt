package com.example.presentation.model

data class ShoppingItemModel(
    val id: Long,
    val name: String,
    val quantity: String,
    val category: String,
    val isChecked: Boolean,
    val recipeName: String
)