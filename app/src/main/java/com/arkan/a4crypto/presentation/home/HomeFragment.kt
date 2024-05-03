package com.arkan.a4crypto.presentation.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.arkan.a4crypto.R
import com.arkan.a4crypto.data.model.Coin
import com.arkan.a4crypto.databinding.FragmentHomeBinding
import com.arkan.a4crypto.presentation.detail.DetailActivity
import com.arkan.a4crypto.presentation.home.adapter.CoinAdapter
import com.arkan.a4crypto.presentation.home.adapter.OnItemCLickedListener
import com.arkan.aresto.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var coinAdapter: CoinAdapter? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun showUserData() {
        homeViewModel.getCurrentUser()?.let { user ->
            binding.layoutBanner.tvGreetings.text =
                getString(R.string.text_greetings, user.fullName)
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        getCoinData()
        refreshLayout()
        showUserData()
    }

    private fun refreshLayout() {
        swipeRefreshLayout = binding.swipeHome
        swipeRefreshLayout.setOnRefreshListener {
            getCoinData()
            showUserData()
            Handler().postDelayed(
                { swipeRefreshLayout.isRefreshing = false },
                1000,
            )
        }
    }

    private fun getCoinData() {
        homeViewModel.getCoinList().observe(viewLifecycleOwner) { it ->
            it.proceedWhen(
                doOnLoading = {
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.rvListCoin.isVisible = false
                },
                doOnError = {
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.rvListCoin.isVisible = false
                },
                doOnSuccess = {
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.rvListCoin.isVisible = true
                    it.payload?.let { data ->
                        bindCoinList(data)
                    }
                },
                doOnEmpty = {
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.rvListCoin.isVisible = false
                },
            )
        }
    }

    private fun bindCoinList(data: List<Coin>) {
        coinAdapter =
            CoinAdapter(
                listener =
                    object : OnItemCLickedListener<Coin> {
                        override fun onItemClicked(item: Coin) {
                            navigateToDetail(item)
                        }
                    },
            )
        binding.rvListCoin.adapter = this@HomeFragment.coinAdapter
        coinAdapter?.submitData(data)
    }

    private fun navigateToDetail(item: Coin) {
        startActivity(
            Intent(activity, DetailActivity::class.java).putExtra(
                DetailActivity.EXTRAS_ITEM_ACT,
                item,
            ),
        )
    }
}
