package com.yjh.retrofit

import android.view.View
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.functions.Function
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.io.*
import java.io.FileDescriptor.out

class UploadFileUnitTest {

    val retrofit = Retrofit.Builder().baseUrl("https://www.httpbin.org/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) //集成Retrofit2+RxJava3时，不加这行可能出错
        .build()
    val uploadService = retrofit.create(UploadService::class.java)

    //上传文件，使用vararg上传多个文件
    @Test
    fun uploadFileTest() {
        val file1 = File("E:\\learn\\AndroidProject\\Retrofit\\1.txt")
        val file2 = File("E:\\learn\\AndroidProject\\Retrofit\\2.txt")
        val part1 = MultipartBody.Part.createFormData(
            "file1", file1.name, RequestBody.create(
                MediaType.parse("text/plain"), file1
            )
        )
        val part2 = MultipartBody.Part.createFormData(
            "file2", file2.name, RequestBody.create(
                MediaType.parse("text/plain"), file2
            )
        )
        val response: Response<ResponseBody> = uploadService.upload(part1, part2).execute()
        System.out.println("WanAndroidUnitTest.uploadFileTest: \n" + response.body()?.string())
    }

    //上传文件，使用@PartMap上传多个文件
    @Test
    fun uploadManyFileTest() {
        val file1 = File("E:\\learn\\AndroidProject\\Retrofit\\1.txt")
        val file2 = File("E:\\learn\\AndroidProject\\Retrofit\\2.txt")
        val map: HashMap<String, RequestBody> = HashMap()
        map.put("file1", RequestBody.create(MediaType.parse("text/plain"), file1))
        map.put("file2", RequestBody.create(MediaType.parse("text/plain"), file2))
        val response: Response<ResponseBody> = uploadService.uploadMany(map).execute()
        System.out.println("WanAndroidUnitTest.uploadFileTest: \n" + response.body()?.string())
    }

    //上传文件，更复杂的方法，完全的多余。。
    @Test
    fun uploadFileBodyTest() {
        val file1 = File("E:\\learn\\AndroidProject\\Retrofit\\1.txt")
        val file2 = File("E:\\learn\\AndroidProject\\Retrofit\\2.txt")
        val multipartBody = MultipartBody.Builder()
            .addFormDataPart(
                "file1", file1.name, RequestBody.create(
                    MediaType.parse("text/plain"), file1 //这里的contentType（create的第一个参数）取决于上传的数据
                )
            )
            .addFormDataPart(
                "file2", file2.name, RequestBody.create(
                    MediaType.parse("text/plain"), file2
                )
            )
            .build()
        val response: Response<ResponseBody> =
            uploadService.upload(multipartBody.part(0), multipartBody.part(1)).execute()
        System.out.println("WanAndroidUnitTest.uploadFileBodyTest: \n" + response.body()?.string())
    }

    //下载文件，简单写法
    @Test
    fun downloadTest() {
        val response = uploadService.download("https://xyz.51job.com/xslx/gpa.xlsx?v=3").execute()
        val body = response.body()

        if (body != null) {
            val inputStream = body.byteStream()
            val fileOutputStream = FileOutputStream("E:\\learn\\AndroidProject\\Retrofit\\a.xlsx")
            val buffer = ByteArray(1024)
            var len = inputStream.read(buffer)
            while (len != -1) {
                fileOutputStream.write(buffer, 0, len)
                len = inputStream.read(buffer)
            }
            inputStream.close()
            fileOutputStream.close()
        }
    }

    //下载文件，更全面的写法
    @Test
    fun downloadTotalTest() {
        val response = uploadService.download("https://xyz.51job.com/xslx/gpa.xlsx?v=3").execute()
        val body = response.body()

        if (body != null) { //返回的body为空就不存了
            var inStream: InputStream? = null
            var outStream: OutputStream? = null
            try {
                //以下读写文件的操作和java类似
                inStream = body.byteStream()
                outStream = FileOutputStream("E:\\learn\\AndroidProject\\Retrofit\\b.xlsx")
                //文件总长度
                val contentLength = body.contentLength()
                //当前已下载长度
                var currentLength = 0L
                //缓冲区
                val buff = ByteArray(1024)
                var len = inStream.read(buff)
                var percent = 0
                while (len != -1) {
                    outStream.write(buff, 0, len)
                    currentLength += len

                    len = inStream.read(buff)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                inStream?.close()
                outStream?.close()
            }

        }
    }

    //下载文件，rxjava的写法
    @Test
    fun downloadRxjavaTest() {
        uploadService.downloadRxJava("https://xyz.51job.com/xslx/gpa.xlsx?v=3")
            .map(object : Function<ResponseBody, File> {
                override fun apply(t: ResponseBody?): File {
                    val inputStream = t!!.byteStream()
                    val file = File("E:\\learn\\AndroidProject\\Retrofit\\c.xlsx")
                    val fileOutputStream = FileOutputStream(file)
                    val buffer = ByteArray(1024)
                    var len = inputStream.read(buffer)
                    while (len != -1) {
                        fileOutputStream.write(buffer, 0, len)
                        len = inputStream.read(buffer)
                    }
                    inputStream.close()
                    fileOutputStream.close()
                    return file
                }
            }).subscribe(object : Consumer<File> {
                override fun accept(t: File?) {

                }

            })
        while (true) {
        } //阻塞，等待结果输出
    }

}