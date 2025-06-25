package com.example.presentation.ui.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.LocalRecipe
import com.example.domain.usecase.DeleteRecipeUseCase
import com.example.domain.usecase.FindRecipeByIdUseCase
import com.example.domain.usecase.FindRecipeByNameUseCase
import com.example.domain.usecase.GenerateRecipeUseCase
import com.example.domain.usecase.InsertRecipeUseCase
import com.example.presentation.mapper.toDomain
import com.example.presentation.mapper.toPresentation
import com.example.presentation.model.IngredientsModel
import com.example.presentation.model.RecipeModel
import com.example.presentation.model.WellbeingRecipeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val generateRecipeUseCase: GenerateRecipeUseCase,
    private val insertRecipeUseCase: InsertRecipeUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase,
    private val findRecipeByIdUseCase: FindRecipeByIdUseCase,
    private val findRecipeByNameUseCase: FindRecipeByNameUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        RecipeUiState(
            id = 0L,
            searchKeyword = "",
            ingredientsList = emptyList(),
            recipeList = emptyList(),
            isLoading = false,
            isSubscribe = false,
            wellbeingRecipeModel = emptyList()
        )
    )
    val uiState: StateFlow<RecipeUiState> = _uiState

    fun setSearchKeyword(searchKeyword: String) {
        _uiState.value = _uiState.value.copy(searchKeyword = searchKeyword)
    }

    fun setIngredientsList(ingredientsList: List<IngredientsModel>) {
        _uiState.value = _uiState.value.copy(ingredientsList = ingredientsList)
    }

    fun setRecipeList(recipeList: List<RecipeModel>) {
        _uiState.value = _uiState.value.copy(recipeList = recipeList)
    }

    fun setWellBeingRecipeList(wellbeingRecipeList: List<WellbeingRecipeModel>) {
        val wellbeingRecipeModel = wellbeingRecipeList.firstOrNull()
        _uiState.value = _uiState.value.copy(wellbeingRecipeModel = wellbeingRecipeList)
    }


    fun getRecipe() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        val searchKeyword = _uiState.value.searchKeyword
        val ingredientsList = _uiState.value.ingredientsList
        val prompt = getFormattedRecipe(searchKeyword, ingredientsList)
        viewModelScope.launch(Dispatchers.IO) {
            generateRecipeUseCase(prompt)
                .onSuccess { response ->
                    val recipeList = getRecipeList(response.content)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        recipeList = recipeList
                    )
                }.onFailure {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false
                    )
                }
        }
    }


    private fun getRecipeList(content: String): List<RecipeModel> {
        return try {
            val jsonArray = JSONArray(content)
            (0 until jsonArray.length()).mapNotNull { i ->
                val jsonObject = jsonArray.getJSONObject(i)
                if (jsonObject.has("레시피")) {
                    val recipe = jsonObject.getString("레시피")
                    RecipeModel(
                        id = "recipe_$i",
                        recipe = recipe,
                        isSelected = true
                    )
                } else null
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun getFormattedRecipe(
        searchKeyword: String,
        ingredientsList: List<IngredientsModel>,
    ): String {
        val ingredients = ingredientsList.joinToString(",") { it.ingredients }
        return "$ingredients 재료들로 ${searchKeyword}(을)를 요리하기 위한 순서를 일반적인 방식(레시피)과 건강한 방식(웰빙)을 나열해줘\n" +
                "답변은 아래와 같은 형식과 한국어만으로 표시해\n" +
                "주의사항: 두개의 JSON Array 를 생성하지말고 하나의 JSON Array 로 답변을 표시해\n" +
                "[{\"레시피\":\"기름에 돼지고기를 볶는다\"}, {\"레시피\":\"물을 붓는다\"}, {\"웰빙\":\"따뜻한 물을 붓는다\"}, {\"웰빙\":\"땅콩을 갈아 넣는다\"}]"
    }

    fun insertRecipe() {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(isLoading = true)

        viewModelScope.launch(Dispatchers.IO) {
            val existingRecipe = findRecipeByNameUseCase(currentState.searchKeyword)

            if (existingRecipe != null) {
                _uiState.value = currentState.copy(
                    id = existingRecipe.id,
                    isSubscribe = true,
                    isLoading = false
                )
            } else {
                val localRecipe = createLocalRecipeFromCurrentState()

                insertRecipeUseCase(localRecipe)
                    .onSuccess { id ->
                        _uiState.value = _uiState.value.copy(
                            id = id,
                            isSubscribe = true,
                            isLoading = false
                        )
                    }
                    .onFailure { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                        )
                    }
            }
        }
    }

    private fun createLocalRecipeFromCurrentState(): LocalRecipe {
        val currentState = _uiState.value
        return LocalRecipe(
            id = currentState.id,
            searchKeyword = currentState.searchKeyword,
            ingredientsList = currentState.ingredientsList.map { it.toDomain() },
            recipeList = currentState.recipeList.map { it.toDomain() }
        )
    }

    fun deleteRecipe() {
        val currentState = _uiState.value

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            deleteRecipeUseCase(currentState.searchKeyword)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        id = 0L,
                        isSubscribe = false,
                        isLoading = false,

                        )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                    )
                }
        }
    }


    fun findRecipeById(id: Long) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val recipe = findRecipeByIdUseCase(id)
            if (recipe != null) {
                _uiState.value = RecipeUiState(
                    id = recipe.id,
                    searchKeyword = recipe.searchKeyword,
                    isSubscribe = true,
                    isLoading = false,
                    ingredientsList = recipe.ingredientsList.map { it.toPresentation() },
                    recipeList = recipe.recipeList.map { it.toPresentation() })
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                )
            }
        }
    }
}

