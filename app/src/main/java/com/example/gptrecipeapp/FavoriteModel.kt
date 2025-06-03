package com.example.gptrecipeapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class FavoriteModel(
    var id: Long = 0,
    var searchKeyword: String = ""
) : Parcelable
