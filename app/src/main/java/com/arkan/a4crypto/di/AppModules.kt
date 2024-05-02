package com.arkan.a4crypto.di

import com.arkan.a4crypto.data.datasource.CoinDataSource
import com.arkan.a4crypto.data.datasource.CoinDataSourceImpl
import com.arkan.a4crypto.data.datasource.CoinDetailDataSource
import com.arkan.a4crypto.data.datasource.CoinDetailDataSourceImpl
import com.arkan.a4crypto.data.datasource.auth.AuthDataSource
import com.arkan.a4crypto.data.datasource.auth.FirebaseAuthDataSource
import com.arkan.a4crypto.data.datasource.favorite.FavoriteDataSource
import com.arkan.a4crypto.data.datasource.favorite.FavoriteDatabaseDataSource
import com.arkan.a4crypto.data.repository.CoinDetailRepository
import com.arkan.a4crypto.data.repository.CoinDetailRepositoryImpl
import com.arkan.a4crypto.data.repository.CoinRepository
import com.arkan.a4crypto.data.repository.CoinRepositoryImpl
import com.arkan.a4crypto.data.repository.FavoriteRepository
import com.arkan.a4crypto.data.repository.FavoriteRepositoryImpl
import com.arkan.a4crypto.data.repository.UserRepository
import com.arkan.a4crypto.data.repository.UserRepositoryImpl
import com.arkan.a4crypto.data.source.firebase.FirebaseService
import com.arkan.a4crypto.data.source.firebase.FirebaseServiceImpl
import com.arkan.a4crypto.data.source.network.services.FourCryptoApiServices
import com.arkan.a4crypto.presentation.detail.DetailViewModel
import com.arkan.a4crypto.presentation.home.HomeViewModel
import com.arkan.a4crypto.presentation.login.LoginViewModel
import com.arkan.a4crypto.presentation.profile.ProfileViewModel
import com.arkan.a4crypto.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object AppModules {
    private val networkModule =
        module {
            single<FourCryptoApiServices> { FourCryptoApiServices.invoke() }
        }

//    how to setup using database

    private val firebaseModule =
        module {
            single<AuthDataSource> { FirebaseAuthDataSource(get()) }
            single<FirebaseService> { FirebaseServiceImpl() }
        }

    private val datasource =
        module {
            single<CoinDataSource> { CoinDataSourceImpl(get()) }
            single<CoinDetailDataSource> { CoinDetailDataSourceImpl(get()) }
            single<FavoriteDataSource> { FavoriteDatabaseDataSource(get()) }
        }

    private val repository =
        module {
            single<CoinRepository> { CoinRepositoryImpl(get()) }
            single<UserRepository> { UserRepositoryImpl(get()) }
            single<CoinDetailRepository> { CoinDetailRepositoryImpl(get()) }
            single<FavoriteRepository> { FavoriteRepositoryImpl(get()) }
        }

    private val viewModelModule =
        module {
            viewModelOf(::HomeViewModel)
            viewModelOf(::LoginViewModel)
            viewModelOf(::RegisterViewModel)
            viewModelOf(::ProfileViewModel)
            viewModelOf(::DetailViewModel)
        }

    val modules =
        listOf(
            networkModule,
            datasource,
            repository,
            firebaseModule,
            viewModelModule,
        )
}
