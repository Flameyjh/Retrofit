package com.yjh.retrofit

import io.reactivex.rxjava3.core.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UploadService {

    //@Part上传单个文件，需要多个可以增加参数
    @POST("post")
    @Multipart
    fun upload(@Part vararg file: MultipartBody.Part): Call<ResponseBody> //vararg表示可变长度参数

    //@PartMap上传多个文件
    @POST("post")
    @Multipart
    fun uploadMany(@PartMap map: HashMap<String, RequestBody>): Call<ResponseBody>

    //@下载文件
    @GET
    @Streaming //该注解有效避免内存溢出问题，文件过大必须加
    fun download(@Url url: String): Call<ResponseBody>

    //@下载文件
    @GET
    @Streaming
    fun downloadRxJava(@Url url: String): Flowable<ResponseBody>
}