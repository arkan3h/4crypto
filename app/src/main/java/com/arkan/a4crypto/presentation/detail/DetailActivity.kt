package com.arkan.a4crypto.presentation.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import coil.load
import com.arkan.a4crypto.R
import com.arkan.a4crypto.data.model.CoinDetail
import com.arkan.a4crypto.databinding.ActivityDetailBinding
import com.arkan.aresto.utils.proceedWhen
import com.arkan.aresto.utils.toDollarFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRAS_ITEM_ACT = "EXTRAS_ITEM_ACT"
    }

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private val viewModel: DetailViewModel by viewModel {
        parametersOf(intent.extras)
    }

    private lateinit var url: String
    private val baseUrl = "https://www.coingecko.com/en/coins/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getDetailInfo()
        setClickListener()
    }

    private fun setClickListener() {
        binding.btnDetailBack.setOnClickListener {
            backNavigation()
        }
        binding.btnDetailFav.setOnClickListener {
            addCoinToFavorite()
        }
        binding.btnGoToWeb.setOnClickListener {
            goToWeb()
        }
    }

    private fun backNavigation() = onBackPressedDispatcher.onBackPressed()

    private fun getDetailInfo() {
        viewModel.getDetailInfo().observe(this) { it ->
            it.proceedWhen(
                doOnLoading = {
                    binding.svDetail.isVisible = false
                    binding.btnGoToWeb.isVisible = false
                    binding.btnDetailFav.isVisible = false
                    binding.layoutState.pbLoading.isVisible = true
                },
                doOnSuccess = {
                    binding.svDetail.isVisible = true
                    binding.btnGoToWeb.isVisible = true
                    binding.btnDetailFav.isVisible = true
                    it.payload?.let { data ->
                        bindCoinDetail(data)
                        setUrl(data)
                    }
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                },
                doOnError = {
                    binding.svDetail.isVisible = false
                    binding.btnGoToWeb.isVisible = false
                    binding.btnDetailFav.isVisible = false
                    binding.layoutState.tvError.text = it.exception?.message.orEmpty()
                },
            )
        }
    }

    private fun bindCoinDetail(data: CoinDetail) {
        binding.ivDetailImage.load(data.image)
        binding.tvDetailName.text = data.name
        binding.tvDetailDesc.text = data.desc
        binding.tvDetailSymbol.text = data.symbol
        binding.tvDetailPrice.text = data.price.toDollarFormat()
    }

    private fun addCoinToFavorite() {
        viewModel.addToFavorite().observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.btnDetailFav.isVisible = true
                    binding.btnDetailFav.setImageResource(R.drawable.ic_fav)
                    binding.pbLoading.isVisible = false
                    Toast.makeText(
                        this,
                        getString(R.string.text_success_add_to_favorite),
                        Toast.LENGTH_SHORT,
                    ).show()
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnDetailFav.isVisible = true
                    Toast.makeText(
                        this,
                        getString(R.string.text_failed_add_to_favorite),
                        Toast.LENGTH_SHORT,
                    ).show()
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnDetailFav.isVisible = false
                },
            )
        }
    }

    private fun setUrl(data: CoinDetail) {
        url = baseUrl + data.id
    }

    private fun goToWeb() {
        val i = Intent(Intent.ACTION_VIEW)
        i.setData(Uri.parse(url))
        startActivity(i)
    }
}
