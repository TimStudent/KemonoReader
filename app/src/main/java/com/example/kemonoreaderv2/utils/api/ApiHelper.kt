package com.example.kemonoreaderv2.utils.api
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiHelper {
    @Streaming
    @GET
    suspend fun downloadFile(
        @Url
        fileUrl: String
    ): Response<ResponseBody>

    companion object {
        const val BASE_URL = "https://kemono.party/"
        val serviceApi: ApiHelper = ApiService.downloadService
    }
}