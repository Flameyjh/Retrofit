package com.yjh.retrofit

import okhttp3.RequestBody
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

    @HTTP(method = "POST", path = "get", hasBody = true)
    fun http(@Query("username") username: String, @Query("password") psw: String): Call<ResponseBody>

    @POST("post")
    fun postBody(@Body body: RequestBody): Call<ResponseBody> //@Body注解表示自己指定传参方式

    @POST("{id}")
    fun postInPath(
        @Path("id") path: String, //@Path注解表示用path替换{id}，例如请求第几页数据时用到
        @Header("os") os: String //@Header注解用于加请求头
    ): Call<ResponseBody>

    @Headers("os:android","version:1.0") //@Headers注解用于写死请求头
    @POST("post")
    fun postWithHeaders(): Call<ResponseBody>

    @POST
    fun postUrl(@Url url: String): Call<ResponseBody> //@Url注解指定此次请求的完整的url地址，一般情况是Retrofit的baseUrl拼上注解里的Path
}