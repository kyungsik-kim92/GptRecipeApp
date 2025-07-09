package com.example.presentation.ui.splash

import android.animation.Animator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.example.presentation.R
import com.example.presentation.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLottieAnimation()
        observeViewModel()
    }

    private fun setupLottieAnimation() {
        with(binding.splash) {

            setAnimation(R.raw.splash)
            addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    viewModel.onAnimationStart()
                }

                override fun onAnimationEnd(animation: Animator) {
                    viewModel.onAnimationEnd()
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
            playAnimation()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewEvent.collect { event ->
                    handleViewEvent(event)
                }
            }
        }
    }

    private fun handleViewEvent(event: SplashUiEvent) {
        when (event) {
            is SplashUiEvent.RouteToHome -> {
                routeToHome()
            }

            is SplashUiEvent.ShowError -> {
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun routeToHome() {
        try {
            findNavController().navigate(
                SplashFragmentDirections.actionNavigationSplashToNavigationSearch()
            )
        } catch (e: Exception) {
            Log.e("SplashFragment", "Navigation 실패", e)
        }
    }
}