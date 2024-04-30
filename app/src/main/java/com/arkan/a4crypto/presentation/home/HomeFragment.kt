package com.arkan.a4crypto.presentation.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.arkan.a4crypto.R
import com.arkan.a4crypto.data.datasource.CoinDataSource
import com.arkan.a4crypto.data.datasource.CoinDataSourceImpl
import com.arkan.a4crypto.data.repository.CoinRepository
import com.arkan.a4crypto.data.repository.CoinRepositoryImpl
import com.arkan.a4crypto.data.source.network.services.FourCryptoApiServices
import com.arkan.a4crypto.databinding.FragmentHomeBinding
import com.arkan.a4crypto.presentation.home.adapter.CoinAdapter
import com.arkan.aresto.utils.GenericViewModelFactory
import com.arkan.aresto.utils.proceedWhen

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var coinAdapter = CoinAdapter()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val viewModel: HomeViewModel by viewModels {
        val service = FourCryptoApiServices.invoke()
        val ds: CoinDataSource = CoinDataSourceImpl(service)
        val rp: CoinRepository = CoinRepositoryImpl(ds)
        GenericViewModelFactory.create(
            HomeViewModel(rp),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        getCoinData()
        bindCoinList()
        refreshLayout()
    }

    private fun refreshLayout() {
        swipeRefreshLayout = binding.swipeHome
        swipeRefreshLayout.setOnRefreshListener {
            getCoinData()
            bindCoinList()
            Handler().postDelayed(
                { swipeRefreshLayout.isRefreshing = false },
                1000,
            )
        }
    }

    private fun getCoinData() {
        viewModel.getCoinList().observe(viewLifecycleOwner) { it ->
            it.proceedWhen(
                doOnLoading = {
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.rvListCoin.isVisible = false
                },
                doOnError = {
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = it.exception?.message.orEmpty()
                    binding.rvListCoin.isVisible = false
                },
                doOnSuccess = {
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.rvListCoin.isVisible = true
                    it.payload?.let { data ->
                        coinAdapter.submitData(data)
                    }
                },
                doOnEmpty = {
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = getString(R.string.text_coin_is_empty)
                    binding.rvListCoin.isVisible = false
                },
            )
        }
    }

    private fun bindCoinList() {
        binding.rvListCoin.adapter = this@HomeFragment.coinAdapter
    }
}
