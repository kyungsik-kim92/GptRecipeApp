package com.example.gptrecipeapp.ui.recingredients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gptrecipeapp.GptRequestParam
import com.example.gptrecipeapp.MessageRequestParam
import com.example.gptrecipeapp.RecIngredientsUiModel
import com.example.gptrecipeapp.Repository
import com.example.gptrecipeapp.model.GPT
import com.example.gptrecipeapp.model.IngredientsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class RecIngredientsViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiModel = MutableStateFlow(
        RecIngredientsUiModel(
            isLoading = false,
            isFetched = false,
            searchKeywordList = ArrayList(),
            ingredientsList = ArrayList()
        )
    )
    val uiModel: StateFlow<RecIngredientsUiModel> = _uiModel

    private val _meatList = MutableStateFlow<List<IngredientsModel>>(emptyList())
    val meatList = _meatList.asStateFlow()

    private val _seafoodList = MutableStateFlow<List<IngredientsModel>>(emptyList())
    val seafoodList = _seafoodList.asStateFlow()

    private val _vegetableList = MutableStateFlow<List<IngredientsModel>>(emptyList())
    val vegetableList = _vegetableList.asStateFlow()

    private val _fruitList = MutableStateFlow<List<IngredientsModel>>(emptyList())
    val fruitList = _fruitList.asStateFlow()

    private val _processedList = MutableStateFlow<List<IngredientsModel>>(emptyList())
    val processedList = _processedList.asStateFlow()

    private val _etcList = MutableStateFlow<List<IngredientsModel>>(emptyList())
    val etcList = _etcList.asStateFlow()

    init {
        loadIngredients()
    }

    private fun loadIngredients() {
        _meatList.value = getMeatList()
        _seafoodList.value = getSeafoodList()
        _vegetableList.value = getVegetableList()
        _fruitList.value = getFruitList()
        _processedList.value = getProcessedList()
        _etcList.value = getEtcList()
    }

    fun getRecRecipes(ingredientsList: ArrayList<IngredientsModel>) {
        _uiModel.value = _uiModel.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    repository.getGptResponse(
                        GptRequestParam(
                            messages = arrayListOf(
                                MessageRequestParam(
                                    role = "user",
                                    content = getFormattedSearchKeyword(ingredientsList)
                                )
                            )
                        )
                    )
                }
                _uiModel.value = _uiModel.value.copy(
                    isLoading = false,
                    isFetched = true,
                    searchKeywordList = getSearchKeywordList(response),
                    ingredientsList = ingredientsList
                )
            } catch (e: Exception) {
                _uiModel.value = _uiModel.value.copy(isLoading = false)
            }
        }
    }

    private fun getMeatList(): MutableList<IngredientsModel> {
        return mutableListOf(
            IngredientsModel(ingredients = "닭고기", initialIsSelected = false),
            IngredientsModel(ingredients = "돼지고기", initialIsSelected = false),
            IngredientsModel(ingredients = "소고기", initialIsSelected = false),
            IngredientsModel(ingredients = "양고기", initialIsSelected = false),
            IngredientsModel(ingredients = "오리고기", initialIsSelected = false)
        )
    }

    private fun getSeafoodList(): MutableList<IngredientsModel> {
        return mutableListOf(
            IngredientsModel(ingredients = "전복", initialIsSelected = false),
            IngredientsModel(ingredients = "굴", initialIsSelected = false),
            IngredientsModel(ingredients = "꽃게", initialIsSelected = false),
            IngredientsModel(ingredients = "낙지", initialIsSelected = false),
            IngredientsModel(ingredients = "쭈꾸미", initialIsSelected = false),
            IngredientsModel(ingredients = "문어", initialIsSelected = false),
            IngredientsModel(ingredients = "오징어", initialIsSelected = false),
            IngredientsModel(ingredients = "미역", initialIsSelected = false),
            IngredientsModel(ingredients = "새우", initialIsSelected = false),
            IngredientsModel(ingredients = "전어", initialIsSelected = false),
            IngredientsModel(ingredients = "연어", initialIsSelected = false),
            IngredientsModel(ingredients = "갈치", initialIsSelected = false),
            IngredientsModel(ingredients = "꽁치", initialIsSelected = false),
            IngredientsModel(ingredients = "고등어", initialIsSelected = false),
            IngredientsModel(ingredients = "멸치", initialIsSelected = false),
            IngredientsModel(ingredients = "조개", initialIsSelected = false),
            IngredientsModel(ingredients = "바지락", initialIsSelected = false),
            IngredientsModel(ingredients = "꼬막", initialIsSelected = false),
            IngredientsModel(ingredients = "골뱅이", initialIsSelected = false),
        )
    }

    private fun getVegetableList(): MutableList<IngredientsModel> {
        return mutableListOf(
            IngredientsModel(ingredients = "김치", initialIsSelected = false),
            IngredientsModel(ingredients = "배추", initialIsSelected = false),
            IngredientsModel(ingredients = "양배추", initialIsSelected = false),
            IngredientsModel(ingredients = "상추", initialIsSelected = false),
            IngredientsModel(ingredients = "깻잎", initialIsSelected = false),
            IngredientsModel(ingredients = "시금치", initialIsSelected = false),
            IngredientsModel(ingredients = "대파", initialIsSelected = false),
            IngredientsModel(ingredients = "가지", initialIsSelected = false),
            IngredientsModel(ingredients = "무", initialIsSelected = false),
            IngredientsModel(ingredients = "대파", initialIsSelected = false),
            IngredientsModel(ingredients = "마늘", initialIsSelected = false),
            IngredientsModel(ingredients = "양파", initialIsSelected = false),
            IngredientsModel(ingredients = "버섯", initialIsSelected = false),
            IngredientsModel(ingredients = "마늘", initialIsSelected = false),
            IngredientsModel(ingredients = "콩나물", initialIsSelected = false),
            IngredientsModel(ingredients = "토마토", initialIsSelected = false),
            IngredientsModel(ingredients = "파프리카", initialIsSelected = false),
            IngredientsModel(ingredients = "두부", initialIsSelected = false),
            IngredientsModel(ingredients = "콩", initialIsSelected = false),
            IngredientsModel(ingredients = "감자", initialIsSelected = false),
            IngredientsModel(ingredients = "고구마", initialIsSelected = false),
            IngredientsModel(ingredients = "옥수수", initialIsSelected = false),
        )
    }

    private fun getFruitList(): MutableList<IngredientsModel> {
        return mutableListOf(
            IngredientsModel(ingredients = "사과", initialIsSelected = false),
            IngredientsModel(ingredients = "바나나", initialIsSelected = false),
            IngredientsModel(ingredients = "배", initialIsSelected = false),
            IngredientsModel(ingredients = "복숭아", initialIsSelected = false),
            IngredientsModel(ingredients = "딸기", initialIsSelected = false),
            IngredientsModel(ingredients = "포도", initialIsSelected = false),
            IngredientsModel(ingredients = "망고", initialIsSelected = false),
            IngredientsModel(ingredients = "블루베리", initialIsSelected = false),
            IngredientsModel(ingredients = "파인애플", initialIsSelected = false),
            IngredientsModel(ingredients = "레몬", initialIsSelected = false),
            IngredientsModel(ingredients = "라임", initialIsSelected = false),
            IngredientsModel(ingredients = "아보카도"),
        )
    }

    private fun getProcessedList(): MutableList<IngredientsModel> {
        return mutableListOf(
            IngredientsModel(ingredients = "계란", initialIsSelected = false),
            IngredientsModel(ingredients = "메추리알", initialIsSelected = false),
            IngredientsModel(ingredients = "치즈", initialIsSelected = false),
            IngredientsModel(ingredients = "요거트", initialIsSelected = false),
            IngredientsModel(ingredients = "스팸", initialIsSelected = false),
            IngredientsModel(ingredients = "햄", initialIsSelected = false),
            IngredientsModel(ingredients = "베이컨", initialIsSelected = false),
            IngredientsModel(ingredients = "소시지", initialIsSelected = false),
            IngredientsModel(ingredients = "만두", initialIsSelected = false),
            IngredientsModel(ingredients = "순대", initialIsSelected = false),
            IngredientsModel(ingredients = "참치캔", initialIsSelected = false),
        )
    }

    private fun getEtcList(): MutableList<IngredientsModel> {
        return mutableListOf(
            IngredientsModel(ingredients = "라면", initialIsSelected = false),
            IngredientsModel(ingredients = "스파게티면", initialIsSelected = false),
            IngredientsModel(ingredients = "소면", initialIsSelected = false),
            IngredientsModel(ingredients = "당면", initialIsSelected = false),
            IngredientsModel(ingredients = "우동면", initialIsSelected = false),
            IngredientsModel(ingredients = "수제비", initialIsSelected = false),
            IngredientsModel(ingredients = "가래떡", initialIsSelected = false),
            IngredientsModel(ingredients = "떡국떡", initialIsSelected = false),
            IngredientsModel(ingredients = "바게트", initialIsSelected = false),
            IngredientsModel(ingredients = "베이글", initialIsSelected = false),
            IngredientsModel(ingredients = "식빵", initialIsSelected = false),
        )
    }

    private fun getFormattedSearchKeyword(
        ingredientsList: ArrayList<IngredientsModel>,
    ): String {

        var format = ""
        ingredientsList.forEachIndexed { index, ingredientsModel ->
            format += if (index == ingredientsList.lastIndex) {
                ingredientsModel.ingredients
            } else {
                "${ingredientsModel.ingredients},"
            }
        }
        return "$format 재료들로 조리할 수 있는 음식을 나열해줘\n" +
                "답변은 아래와 같은 형식과 한국어만으로 표시해\n" +
                "[{\"키워드\":\"김치찌개\"}, {\"키워드\":\"된장찌개\"}]"
    }

    private fun getSearchKeywordList(response: GPT): ArrayList<String> {
        val searchKeywordList = ArrayList<String>()
        val jsonArray = response.choices[0].message.content?.let { JSONArray(it) }

        for (i in 0 until (jsonArray?.length() ?: 0)) {
            jsonArray?.getJSONObject(i)?.apply {
                if (has("키워드")) {
                    searchKeywordList.add(get("키워드").toString())
                }
            }
        }
        return searchKeywordList
    }
}
