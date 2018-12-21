package com.ruthwik.funtime.model

import com.google.gson.annotations.SerializedName

data class ApiVideo(@SerializedName ("public_id") val publicId: String,
                    val version: Long,
                    val format: String,
                    val width: Int,
                    val height: Int,
                    val type: String,
                    @SerializedName("created_at") val createdAt: String)

data class ApiResponse(val resources: List<ApiVideo>,
                       @SerializedName("updated_at") val updatedAt: String)