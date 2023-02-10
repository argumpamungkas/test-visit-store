package com.argump.visitstore.source

import com.argump.visitstore.util.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val moduleNetwork = module {
    single { provideOkhttp() }
    single { provideRetrofit(get()) }
    single { provideApi(get()) }
}

private fun provideOkhttp(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun provideApi(retrofit: Retrofit): ApiEndpoint = retrofit.create(ApiEndpoint::class.java)