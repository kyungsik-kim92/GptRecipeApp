package com.example.domain.usecase

import com.example.domain.model.ShoppingItem
import com.example.domain.repo.Repository
import javax.inject.Inject
import kotlin.collections.map

class GenerateShoppingListUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(recipeId: Long) {
        val recipe = repository.findRecipe(recipeId)
            ?: throw IllegalArgumentException("레시피를 찾을 수 없습니다.")

        val shoppingItems = recipe.ingredientsList.map { ingredient ->
            ShoppingItem(
                id = 0L,
                name = parseIngredientName(ingredient.ingredients),
                quantity = parseQuantity(ingredient.ingredients),
                category = categorizeIngredient(ingredient.ingredients),
                isChecked = false,
                recipeName = recipe.searchKeyword
            )
        }
        repository.insertShoppingItems(shoppingItems)
    }


    private fun parseIngredientName(ingredientText: String): String {
        return ingredientText.split(" ").firstOrNull() ?: ingredientText
    }

    private fun parseQuantity(ingredientText: String): String {
        val parts = ingredientText.split(" ")
        return if (parts.size > 1) parts.drop(1).joinToString(" ") else "적당량"
    }

    private fun categorizeIngredient(ingredientText: String): String {
        val meatKeywords = listOf("소고기", "돼지고기", "닭고기", "양고기", "고기", "삼겹살", "목살")
        val seafoodKeywords = listOf("생선", "새우", "조개", "오징어", "문어", "참치", "연어")
        val vegetableKeywords = listOf("양파", "마늘", "당근", "감자", "배추", "파", "고추", "상추", "깻잎")
        val dairyKeywords = listOf("우유", "치즈", "버터", "요거트", "계란", "달걀")
        val seasoningKeywords = listOf("간장", "소금", "설탕", "식초", "고춧가루", "된장", "고추장", "참기름")

        return when {
            meatKeywords.any { ingredientText.contains(it) } -> "육류"
            seafoodKeywords.any { ingredientText.contains(it) } -> "해산물"
            vegetableKeywords.any { ingredientText.contains(it) } -> "채소"
            dairyKeywords.any { ingredientText.contains(it) } -> "유제품"
            seasoningKeywords.any { ingredientText.contains(it) } -> "조미료"
            else -> "기타"
        }
    }
}