package com.yjh.retrofit

import com.google.gson.Gson
import com.yjh.retrofit.bean.BaseResponse
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WanAndroidUnitTest {
    val retrofit = Retrofit.Builder().baseUrl("https://www.wanandroid.com/").build()
    val wanAndroidService = retrofit.create(WanAndroidService::class.java)

    val retrofit2 = Retrofit.Builder().baseUrl("https://www.wanandroid.com/")
        .addConverterFactory(GsonConverterFactory.create()).build() //添加Gson转换器的retrofit
    val wanAndroidService2 = retrofit2.create(WanAndroidService::class.java)


    //直接用Gson 将返回的Json数据转换为Java对象
    @Test
    fun loginTest(){
        val response = wanAndroidService.login("yjhyjh", "123456").execute()
        val result = response.body()?.string()
        val baseResponse = Gson().fromJson(result, BaseResponse::class.java)
        System.out.println("WanAndroidUnitTest.loginTest: \n" + baseResponse)
    }

    //用Gson转换器 将返回的Json数据转换为Java对象
    @Test
    fun loginCnverterTest(){
        val response = wanAndroidService2.loginConverter("yjhyjh", "123456").execute()
        val baseResponse = response.body()
        System.out.println("WanAndroidUnitTest.loginCnverterTest: \n" + baseResponse)
    }
}