package com.yjh.retrofit


import okhttp3.FormBody
import org.junit.Test
import retrofit2.Retrofit

//测试HttpbinService.XX方法
//用单元测试以免每次要打包到手机上

class AnnotionUnitTest {
    val retrofit = Retrofit.Builder().baseUrl("https://www.httpbin.org/").build()
    val httpbinService = retrofit.create(HttpbinService::class.java)

    @Test
    fun bodyTest(){
        val formBody: FormBody = FormBody.Builder().add("a", "1").add("b", "2").build()
        val response = httpbinService.postBody(formBody).execute()
        System.out.println(response.body()?.string())
    }

    @Test
    fun pathTest(){
        //https://www.httpbin.org/post
        val response =  httpbinService.postInPath("post", "android").execute()
        System.out.println(response.body()?.string())
    }

    @Test
    fun headersTest(){
        val response =  httpbinService.postWithHeaders().execute()
        System.out.println(response.body()?.string())
    }

    @Test
    fun urlTest(){
        val response =  httpbinService.postUrl("https://www.httpbin.org/post").execute()
        System.out.println(response.body()?.string())
    }
}