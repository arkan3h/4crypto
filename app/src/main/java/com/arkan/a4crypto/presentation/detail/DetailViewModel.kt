package com.arkan.a4crypto.presentation.detail

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.arkan.a4crypto.data.model.Coin

class DetailViewModel(
    extras: Bundle?,
) : ViewModel() {
    val product = extras?.getParcelable<Coin>(DetailActivity.EXTRAS_ITEM_ACT)
}
