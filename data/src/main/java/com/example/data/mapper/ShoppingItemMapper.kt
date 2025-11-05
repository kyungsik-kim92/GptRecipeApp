package com.example.data.mapper

import com.example.data.local.entity.ShoppingItemEntity
import com.example.domain.model.ShoppingItem

fun ShoppingItemEntity.toShoppingItemDomain(): ShoppingItem {
    return ShoppingItem(
        id = this.id,
        name = this.name,
        quantity = this.quantity,
        category = this.category,
        isChecked = this.isChecked,
        recipeName = this.recipeName
    )
}

fun ShoppingItem.toShoppingItemEntity(): ShoppingItemEntity {
    return ShoppingItemEntity(
        id = this.id,
        name = this.name,
        quantity = this.quantity,
        category = this.category,
        isChecked = this.isChecked,
        recipeName = this.recipeName,
        shoppingListId = 0L
    )
}