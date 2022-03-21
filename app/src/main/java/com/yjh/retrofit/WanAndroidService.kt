package com.yjh.retrofit

import com.yjh.retrofit.bean.BaseResponse
import io.reactivex.rxjava3.core.Flowable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface WanAndroidService {

    @POST("/user/login")
    @FormUrlEncoded
    fun login(@Field("username") username: String, @Field("password") psw: String): Call<ResponseBody>

    @POST("/user/login")
    @FormUrlEncoded
    fun loginConverter(@Field("username") username: String, @Field("password") psw: String): Call<BaseResponse>

    @POST("/user/login")
    @FormUrlEncoded
    fun loginFlowable(@Field("username") username: String, @Field("password") psw: String): Flowable<BaseResponse>

    @GET("lg/collect/list/{pageNum}/json")
    fun getArticle(@Path("pageNum") pageNum: Int): Flowable<ResponseBody>
}