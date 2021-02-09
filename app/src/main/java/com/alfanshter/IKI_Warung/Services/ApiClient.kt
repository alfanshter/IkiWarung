package com.alfanshter.IKI_Warung.Services

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object{
        private var retrofit : Retrofit? = null
        const val BASE_URL = "http://103.149.239.72/"
        private var opt = OkHttpClient.Builder().apply {
            connectTimeout(30,TimeUnit.SECONDS)
            readTimeout(30,TimeUnit.SECONDS)
            writeTimeout(30,TimeUnit.SECONDS)
        }.build()

        private fun getClient() : Retrofit{
            return if(retrofit == null){
                retrofit = Retrofit.Builder().apply {
                    client(opt)
                    baseUrl(BASE_URL)
                    addConverterFactory(GsonConverterFactory.create())
                }.build()
                retrofit!!
            }else{
                retrofit!!
            }
        }

        fun instance() = getClient().create(ApiService::class.java)



    }
}