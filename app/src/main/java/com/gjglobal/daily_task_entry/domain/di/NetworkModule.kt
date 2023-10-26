package com.gjglobal.daily_task_entry.domain.di

import android.content.Context
import com.gjglobal.daily_task_entry.core.Constants
import com.gjglobal.daily_task_entry.domain.data.cache.CacheManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

//    @Provides
//    @Singleton
//    @Named("LocalRetrofit")
//    fun provideGateRetrofit(okHttpClient: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(Constants.LOCAL_URL)
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }


    // Custom interceptor to handle redirection (HTTP 302)
    private val redirectionInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val response = chain.proceed(originalRequest)

            if (response.code == 302) {
                val redirectedUrl = response.header("Location")
                // Handle the redirection as required (e.g., you may choose to retry with the new URL)
                // ...
                // For now, we'll just return the original response
                return response
            }
            return response
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cacheManager: CacheManager): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        val clientBuilder = OkHttpClient.Builder()
            //.addInterceptor(redirectionInterceptor)
        // if (BuildConfig.DEBUG) {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(httpLoggingInterceptor)
//            clientBuilder.addInterceptor(DomainInterceptor())
        clientBuilder.cache(null)

        //  }
        clientBuilder.readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)

            .addNetworkInterceptor { chain: Interceptor.Chain ->
                //val accessToken = cacheManager.getAuthResponse()?.data?.tokenDate?.access_token

                val accessToken = "cacheManager.getAuthResponse()?.data?.tokenDate?.access_token"

                val originalRequest: Request = chain.request()
                if (!shouldAddBearerToken(originalRequest)) {
                    val modifiedRequest:Request = originalRequest.newBuilder()
                        .addHeader("Authorization", "Bearer$accessToken")
                        .build()
                    chain.proceed(modifiedRequest)
                } else {
                    chain.proceed(originalRequest)
                }
            }

        return clientBuilder.build()
    }

    private fun shouldAddBearerToken(originalRequest: Request): Boolean {
        // Check if the bearer token should be added based on the API endpoint
        val url = originalRequest.url.toString()
        return url.contains("login") || url.contains("forgot-password-for-a-user")
    }

    @Provides
    @Singleton
    fun provideCacheManager(@ApplicationContext appContext: Context): CacheManager {
        return CacheManager(appContext)
    }
}