package com.example.gptrecipeapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteUiModel(
    var favoriteList: ArrayList<FavoriteModel>,
    val isLoading: Boolean = false
) : Parcelable