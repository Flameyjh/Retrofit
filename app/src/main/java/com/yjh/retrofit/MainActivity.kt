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
    }
}