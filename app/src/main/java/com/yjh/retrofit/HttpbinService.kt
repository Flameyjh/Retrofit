package com.yjh.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

//第一步：根据后端提供的Http接口创建Kotlin接口

interface HttpbinService {

    @GET("get")
    fun get(@Query("username") username: String, @Query("password") psw: String): Call<ResponseBody>

    @POST("post")
    @FormUrlEncoded
    fun post(@Field("username") username: String, @Field("password") psw: String): Call<ResponseBody>
}