package com.example.sightsafe.data

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(

    @field:SerializedName("data")
    var data: Data = Data(),

    @field:SerializedName("message")
    var message: String? = null
)

data class Data(

    @field:SerializedName("result")
    var result: String? = null,

    @field:SerializedName("createdAt")
    var createdAt: String? = null,

    @field:SerializedName("confidenceScore")
    var confidenceScore: Any? = null,

    @field:SerializedName("isAboveThreshold")
    var isAboveThreshold: Boolean? = null,

    @field:SerializedName("recommendation")
    var recommendation: String? = null,

    @field:SerializedName("id")
    var id: String? = null
)