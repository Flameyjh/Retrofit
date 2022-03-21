package com.yjh.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var retrofit: Retrofit
    lateinit var httpbinService: HttpbinService
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofit = Retrofit.Builder().baseUrl("https://www.httpbin.org/").build()
        httpbinService = retrofit.create(HttpbinService::class.java)
    }

    fun postAsync(view: View) {
        val call  = httpbinService.post("yjhyjh", "123456")
        call.enqueue(object : retrofit2.Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    Log.i(TAG, "postAsync: "+ response.body()?.string())
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }

        })

        //----------------与okhttp进行对比,retrofit不需要创建requestBody和request-----------------------
        //----------------okhttp发送post请求---------------------------------------------------------
//        val formBody = FormBody.Builder().add("a", "1").add("b", "2").build()
//        val request = Request.Builder().url("https://www.httpbin.org/post").post(formBody).build()
//        //准备好请求的Call对象
//        val call = okHttpClient.newCall(request)
//        call.enqueue(object : Callback { //enqueue发起异步请求，内部自动创建子线程
//            override fun onFailure(call: okhttp3.Call, e: IOException) {
//
//            }
//
//            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
//                if (response.isSuccessful){
//                    Log.i(TAG, "postAsync:" + response.body?.string())
//                }
//            }
//        })
        //------------------------------------------------------------------------------------------
    }
}