package com.arkan.a4crypto.di

// object AppModules {
//    object AppModules {
// //        private val networkModule =
// //            module {
// //                single<FoodiesApiService> { FoodiesApiService.invoke() }
// //            }
//
//        private val firebaseModule =
//            module {
//                single<FirebaseService> { FirebaseServiceImpl() }
//            }
// //        private val localModule =
// //            module {
// ////                single<AppDatabase> { AppDatabase.createInstance(androidContext()) }
// ////                single<CartDao> { get<AppDatabase>().cartDao() }
// //                single<SharedPreferences> {
// //                    SharedPreferenceUtils.createPreference(
// //                        androidContext(),
// //                        UserPreferenceImpl.PREF_NAME,
// //                    )
// //                }
// //                single<UserPreference> { UserPreferenceImpl(get()) }
// //            }
//
//        private val datasource =
//            module {
// //                single<CartDataSource> { CartDatabaseDataSource(get()) }
// //                single<CategoryDataSource> { CategoryApiDataSource(get()) }
// //                single<MenuDataSource> { MenuApiDataSource(get()) }
// //                single<UserDataSource> { UserDataSourceImpl(get()) }
//                single<AuthDataSource> { FirebaseAuthDataSource(get()) }
//            }
//        private val repository =
//            module {
// //                single<CartRepository> { CartRepositoryImpl(get()) }
// //                single<CategoryRepository> { CategoryRepositoryImpl(get()) }
// //                single<MenuRepository> { MenuRepositoryImpl(get()) }
//                single<UserRepository> { UserRepositoryImpl(get()) }
//            }
//
//        private val viewModelModule = module {
//            viewModelOf(::HomeViewModel)
//            viewModelOf(::LoginViewModel)
//            viewModelOf(::RegisterViewModel)
//            viewModelOf(::ProfileViewModel)
//            viewModel { MainViewModel(get()) }
//        }
//        val modules =
//            listOf(
// //                networkModule,
// //                localModule,
//                datasource,
//                repository,
//                firebaseModule,
//                viewModelModule,
//            )
//    }
// }
