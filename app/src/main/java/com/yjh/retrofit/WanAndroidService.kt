package com.yjh.retrofit

import com.yjh.retrofit.bean.BaseResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface WanAndroidService {

    @POST("/user/login")
    @FormUrlEncoded
    fun login(@Field("username") username: String, @Field("password") psw: String): Call<ResponseBody>

    @POST("/user/login")
    @FormUrlEncoded
    fun loginConverter(@Field("username") username: String, @Field("password") psw: String): Call<BaseResponse>
}