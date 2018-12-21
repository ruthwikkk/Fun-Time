package com.ruthwik.funtime.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object VideosService {

    private const val CLOUDINARY_BASE_URL = "https://res.cloudinary.com/"

    private fun createRetrofitInstance() = Retrofit.Builder()
        .baseUrl(CLOUDINARY_BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun createCloudinaryVideoService() = createRetrofitInstance().create(CloudinaryApi::class.java)

    fun fetchVideos() = createCloudinaryVideoService().fetchVideos()
}