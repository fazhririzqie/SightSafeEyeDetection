package com.example.sightsafe.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class News(
    val title: String,
    val subtitle: String,
    val photo: Int,
    val url: String
) : Parcelable