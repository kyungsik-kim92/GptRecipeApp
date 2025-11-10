package com.example.presentation.ui.shoppinglist

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.presentation.databinding.FragmentShoppingListBinding
import com.example.presentation.ui.adapter.ShoppingListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ShoppingListFragment : Fragment() {
    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShoppingListViewModel by viewModels()

    private lateinit var shoppingListAdapter: ShoppingListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shoppingListAdapter = ShoppingListAdapter(
            onItemChecked = { item, isChecked ->
                viewModel.onItemCheckedChanged(item.id, isChecked)
            }
        )
        setupRecyclerView()
        setupButtons()
        observeUiState()
        observeEvents()
    }

    private fun setupRecyclerView() {
        binding.rvShoppingList.apply {
            adapter = shoppingListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupButtons() {
        binding.btnDeleteChecked.setOnClickListener {
            val currentState = viewModel.uiState.value
            if (currentState is ShoppingListUiState.Success && currentState.checkedCount > 0) {
                showDeleteCheckedConfirmation()
            } else {
                Toast.makeText(requireContext(), "완료된 항목이 없습니다", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDeleteAll.setOnClickListener {
            val currentState = viewModel.uiState.value
            if (currentState is ShoppingListUiState.Success && currentState.totalCount > 0) {
                viewModel.deleteAllItems()
            } else {
                Toast.makeText(requireContext(), "삭제할 항목이 없습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeleteCheckedConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("완료된 항목 삭제")
            .setMessage("체크된 항목을 모두 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                viewModel.deleteCheckedItems()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun showDeleteAllConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("전체 삭제")
            .setMessage("모든 항목을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                viewModel.confirmDeleteAll()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ShoppingListUiState.Idle -> {
                            binding.progressBar.visibility = View.GONE
                        }

                        is ShoppingListUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rvShoppingList.visibility = View.GONE
                            binding.llEmptyState.visibility = View.GONE
                        }

                        is ShoppingListUiState.Success -> {
                            binding.progressBar.visibility = View.GONE

                            if (state.items.isEmpty()) {
                                binding.rvShoppingList.visibility = View.GONE
                                binding.llEmptyState.visibility = View.VISIBLE
                                binding.btnDeleteChecked.visibility = View.GONE
                            } else {
                                binding.rvShoppingList.visibility = View.VISIBLE
                                binding.llEmptyState.visibility = View.GONE
                                binding.btnDeleteChecked.visibility = View.VISIBLE

                                shoppingListAdapter.submitList(state.items)
                            }

                            binding.tvTotalCount.text = "전체: ${state.totalCount}개"
                            binding.tvCheckedCount.text = "완료: ${state.checkedCount}개"
                            binding.btnDeleteChecked.isEnabled = state.checkedCount > 0
                        }


                        is ShoppingListUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                state.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.clearError()
                        }
                    }
                }
            }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is ShoppingListEvent.ShowSuccess -> {
                            Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is ShoppingListEvent.ShowError -> {
                            Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is ShoppingListEvent.ShowDeleteAllConfirmation -> {
                            showDeleteAllConfirmation()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}