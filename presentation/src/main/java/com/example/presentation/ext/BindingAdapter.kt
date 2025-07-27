package com.example.presentation.ext

import android.animation.Animator
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView

@BindingAdapter("onLottieAnimateState")
fun LottieAnimationView.onLottieAnimateState(state: ((LottieAnimateState) -> Unit)?) {
    this.addAnimatorListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            state?.invoke(LottieAnimateState.Start)
        }

        override fun onAnimationEnd(animation: Animator) {
            state?.invoke(LottieAnimateState.End)
        }

        override fun onAnimationCancel(animation: Animator) {
            state?.invoke(LottieAnimateState.Cancel)
        }

        override fun onAnimationRepeat(animation: Animator) {}
    })
}

sealed class LottieAnimateState {
    data object Start : LottieAnimateState()
    data object End : LottieAnimateState()
    data object Cancel : LottieAnimateState()
}