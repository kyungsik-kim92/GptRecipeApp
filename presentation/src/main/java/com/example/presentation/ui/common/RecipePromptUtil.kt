package com.example.presentation.ui.common

import com.example.presentation.model.IngredientsModel
import com.example.presentation.model.RecipeModel
import com.example.presentation.model.WellbeingRecipeModel
import org.json.JSONArray

object RecipePromptUtil {

    fun createIngredientsPrompt(searchKeyword: String): String {
        return "${searchKeyword}(을)를 요리하기 위한 재료를 정확하고 완전하게 나열해줘\n" +
                "다음 조건을 반드시 지켜서 답변해:\n" +
                "1. ${searchKeyword}의 핵심 재료는 반드시 포함할것\n" +
                "2. 일반적으로 사용되는 기본 재료들을 모두 포함할것\n" +
                "3. 최소 8개 이상의 재료를 제시할것\n" +
                "4. 조미료와 양념류도 포함할것\n" +
                "답변은 아래와 같은 형식과 한국어만으로 표시해\n" +
                "[{\"재료\":\"양파\"}, {\"재료\":\"김치\"}, {\"재료\":\"돼지고기\"}, {\"재료\":\"마늘\"}, {\"재료\":\"대파\"}, {\"재료\":\"고춧가루\"}, {\"재료\":\"간장\"}, {\"재료\":\"참기름\"}]"
    }

    fun parseIngredientsResponse(response: String): List<IngredientsModel> {
        return try {
            val cleanResponse = response.trim()
                .removePrefix("```json")
                .removeSuffix("```")
                .trim()

            val jsonArray = JSONArray(cleanResponse)
            (0 until jsonArray.length()).mapNotNull { i ->
                val jsonObject = jsonArray.getJSONObject(i)
                if (jsonObject.has("재료")) {
                    val ingredient = jsonObject.getString("재료")
                    IngredientsModel(
                        id = "ingredient_$i",
                        ingredients = ingredient,
                        isSelected = true
                    )
                } else null
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun createRecipePrompt(
        searchKeyword: String,
        ingredientsList: List<IngredientsModel>
    ): String {
        val ingredients = ingredientsList.joinToString(", ") { it.ingredients }
        return "재료: $ingredients\n" +
                "요리명: $searchKeyword\n\n" +
                "위 재료들로 ${searchKeyword}을(를) 요리하기 위한 단계별 조리법을 정확하고 상세하게 나열해줘\n\n" +
                "다음 조건을 반드시 지켜서 답변해:\n" +
                "1. 일반적인 방식(레시피)과 건강한 방식(웰빙)으로 구분하여 작성\n" +
                "2. 각 단계는 구체적이고 실용적으로 작성 (예: 중불에서 3분간 볶기, 물 200ml 추가 등)\n" +
                "3. 조리 시간, 온도, 양념 분량을 명확하게 포함\n" +
                "4. 각 방식당 최소 6단계 이상 상세하게 제시\n" +
                "5. 단계별 번호는 표시하지 않고 순서를 지켜서 내용만 작성\n" +
                "6. 재료 준비부터 완성까지 모든 과정 포함\n\n" +
                "답변은 반드시 아래 JSON 형식으로만 표시하고, 다른 설명 없이 JSON만 반환해:\n" +
                "주의사항: 하나의 JSON Array로만 답변하고, 레시피와 웰빙을 구분하여 작성\n\n" +
                "[{\"레시피\":\"팬에 식용유 2큰술을 두르고 중불에서 1분간 달군다\"}, {\"레시피\":\"돼지고기 200g을 넣고 겉면이 갈색이 될 때까지 3-4분간 볶는다\"}, {\"웰빙\":\"팬에 올리브오일 1큰술만 두르고 약불에서 30초간 달군다\"}, {\"웰빙\":\"돼지고기 대신 닭가슴살 150g을 사용하여 2-3분간 조리한다\"}]"
    }

    fun parseRecipeResponse(response: String): Pair<List<RecipeModel>, List<WellbeingRecipeModel>> {
        return try {
            val cleanResponse = response.trim()
                .removePrefix("```json")
                .removeSuffix("```")
                .trim()

            val jsonArray = JSONArray(cleanResponse)

            val recipeList = mutableListOf<RecipeModel>()
            val wellbeingList = mutableListOf<WellbeingRecipeModel>()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)

                if (jsonObject.has("레시피")) {
                    recipeList.add(
                        RecipeModel(
                            id = "recipe_$i",
                            recipe = jsonObject.getString("레시피"),
                            isSelected = true
                        )
                    )
                }

                if (jsonObject.has("웰빙")) {
                    wellbeingList.add(
                        WellbeingRecipeModel(
                            id = "wellbeing_$i",
                            wellbeingRecipe = jsonObject.getString("웰빙"),
                            isSelected = true
                        )
                    )
                }
            }

            Pair(recipeList, wellbeingList)
        } catch (e: Exception) {
            Pair(emptyList(), emptyList())
        }
    }
}
