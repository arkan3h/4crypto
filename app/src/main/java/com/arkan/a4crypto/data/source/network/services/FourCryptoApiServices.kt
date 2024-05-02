package com.arkan.a4crypto.data.source.network.services

import com.arkan.a4crypto.BuildConfig
import com.arkan.a4crypto.data.source.network.model.CoinDetailResponse
import com.arkan.a4crypto.data.source.network.model.CoinResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface FourCryptoApiServices {
    @GET("markets")
    suspend fun getCoinList(
        @Query("vs_currency") vs_currency: String = "usd",
    ): List<CoinResponse>

    @GET("{id}")
    suspend fun getCoinDetail(
        @Path("id") id: String?,
    ): CoinDetailResponse

    companion object {
        @JvmStatic
        operator fun invoke(): FourCryptoApiServices {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClient =
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor { chain ->
                        val request =
                            chain.request().newBuilder()
                                .addHeader("accept", "application/json")
                                .addHeader("x-cg-demo-api-key", BuildConfig.API_KEY)
                        chain.proceed(request.build())
                    }
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build()
            val retrofit =
                Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
            return retrofit.create(FourCryptoApiServices::class.java)
        }
    }
}
