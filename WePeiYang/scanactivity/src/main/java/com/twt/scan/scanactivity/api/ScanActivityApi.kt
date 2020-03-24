package com.twt.scan.scanactivity.api

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

const val BASE_URL = "https://activity.twt.edu.cn/api/"

interface ScanActivityApi {

    @GET("${BASE_URL}user/login")
    fun login(): Deferred<CommonBody<LoginBean>>

    @POST("${BASE_URL}QrCode/scan")
    fun sign(@Query("activity_id") activity_id: Int,
             @Query("student_number") student_number: String,
             @Query("time") time: Int): Deferred<CommonBody<Any>>

    @GET("${BASE_URL}activity/index")
    fun getActivitiesAsync(@Query("page") page: Int,
                           @Query("limit") limit: Int,
                           @Query("method") method: Int): Deferred<CommonBody<ActivityBean>>

    @GET("${BASE_URL}user/register/checkManager")
    fun checkManager(@Query("user_id") user_id: Int): Deferred<CommonBody<Any>>

    @GET("${BASE_URL}user/getNameByNumber")
    fun getNameByNumber(@Query("student_number") student_number: String): Deferred<CommonBody<String>>

    companion object : ScanActivityApi by ServiceFactory()
}

data class LoginBean(
        val permission: Int,
        val user_id: Int
)

data class ActivityBean(
        val currentPage: Int,
        val details: List<Details>,
        val lastPage: Int,
        val number: Int
)

data class Details(
        val activity_id: Int,
        val content: String,
        val end: String,
        val `file`: File,
        val isStarter: Int,
        val manager: List<Manager>,
        val numberOfManager: Int,
        val numberOfWorker: Int,
        val position: String,
        val start: String,
        val teacher: String,
        val title: String,
        val worker: List<Worker>
)

data class File(
        val file_id: Int,
        val file_name: String,
        val url: String
)

data class Manager(
        val name: String,
        val student_number: String
)

data class Worker(
        val name: String,
        val student_number: String
)
