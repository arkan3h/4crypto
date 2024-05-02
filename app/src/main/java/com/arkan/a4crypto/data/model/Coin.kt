package com.arkan.a4crypto.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coin(
    var id: String,
    val name: String,
    val desc: String,
    val image: String,
    val price: Double,
) : Parcelable
