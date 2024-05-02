package com.arkan.a4crypto.presentation.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import coil.load
import com.arkan.a4crypto.R
import com.arkan.a4crypto.data.datasource.CoinDetailDataSource
import com.arkan.a4crypto.data.datasource.CoinDetailDataSourceImpl
import com.arkan.a4crypto.data.datasource.favorite.FavoriteDataSource
import com.arkan.a4crypto.data.datasource.favorite.FavoriteDatabaseDataSource
import com.arkan.a4crypto.data.model.CoinDetail
import com.arkan.a4crypto.data.repository.CoinDetailRepository
import com.arkan.a4crypto.data.repository.CoinDetailRepositoryImpl
import com.arkan.a4crypto.data.repository.FavoriteRepositoryImpl
import com.arkan.a4crypto.data.source.AppDatabase
import com.arkan.a4crypto.data.source.network.services.FourCryptoApiServices
import com.arkan.a4crypto.databinding.ActivityDetailBinding
import com.arkan.aresto.utils.GenericViewModelFactory
import com.arkan.aresto.utils.proceedWhen
import com.arkan.aresto.utils.toDollarFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRAS_ITEM_ACT = "EXTRAS_ITEM_ACT"
    }

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private val viewModel: DetailViewModel by viewModels {
        var ApiDataServices = FourCryptoApiServices.invoke()
        val database = AppDatabase.getInstance(this)
        val favoriteDataSource: FavoriteDataSource = FavoriteDatabaseDataSource(database.favoriteDao())
        val favoriteRepository = FavoriteRepositoryImpl(favoriteDataSource)

        val catalogDataSource: CoinDetailDataSource = CoinDetailDataSourceImpl(ApiDataServices)
        val catalogRepository: CoinDetailRepository = CoinDetailRepositoryImpl(catalogDataSource)
        GenericViewModelFactory.create(
            DetailViewModel(
                intent?.extras,
                catalogRepository,
                favoriteRepository,
            ),
        )
    }

    private lateinit var url: String
    private val baseUrl = "https://www.coingecko.com/en/coins/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
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
        binding.tvDetailPrice.text = data.price.toDollarFormat()
    }

    private fun addCoinToFavorite() {
        viewModel.addToFavorite().observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(
                        this,
                        getString(R.string.text_succes_add_to_favorite),
                        Toast.LENGTH_SHORT,
                    ).show()
                    finish()
                },
                doOnError = {
                    Toast.makeText(
                        this,
                        getString(R.string.text_faliled_add_to_favorite),
                        Toast.LENGTH_SHORT,
                    )
                        .show()
                },
                doOnLoading = {
                    Toast.makeText(
                        this,
                        getString(R.string.text_load_add_to_favorite),
                        Toast.LENGTH_SHORT,
                    ).show()
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
