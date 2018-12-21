package com.ruthwik.funtime.api

import com.ruthwik.funtime.model.ApiResponse
import io.reactivex.Single
import retrofit2.http.GET

interface CloudinaryApi {
    @GET("demo/video/list/samples.json")
    fun fetchVideos(): Single<ApiResponse>
}