package com.bertalandancs.otpflickrsearcher.data

import android.util.Log
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

private const val apiKey: String = "65803e8f6e4a3982200621cad356be51"
private const val baseUrl: String = "https://www.flickr.com/services/rest/"

val networkModule = module {

    fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor {
                val currentUrl = it.request().url()
                val newRequest = it.request()
                    .newBuilder()
                    .url("$currentUrl&api_key=$apiKey")
                    .build()
                Log.d("OkHttp", "request url: ${newRequest.url()}")
                it.proceed(newRequest)
            }
            .build()
    }

    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(SimpleXmlConverterFactory.create()) //TODO: Change to JAXB converter
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    single { provideClient() }
    single { provideRetrofit(get()) }
}
