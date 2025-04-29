package com.example.gptrecipeapp.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.gptrecipeapp.ApiService
import com.example.gptrecipeapp.GptRequestParam
import com.example.gptrecipeapp.MessageRequestParam
import com.example.gptrecipeapp.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = ApiService.create()
        lifecycleScope.launch {
            val body = apiService.getGptResponse(
                GptRequestParam(
                    messages = arrayListOf(
                        MessageRequestParam(
                            role = "user",
                            content = "김치찌개"
                        )
                    )
                )
            )
            Log.d("ddd", body.toString())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}