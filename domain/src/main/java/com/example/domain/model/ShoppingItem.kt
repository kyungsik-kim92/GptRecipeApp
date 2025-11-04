package com.example.domain.model

data class ShoppingItem(
    val id: Long,
    val name: String,
    val quantity: String,
    val category: String,
    val isChecked: Boolean,
    val recipeName: String
)