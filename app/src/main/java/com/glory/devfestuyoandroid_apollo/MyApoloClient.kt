package com.pdl.graghqlexample

import com.apollographql.apollo.ApolloClient

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object MyApoloClient {
    private val BASE_URL = "https://api.graph.cool/simple/v1/cjxxjdsj129t50154n4knqazf"
    private var myApoloClient: ApolloClient? = null

    fun getMyApoloClient(): ApolloClient? {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        myApoloClient = ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(client)
            .build()

        return myApoloClient

    }
}
