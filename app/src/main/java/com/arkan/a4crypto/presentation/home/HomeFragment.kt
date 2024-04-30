package com.arkan.a4crypto.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.arkan.a4crypto.data.datasource.CoinDataSource
import com.arkan.a4crypto.data.datasource.CoinDataSourceImpl
import com.arkan.a4crypto.data.model.Coin
import com.arkan.a4crypto.data.repository.CoinRepository
import com.arkan.a4crypto.data.repository.CoinRepositoryImpl
import com.arkan.a4crypto.data.source.network.services.FourCryptoApiServices
import com.arkan.a4crypto.databinding.FragmentHomeBinding
import com.arkan.a4crypto.presentation.home.adapter.CoinAdapter
import com.arkan.aresto.utils.GenericViewModelFactory
import com.arkan.aresto.utils.proceedWhen

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var coinAdapter: CoinAdapter? = null

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
        getProductData()
    }

    private fun getProductData() {
        viewModel.getCoinList().observe(viewLifecycleOwner) { it ->
            it.proceedWhen(
                doOnSuccess = {
                    it.payload?.let { data ->
                        bindCoinList(data)
                    }
                },
            )
        }
    }

    private fun bindCoinList(data: List<Coin>) {
        binding.rvListFood.apply {
            adapter = this@HomeFragment.coinAdapter
        }
        coinAdapter?.submitData(data)
    }
}
