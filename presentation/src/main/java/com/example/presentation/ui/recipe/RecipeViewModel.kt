package com.example.presentation.ui.recipe

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.DeleteRecipeUseCase
import com.example.domain.usecase.FindRecipeByIdUseCase
import com.example.domain.usecase.FindRecipeByNameUseCase
import com.example.domain.usecase.GenerateRecipeUseCase
import com.example.domain.usecase.InsertRecipeUseCase
import com.example.presentation.model.IngredientsModel
import com.example.presentation.model.RecipeModel
import com.example.presentation.model.WellbeingRecipeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
}

//    fun getRecipe() {
//        _uiState.value = _uiState.value.copy(isLoading = true)
//        val searchKeyword = _uiState.value.searchKeyword
//        val ingredientsList = _uiState.value.ingredientsList
//        viewModelScope.launch(Dispatchers.IO) {
//            runCatching {
//                val response = repository.getGptResponse(
//                    GptRequestParam(
//                        messages = arrayListOf(
//                            MessageRequestParam(
//                                role = "user",
//                                content = getFormattedRecipe(searchKeyword, ingredientsList)
//                            )
//                        )
//                    )
//                )
//
//                withContext(Dispatchers.Main) {
//                    _uiModel.value = _uiModel.value.copy(
//                        isLoading = false, recipeList = getRecipeList(response)
//                    )
//                }
//            }.onFailure {
//                withContext(Dispatchers.Main) {
//                    _uiModel.value = _uiModel.value.copy(
//                        isLoading = false
//                    )
//                }
//            }
//        }
//    }
//
//    private fun getRecipeList(response: GPT): ArrayList<RecipeModel> {
//        val recipeList = ArrayList<RecipeModel>()
//        val jsonArray = response.choices[0].message.content?.let { JSONArray(it) }
//
//        for (i in 0 until (jsonArray?.length() ?: 0)) {
//            jsonArray?.getJSONObject(i)?.apply {
//                if (has("레시피")) {
//                    val recipe = get("레시피").toString()
//                    recipeList.add(
//                        RecipeModel(
//                            initialIsSelected = true, recipe = recipe
//                        )
//                    )
//                }
//            }
//        }
//        return recipeList
//    }
//
//    private fun getFormattedRecipe(
//        searchKeyword: String,
//        ingredientsList: ArrayList<IngredientsModel>,
//    ): String {
//
//        var format = ""
//        ingredientsList.forEachIndexed { index, ingredientsModel ->
//            format += if (index == ingredientsList.lastIndex) {
//                ingredientsModel.ingredients
//            } else {
//                "${ingredientsModel.ingredients},"
//            }
//        }
//        return "$format 재료들로 ${searchKeyword}(을)를 요리하기 위한 순서를 일반적인 방식(레시피)과 건강한 방식(웰빙)을 나열해줘\n" + "답변은 아래와 같은 형식과 한국어만으로 표시해\n" + "주의사항: 두개의 JSON Array 를 생성하지말고 하나의 JSON Array 로 답변을 표시해\n" + "[{\"레시피\":\"기름에 돼지고기를 볶는다\"}, {\"레시피\":\"물을 붓는다\"}, {\"웰빙\":\"따뜻한 물을 붓는다\"}, {\"웰빙\":\"땅콩을 갈아 넣는다\"}]"
//    }
//
//    fun insertRecipe() {
//        val searchKeyword = _uiModel.value.searchKeyword
//        val ingredientsList = _uiModel.value.ingredientsList
//        val recipeList = _uiModel.value.recipeList
//        val wellbeingRecipeList = _uiModel.value.wellbeingRecipeModel
//
//        viewModelScope.launch(Dispatchers.IO) {
//            val existingRecipe = repository.findRecipeByName(searchKeyword)
//
//            if (existingRecipe != null) {
//                withContext(Dispatchers.Main) {
//                    _uiModel.value = _uiModel.value.copy().apply {
//                        this.id = existingRecipe.id
//                        this.isSubscribe = true
//                    }
//                }
//            } else {
//                val localRecipeEntity = LocalRecipeEntity(
//                    searchKeyword = searchKeyword,
//                    ingredientsList = ArrayList(ingredientsList.map { it.toEntity() }),
//                    recipeList = ArrayList(recipeList.map { it.toEntity() }),
//                    wellbeingRecipeList = ArrayList(wellbeingRecipeList.map { it.toEntity() })
//                )
//                val id = repository.insertRecipe(localRecipeEntity)
//
//                withContext(Dispatchers.Main) {
//                    _uiModel.value = _uiModel.value.copy().apply {
//                        this.id = id
//                        this.isSubscribe = true
//                    }
//                }
//            }
//        }
//    }
//
//    fun deleteRecipe() {
//        val searchKeyword = _uiModel.value.searchKeyword
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.deleteRecipeByName(searchKeyword)
//
//            withContext(Dispatchers.Main) {
//                _uiModel.value = _uiModel.value.copy().apply {
//                    this.id = 0
//                    this.isSubscribe = false
//                }
//            }
//        }
//    }
//
//    fun findRecipe(id: Long) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val recipeModel = repository.findRecipe(id)
//
//            recipeModel?.let {
//                withContext(Dispatchers.Main) {
//                    _uiModel.value = RecipeUiModel(
//                        id = recipeModel.id,
//                        searchKeyword = recipeModel.searchKeyword,
//                        isSubscribe = true,
//                        isLoading = false,
//                        ingredientsList = ArrayList(recipeModel.ingredientsList.map { it.toModel() }),
//                        recipeList = ArrayList(recipeModel.recipeList.map { it.toModel() }),
//                        wellbeingRecipeModel = ArrayList(recipeModel.wellbeingRecipeList.map { it.toModel() }),
//                    )
//                }
//            }
//        }
//    }
//
//    fun checkIfFavoriteByName(recipeName: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val isFavorite = repository.isFavoriteByName(recipeName)
//            withContext(Dispatchers.Main) {
//                _uiModel.value = _uiModel.value.copy().apply {
//                    this.isSubscribe = isFavorite
//                }
//            }
//        }
//    }
//}