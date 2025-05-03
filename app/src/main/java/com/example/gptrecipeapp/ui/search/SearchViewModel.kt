package com.example.gptrecipeapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gptrecipeapp.ApiService
import com.example.gptrecipeapp.GptRequestParam
import com.example.gptrecipeapp.MessageRequestParam
import com.example.gptrecipeapp.Repository
import com.example.gptrecipeapp.RepositoryImpl
import com.example.gptrecipeapp.SearchUiModel
import com.example.gptrecipeapp.model.GPT
import com.example.gptrecipeapp.model.IngredientsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class SearchViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiModel = MutableStateFlow(
        SearchUiModel(
            searchKeyword = "",
            isFetched = false,
            isLoading = false,
            ingredientsList = ArrayList()
        )
    )
    val uiModel: StateFlow<SearchUiModel> = _uiModel

    fun getIngredientsByRecipe(searchKeyword: String) {
        viewModelScope.launch {
            _uiModel.update { it.copy(isLoading = true) }

            runCatching {
                val response = withContext(Dispatchers.IO) {
                    repository.getGptResponse(
                        GptRequestParam(
                            messages = listOf(
                                MessageRequestParam(
                                    role = "user",
                                    content = getFormattedSearchKeyword(searchKeyword)
                                )
                            )
                        )
                    )
                }

                _uiModel.update {
                    it.copy(
                        searchKeyword = searchKeyword,
                        isFetched = true,
                        isLoading = false,
                        ingredientsList = getIngredientsList(response)
                    )
                }
            }.onFailure {
                _uiModel.update { it.copy(isLoading = false) }

            }
        }
    }

    private fun getFormattedSearchKeyword(searchKeyword: String): String {
        return "${searchKeyword}(을)를 요리하기 위한 재료를 나열해줘\n" +
                "답변은 아래와 같은 형식과 한국어만으로 표시해\n" +
                "[{\"재료\":\"양파\"}, {\"재료\":\"김치\"}]"
    }

    private fun getIngredientsList(response: GPT): ArrayList<IngredientsModel> {
        val ingredientsList = ArrayList<IngredientsModel>()
        val jsonArray =
            response.choices[0].message.content?.let { JSONArray(it) } ?: return ingredientsList

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            if (jsonObject.has("재료")) {
                ingredientsList.add(
                    IngredientsModel(
                        initialIsSelected = true,
                        ingredients = jsonObject.get("재료").toString()
                    )
                )
            }
        }

        return ingredientsList
    }
}