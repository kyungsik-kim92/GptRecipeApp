package com.example.presentation.ui.recingredients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GenerateRecipeUseCase
import com.example.presentation.model.IngredientsModel
import com.example.presentation.ui.common.RecipePromptUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject


@HiltViewModel
class RecIngredientsViewModel @Inject constructor(
    private val generateRecipeUseCase: GenerateRecipeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecIngredientsUiState>(RecIngredientsUiState.Idle())
    val uiState: StateFlow<RecIngredientsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RecIngredientsUiEvent>()
    val events: SharedFlow<RecIngredientsUiEvent> = _events.asSharedFlow()


    private val _categories = MutableStateFlow<Map<String, List<IngredientsModel>>>(emptyMap())
    val categories: StateFlow<Map<String, List<IngredientsModel>>> = _categories.asStateFlow()

    init {
        loadIngredients()
    }

    private fun loadIngredients() {
        _categories.value = mapOf(
            "meat" to getMeatList(),
            "seafood" to getSeafoodList(),
            "vegetable" to getVegetableList(),
            "fruit" to getFruitList(),
            "processed" to getProcessedList(),
            "etc" to getEtcList()
        )
    }

    fun getRecRecipes() {
        val selectedIngredients = getSelectedIngredients()

        if (selectedIngredients.isEmpty()) {
            viewModelScope.launch {
                _events.emit(RecIngredientsUiEvent.ShowError("재료를 선택해주세요"))
            }
            return
        }
        val firstIngredient = selectedIngredients.firstOrNull()?.ingredients ?: "요리"
        val prompt = RecipePromptUtil.createIngredientsPrompt(firstIngredient)

        val currentState = _uiState.value
        _uiState.value = RecIngredientsUiState.Loading(
            searchKeywordList = currentState.searchKeywordList,
            ingredientsList = selectedIngredients
        )
        viewModelScope.launch {
            generateRecipeUseCase(prompt)
                .onSuccess { response ->
                    val generatedIngredients =
                        RecipePromptUtil.parseIngredientsResponse(response.content)
                    val searchKeywords = generatedIngredients.map { it.ingredients }
                    val successState = RecIngredientsUiState.Success(
                        searchKeywordList = searchKeywords,
                        ingredientsList = selectedIngredients
                    )
                    _uiState.value = successState
                    _events.emit(RecIngredientsUiEvent.RouteToRecRecipe(successState))
                }
                .onFailure { exception ->
                    val errorState = RecIngredientsUiState.Error(
                        searchKeywordList = currentState.searchKeywordList,
                        ingredientsList = selectedIngredients,
                        message = exception.message ?: "레시피 검색 실패"
                    )
                    _uiState.value = errorState
                    _events.emit(RecIngredientsUiEvent.ShowError(errorState.message))
                }
        }
    }

    fun toggleIngredientSelection(categoryType: String, ingredientId: String) {
        _categories.update { currentCategories ->
            currentCategories.mapValues { (category, ingredients) ->
                if (category == categoryType) {
                    ingredients.map { ingredient ->
                        if (ingredient.id == ingredientId) {
                            ingredient.copy(isSelected = !ingredient.isSelected)
                        } else ingredient
                    }
                } else ingredients
            }
        }
    }

    fun getSelectedIngredients(): List<IngredientsModel> {
        return _categories.value.values
            .flatten()
            .filter { it.isSelected }
    }

    val meatList: StateFlow<List<IngredientsModel>> = _categories
        .map { it["meat"] ?: emptyList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val seafoodList: StateFlow<List<IngredientsModel>> = _categories
        .map { it["seafood"] ?: emptyList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val vegetableList: StateFlow<List<IngredientsModel>> = _categories
        .map { it["vegetable"] ?: emptyList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val fruitList: StateFlow<List<IngredientsModel>> = _categories
        .map { it["fruit"] ?: emptyList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val processedList: StateFlow<List<IngredientsModel>> = _categories
        .map { it["processed"] ?: emptyList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val etcList: StateFlow<List<IngredientsModel>> = _categories
        .map { it["etc"] ?: emptyList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun getMeatList(): List<IngredientsModel> {
        return listOf(
            IngredientsModel(id = "chicken", ingredients = "닭고기", isSelected = false),
            IngredientsModel(id = "pork", ingredients = "돼지고기", isSelected = false),
            IngredientsModel(id = "beef", ingredients = "소고기", isSelected = false),
            IngredientsModel(id = "lamb", ingredients = "양고기", isSelected = false),
            IngredientsModel(id = "duck", ingredients = "오리고기", isSelected = false)
        )
    }

    private fun getSeafoodList(): List<IngredientsModel> {
        return listOf(
            IngredientsModel(id = "abalone", ingredients = "전복", isSelected = false),
            IngredientsModel(id = "oyster", ingredients = "굴", isSelected = false),
            IngredientsModel(id = "crab", ingredients = "꽃게", isSelected = false),
            IngredientsModel(id = "small_octopus", ingredients = "낙지", isSelected = false),
            IngredientsModel(id = "webfoot_octopus", ingredients = "쭈꾸미", isSelected = false),
            IngredientsModel(id = "octopus", ingredients = "문어", isSelected = false),
            IngredientsModel(id = "squid", ingredients = "오징어", isSelected = false),
            IngredientsModel(id = "seaweed", ingredients = "미역", isSelected = false),
            IngredientsModel(id = "shrimp", ingredients = "새우", isSelected = false),
            IngredientsModel(id = "gizzard_shad", ingredients = "전어", isSelected = false),
            IngredientsModel(id = "salmon", ingredients = "연어", isSelected = false),
            IngredientsModel(id = "hairtail", ingredients = "갈치", isSelected = false),
            IngredientsModel(id = "saury", ingredients = "꽁치", isSelected = false),
            IngredientsModel(id = "mackerel", ingredients = "고등어", isSelected = false),
            IngredientsModel(id = "anchovy", ingredients = "멸치", isSelected = false),
            IngredientsModel(id = "clam", ingredients = "조개", isSelected = false),
            IngredientsModel(id = "manila_clam", ingredients = "바지락", isSelected = false),
            IngredientsModel(id = "cockle", ingredients = "꼬막", isSelected = false),
            IngredientsModel(id = "sea_snail", ingredients = "골뱅이", isSelected = false)
        )
    }

    private fun getVegetableList(): List<IngredientsModel> {
        return listOf(
            IngredientsModel(id = "kimchi", ingredients = "김치", isSelected = false),
            IngredientsModel(id = "napa_cabbage", ingredients = "배추", isSelected = false),
            IngredientsModel(id = "cabbage", ingredients = "양배추", isSelected = false),
            IngredientsModel(id = "lettuce", ingredients = "상추", isSelected = false),
            IngredientsModel(id = "perilla_leaves", ingredients = "깻잎", isSelected = false),
            IngredientsModel(id = "spinach", ingredients = "시금치", isSelected = false),
            IngredientsModel(id = "green_onion", ingredients = "대파", isSelected = false),
            IngredientsModel(id = "eggplant", ingredients = "가지", isSelected = false),
            IngredientsModel(id = "radish", ingredients = "무", isSelected = false),
            IngredientsModel(id = "garlic", ingredients = "마늘", isSelected = false),
            IngredientsModel(id = "onion", ingredients = "양파", isSelected = false),
            IngredientsModel(id = "mushroom", ingredients = "버섯", isSelected = false),
            IngredientsModel(id = "bean_sprouts", ingredients = "콩나물", isSelected = false),
            IngredientsModel(id = "tomato", ingredients = "토마토", isSelected = false),
            IngredientsModel(id = "paprika", ingredients = "파프리카", isSelected = false),
            IngredientsModel(id = "tofu", ingredients = "두부", isSelected = false),
            IngredientsModel(id = "bean", ingredients = "콩", isSelected = false),
            IngredientsModel(id = "potato", ingredients = "감자", isSelected = false),
            IngredientsModel(id = "sweet_potato", ingredients = "고구마", isSelected = false),
            IngredientsModel(id = "corn", ingredients = "옥수수", isSelected = false)
        )
    }

    private fun getFruitList(): List<IngredientsModel> {
        return listOf(
            IngredientsModel(id = "apple", ingredients = "사과", isSelected = false),
            IngredientsModel(id = "banana", ingredients = "바나나", isSelected = false),
            IngredientsModel(id = "pear", ingredients = "배", isSelected = false),
            IngredientsModel(id = "peach", ingredients = "복숭아", isSelected = false),
            IngredientsModel(id = "strawberry", ingredients = "딸기", isSelected = false),
            IngredientsModel(id = "grape", ingredients = "포도", isSelected = false),
            IngredientsModel(id = "mango", ingredients = "망고", isSelected = false),
            IngredientsModel(id = "blueberry", ingredients = "블루베리", isSelected = false),
            IngredientsModel(id = "pineapple", ingredients = "파인애플", isSelected = false),
            IngredientsModel(id = "lemon", ingredients = "레몬", isSelected = false),
            IngredientsModel(id = "lime", ingredients = "라임", isSelected = false),
            IngredientsModel(id = "avocado", ingredients = "아보카도", isSelected = false)
        )
    }

    private fun getProcessedList(): List<IngredientsModel> {
        return listOf(
            IngredientsModel(id = "egg", ingredients = "계란", isSelected = false),
            IngredientsModel(id = "quail_egg", ingredients = "메추리알", isSelected = false),
            IngredientsModel(id = "cheese", ingredients = "치즈", isSelected = false),
            IngredientsModel(id = "yogurt", ingredients = "요거트", isSelected = false),
            IngredientsModel(id = "spam", ingredients = "스팸", isSelected = false),
            IngredientsModel(id = "ham", ingredients = "햄", isSelected = false),
            IngredientsModel(id = "bacon", ingredients = "베이컨", isSelected = false),
            IngredientsModel(id = "sausage", ingredients = "소시지", isSelected = false),
            IngredientsModel(id = "dumpling", ingredients = "만두", isSelected = false),
            IngredientsModel(id = "sundae", ingredients = "순대", isSelected = false),
            IngredientsModel(id = "canned_tuna", ingredients = "참치캔", isSelected = false)
        )
    }

    private fun getEtcList(): List<IngredientsModel> {
        return listOf(
            IngredientsModel(id = "ramen", ingredients = "라면", isSelected = false),
            IngredientsModel(id = "spaghetti", ingredients = "스파게티면", isSelected = false),
            IngredientsModel(id = "somen", ingredients = "소면", isSelected = false),
            IngredientsModel(id = "starch_noodle", ingredients = "당면", isSelected = false),
            IngredientsModel(id = "udon", ingredients = "우동면", isSelected = false),
            IngredientsModel(id = "sujebi", ingredients = "수제비", isSelected = false),
            IngredientsModel(id = "garae_tteok", ingredients = "가래떡", isSelected = false),
            IngredientsModel(id = "soup_tteok", ingredients = "떡국떡", isSelected = false),
            IngredientsModel(id = "baguette", ingredients = "바게트", isSelected = false),
            IngredientsModel(id = "bagel", ingredients = "베이글", isSelected = false),
            IngredientsModel(id = "bread", ingredients = "식빵", isSelected = false)
        )
    }
}

