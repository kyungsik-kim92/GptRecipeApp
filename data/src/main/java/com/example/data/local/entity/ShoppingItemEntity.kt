package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val quantity: String,
    val category: String = "기타",
    val isChecked: Boolean = false,
    val recipeName: String = "",
    val shoppingListId: Long = 0L
)