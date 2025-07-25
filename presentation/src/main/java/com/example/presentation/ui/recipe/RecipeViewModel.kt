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
import com.example.presentation.ui.common.RecipePromptUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val generateRecipeUseCase: GenerateRecipeUseCase,
    private val insertRecipeUseCase: InsertRecipeUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase,
    private val findRecipeByIdUseCase: FindRecipeByIdUseCase,
    private val findRecipeByNameUseCase: FindRecipeByNameUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipeUiState>(RecipeUiState.Idle())
    val uiState: StateFlow<RecipeUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RecipeUiEvent>()
    val events: SharedFlow<RecipeUiEvent> = _events.asSharedFlow()


    fun setupInitialData(
        searchKeyword: String,
        ingredientsList: List<IngredientsModel>,
        recipeList: List<RecipeModel>,
        wellbeingRecipeList: List<WellbeingRecipeModel>
    ) {
        _uiState.value = RecipeUiState.Success(
            id = 0L,
            searchKeyword = searchKeyword,
            ingredientsList = ingredientsList,
            recipeList = recipeList,
            isSubscribe = false,
            wellbeingRecipeModel = wellbeingRecipeList
        )
    }

    fun getRecipe() {
        val currentState = _uiState.value
        _uiState.value = RecipeUiState.Loading(
            id = currentState.id,
            searchKeyword = currentState.searchKeyword,
            ingredientsList = currentState.ingredientsList,
            recipeList = currentState.recipeList,
            isSubscribe = currentState.isSubscribe,
            wellbeingRecipeModel = currentState.wellbeingRecipeModel
        )

        val prompt = RecipePromptUtil.createRecipePrompt(
            currentState.searchKeyword,
            currentState.ingredientsList
        )
        viewModelScope.launch(Dispatchers.IO) {
            generateRecipeUseCase(prompt)
                .onSuccess { response ->
                    val (recipeList, wellbeingList) = RecipePromptUtil.parseRecipeResponse(response.content)

                    if (recipeList.isNotEmpty() || wellbeingList.isNotEmpty()) {
                        val successState = RecipeUiState.Success(
                            id = currentState.id,
                            searchKeyword = currentState.searchKeyword,
                            ingredientsList = currentState.ingredientsList,
                            recipeList = recipeList,
                            isSubscribe = currentState.isSubscribe,
                            wellbeingRecipeModel = wellbeingList
                        )
                        _uiState.value = successState
                        _events.emit(RecipeUiEvent.ShowSuccess("레시피가 생성되었습니다"))
                    } else {
                        val errorState = RecipeUiState.Error(
                            id = currentState.id,
                            searchKeyword = currentState.searchKeyword,
                            ingredientsList = currentState.ingredientsList,
                            recipeList = currentState.recipeList,
                            isSubscribe = currentState.isSubscribe,
                            wellbeingRecipeModel = currentState.wellbeingRecipeModel,
                            message = "레시피를 생성할 수 없습니다"
                        )
                        _uiState.value = errorState
                        _events.emit(RecipeUiEvent.ShowError("레시피를 생성할 수 없습니다"))
                    }
                }
                .onFailure { exception ->
                    val errorState = RecipeUiState.Error(
                        id = currentState.id,
                        searchKeyword = currentState.searchKeyword,
                        ingredientsList = currentState.ingredientsList,
                        recipeList = currentState.recipeList,
                        isSubscribe = currentState.isSubscribe,
                        wellbeingRecipeModel = currentState.wellbeingRecipeModel,
                        message = exception.message ?: "Exception Message"
                    )
                    _uiState.value = errorState
                    _events.emit(RecipeUiEvent.ShowError(errorState.message))
                }
        }
    }

    fun insertRecipe() {
        val currentState = _uiState.value

        viewModelScope.launch(Dispatchers.IO) {
            val existingRecipe = findRecipeByNameUseCase(currentState.searchKeyword)

            if (existingRecipe != null) {
                val successState = when (currentState) {
                    is RecipeUiState.Loading -> RecipeUiState.Success(
                        id = existingRecipe.id,
                        searchKeyword = currentState.searchKeyword,
                        ingredientsList = currentState.ingredientsList,
                        recipeList = currentState.recipeList,
                        isSubscribe = true,
                        wellbeingRecipeModel = currentState.wellbeingRecipeModel
                    )

                    else -> RecipeUiState.Success(
                        id = existingRecipe.id,
                        searchKeyword = currentState.searchKeyword,
                        ingredientsList = currentState.ingredientsList,
                        recipeList = currentState.recipeList,
                        isSubscribe = true,
                        wellbeingRecipeModel = currentState.wellbeingRecipeModel
                    )
                }
                _uiState.value = successState
                _events.emit(RecipeUiEvent.ShowSuccess("즐겨찾기에 추가되었습니다"))
            } else {
                val localRecipe = createLocalRecipeFromCurrentState()

                insertRecipeUseCase(localRecipe)
                    .onSuccess { id ->
                        val successState = RecipeUiState.Success(
                            id = id,
                            searchKeyword = currentState.searchKeyword,
                            ingredientsList = currentState.ingredientsList,
                            recipeList = currentState.recipeList,
                            isSubscribe = true,
                            wellbeingRecipeModel = currentState.wellbeingRecipeModel
                        )
                        _uiState.value = successState
                        _events.emit(RecipeUiEvent.ShowSuccess("즐겨찾기에 추가되었습니다"))
                    }
                    .onFailure { exception ->
                        val errorState = RecipeUiState.Error(
                            id = currentState.id,
                            searchKeyword = currentState.searchKeyword,
                            ingredientsList = currentState.ingredientsList,
                            recipeList = currentState.recipeList,
                            isSubscribe = currentState.isSubscribe,
                            wellbeingRecipeModel = currentState.wellbeingRecipeModel,
                            message = "즐겨찾기 추가에 실패했습니다"
                        )
                        _uiState.value = errorState
                        _events.emit(RecipeUiEvent.ShowError("즐겨찾기 추가에 실패했습니다"))
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
            recipeList = currentState.recipeList.map { it.toDomain() },
            wellbeingRecipeList = currentState.wellbeingRecipeModel.map { it.toDomain() }
        )
    }

    fun deleteRecipe() {
        val currentState = _uiState.value

        viewModelScope.launch {
            deleteRecipeUseCase(currentState.searchKeyword)
                .onSuccess {
                    val successState = RecipeUiState.Success(
                        id = 0L,
                        searchKeyword = currentState.searchKeyword,
                        ingredientsList = currentState.ingredientsList,
                        recipeList = currentState.recipeList,
                        isSubscribe = false,
                        wellbeingRecipeModel = currentState.wellbeingRecipeModel
                    )
                    _uiState.value = successState
                    _events.emit(RecipeUiEvent.ShowSuccess("즐겨찾기에서 제거되었습니다"))
                }
                .onFailure { exception ->
                    val errorState = RecipeUiState.Error(
                        id = currentState.id,
                        searchKeyword = currentState.searchKeyword,
                        ingredientsList = currentState.ingredientsList,
                        recipeList = currentState.recipeList,
                        isSubscribe = currentState.isSubscribe,
                        wellbeingRecipeModel = currentState.wellbeingRecipeModel,
                        message = "즐겨찾기 제거에 실패했습니다"
                    )
                    _uiState.value = errorState
                    _events.emit(RecipeUiEvent.ShowError("즐겨찾기 제거에 실패했습니다"))
                }
        }
    }


    fun findRecipeById(id: Long) {
        _uiState.value = RecipeUiState.Loading(
            id = 0L,
            searchKeyword = "",
            ingredientsList = emptyList(),
            recipeList = emptyList(),
            isSubscribe = false,
            wellbeingRecipeModel = emptyList()
        )
        viewModelScope.launch(Dispatchers.IO) {
            val recipe = findRecipeByIdUseCase(id)
            if (recipe != null) {
                val successState = RecipeUiState.Success(
                    id = recipe.id,
                    searchKeyword = recipe.searchKeyword,
                    isSubscribe = true,
                    ingredientsList = recipe.ingredientsList.map { it.toPresentation() },
                    recipeList = recipe.recipeList.map { it.toPresentation() },
                    wellbeingRecipeModel = recipe.wellbeingRecipeList.map { it.toPresentation() }
                )
                _uiState.value = successState
            } else {
                val errorState = RecipeUiState.Error(
                    id = 0L,
                    searchKeyword = "",
                    ingredientsList = emptyList(),
                    recipeList = emptyList(),
                    isSubscribe = false,
                    wellbeingRecipeModel = emptyList(),
                    message = "레시피를 찾을 수 없습니다"
                )
                _uiState.value = errorState
                _events.emit(RecipeUiEvent.ShowError("레시피를 찾을 수 없습니다"))
            }
        }
    }

    fun checkIfFavoriteByName(recipeName: String) {
        viewModelScope.launch {
            findRecipeByNameUseCase(recipeName)?.let { recipe ->
                val currentState = _uiState.value
                _uiState.value = when (currentState) {
                    is RecipeUiState.Idle -> currentState.copy(isSubscribe = true, id = recipe.id)
                    is RecipeUiState.Loading -> currentState.copy(
                        isSubscribe = true,
                        id = recipe.id
                    )

                    is RecipeUiState.Success -> currentState.copy(
                        isSubscribe = true,
                        id = recipe.id
                    )

                    is RecipeUiState.Error -> currentState.copy(isSubscribe = true, id = recipe.id)
                }
            }

        }
    }

    fun routeToWellbeing() {
        val currentState = _uiState.value
        if (currentState.wellbeingRecipeModel.isEmpty()) {
            viewModelScope.launch {
                _events.emit(RecipeUiEvent.ShowError("웰빙 레시피가 없습니다"))
            }
        } else {
            viewModelScope.launch {
                _events.emit(RecipeUiEvent.RouteToWellbeing(currentState))
            }
        }
    }
}
