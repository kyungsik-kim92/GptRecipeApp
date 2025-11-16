package com.example.presentation.ui.shoppinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.DeleteAllShoppingItemsUseCase
import com.example.domain.usecase.DeleteCheckedShoppingItemsUseCase
import com.example.domain.usecase.DeleteShoppingItemUseCase
import com.example.domain.usecase.GenerateShoppingListUseCase
import com.example.domain.usecase.GetAllShoppingListUseCase
import com.example.domain.usecase.InsertShoppingItemUseCase
import com.example.domain.usecase.UpdateShoppingItemCheckedUseCase
import com.example.presentation.mapper.toDomain
import com.example.presentation.mapper.toPresentation
import com.example.presentation.model.ShoppingItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val getAllShoppingItemsUseCase: GetAllShoppingListUseCase,
    private val generateShoppingListUseCase: GenerateShoppingListUseCase,
    private val updateShoppingItemCheckedUseCase: UpdateShoppingItemCheckedUseCase,
    private val deleteCheckedShoppingItemsUseCase: DeleteCheckedShoppingItemsUseCase,
    private val deleteAllShoppingItemsUseCase: DeleteAllShoppingItemsUseCase,
    private val deleteShoppingItemUseCase: DeleteShoppingItemUseCase,
    private val insertShoppingItemUseCase: InsertShoppingItemUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<ShoppingListUiState>(ShoppingListUiState.Idle)
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ShoppingListEvent>()
    val events: SharedFlow<ShoppingListEvent> = _events.asSharedFlow()

    init {
        loadShoppingList()
    }

    private fun loadShoppingList() {
        viewModelScope.launch {
            _uiState.value = ShoppingListUiState.Loading

            getAllShoppingItemsUseCase()
                .catch { e ->
                    _uiState.value = ShoppingListUiState.Error(
                        message = e.message ?: "쇼핑 리스트를 불러오는데 실패했습니다."
                    )
                }
                .collect { domainItems ->
                    val presentationItems = domainItems.map { it.toPresentation() }
                    val checkedCount = presentationItems.count { it.isChecked }

                    _uiState.value = ShoppingListUiState.Success(
                        items = presentationItems,
                        totalCount = presentationItems.size,
                        checkedCount = checkedCount
                    )
                }
        }
    }

    fun onItemCheckedChanged(itemId: Long, isChecked: Boolean) {
        viewModelScope.launch {
            try {
                updateShoppingItemCheckedUseCase(itemId, isChecked)
            } catch (e: Exception) {
                _uiState.value = ShoppingListUiState.Error(
                    message = "체크 상태 변경에 실패했습니다."
                )
            }
        }
    }

    fun deleteCheckedItems() {
        viewModelScope.launch {
            try {
                deleteCheckedShoppingItemsUseCase()
            } catch (e: Exception) {
                _uiState.value = ShoppingListUiState.Error(
                    message = "선택한 항목 삭제에 실패했습니다."
                )
            }
        }
    }

    fun deleteAllItems() {
        viewModelScope.launch {
            _events.emit(ShoppingListEvent.ShowDeleteAllConfirmation)
        }
    }

    fun confirmDeleteAll() {
        viewModelScope.launch {
            try {
                deleteAllShoppingItemsUseCase()
                _events.emit(ShoppingListEvent.ShowSuccess("전체 항목이 삭제되었습니다"))
            } catch (e: Exception) {
                _events.emit(ShoppingListEvent.ShowError("전체 삭제에 실패했습니다"))
            }
        }
    }

    fun deleteItem(itemId: Long) {
        viewModelScope.launch {
            try {
                deleteShoppingItemUseCase(itemId)
            } catch (e: Exception) {
                _events.emit(ShoppingListEvent.ShowError("삭제에 실패했습니다"))
            }
        }
    }

    fun restoreItem(item: ShoppingItemModel) {
        viewModelScope.launch {
            try {
                insertShoppingItemUseCase(item.toDomain())
                _events.emit(ShoppingListEvent.ShowSuccess("항목이 복구되었습니다"))
            } catch (e: Exception) {
                _events.emit(ShoppingListEvent.ShowError("복구에 실패했습니다"))
            }
        }
    }

    fun clearError() {
        if (_uiState.value is ShoppingListUiState.Error) {
            loadShoppingList()
        }
    }
}