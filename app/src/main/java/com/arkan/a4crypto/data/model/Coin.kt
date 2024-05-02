package com.arkan.a4crypto.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coin(
    val id: String,
    val name: String,
    val desc: String,
    val image: String,
    val price: Double,
) : Parcelable
