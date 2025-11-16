package com.example.presentation.mapper

import com.example.domain.model.ShoppingItem
import com.example.presentation.model.ShoppingItemModel

fun ShoppingItem.toPresentation(): ShoppingItemModel {
    return ShoppingItemModel(
        id = this.id,
        name = this.name,
        quantity = this.quantity,
        category = this.category,
        isChecked = this.isChecked,
        recipeName = this.recipeName
    )
}

fun ShoppingItemModel.toDomain(): ShoppingItem {
    return ShoppingItem(
        id = id,
        name = name,
        quantity = quantity,
        category = category,
        isChecked = isChecked,
        recipeName = recipeName
    )
}