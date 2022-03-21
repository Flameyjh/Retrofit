package com.yjh.retrofit

import com.google.gson.Gson
import com.yjh.retrofit.bean.BaseResponse
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.*
import org.junit.Test
import org.reactivestreams.Publisher
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WanAndroidUnitTest {


    //直接用Gson, 将返回的Json数据转换为Java对象
    @Test
    fun loginTest(){
        val retrofit = Retrofit.Builder().baseUrl("https://www.wanandroid.com/").build()
        val wanAndroidService = retrofit.create(WanAndroidService::class.java)

        val response = wanAndroidService.login("yjhyjh", "123456").execute()
        val result = response.body()?.string()
        val baseResponse = Gson().fromJson(result, BaseResponse::class.java)
        System.out.println("WanAndroidUnitTest.loginTest: \n" + baseResponse)
    }

    //用Gson转换器, 将返回的Json数据转换为Java对象
    @Test
    fun loginCnverterTest(){
        val retrofit2 = Retrofit.Builder().baseUrl("https://www.wanandroid.com/")
            .addConverterFactory(GsonConverterFactory.create()).build() //添加Gson转换器
        val wanAndroidService2 = retrofit2.create(WanAndroidService::class.java)

        val response = wanAndroidService2.loginConverter("yjhyjh", "123456").execute()
        val baseResponse = response.body()
        System.out.println("WanAndroidUnitTest.loginCnverterTest: \n" + baseResponse)
    }

    //用rxjava实现嵌套请求, 先请求接口A再请求接口B（先登录在请求收藏列表）
    @Test
    fun rxjavaTest(){
        val cookieList: HashMap<String, List<Cookie>> = HashMap()
        val okHttpClient = OkHttpClient.Builder()
            .cookieJar(object : CookieJar{ //添加cookie关键代码
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    val cookies = cookieList.get(url.host())
                    if (cookies == null){
                        return ArrayList()
                    }
                    return cookies
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    cookieList.put(url.host(), cookies)
                }

            })
            .build()

        val retrofit3 = Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com/")
            .callFactory(okHttpClient) //添加cookie，采用定制的okhttp
            .addConverterFactory(GsonConverterFactory.create()) //添加Gson转换器
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) //添加适配器
            .build()
        val wanAndroidService3 = retrofit3.create(WanAndroidService::class.java)

        wanAndroidService3.loginFlowable("yjhyjh", "123456")
             .flatMap(object: Function<BaseResponse, Publisher<ResponseBody>>{
                 override fun apply(t: BaseResponse?): Publisher<ResponseBody> {
                     return wanAndroidService3.getArticle(0)
                 }

             })
            .observeOn(Schedulers.io()) //切换线程
            .subscribeOn(Schedulers.newThread()) //切换线程，如果在Android环境中应写成AndroidSchedulers.mainThread()
            .subscribe(object : io.reactivex.rxjava3.functions.Consumer<ResponseBody> {
                override fun accept(p0: ResponseBody) {
                    System.out.println("WanAndroidUnitTest.rxjavaTest: \n" + p0.string())
                }

            })
        while (true){} //阻塞，等待结果输出
    }


}
