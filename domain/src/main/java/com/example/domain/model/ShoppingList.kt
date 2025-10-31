package com.example.domain.model

data class ShoppingList(
    val id: Long,
    val recipeName: String,
    val items: List<ShoppingItem>,
    val createdAt: Long
)