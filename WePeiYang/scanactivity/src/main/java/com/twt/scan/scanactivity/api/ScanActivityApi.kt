package com.twt.scan.scanactivity.api

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

const val BASE_URL = "https://activity.twt.edu.cn/api/"
interface ScanActivityApi {

    @POST("${BASE_URL}QrCode/scan")
    fun sign(@Query("activity_id") activity_id: Int,
                 @Query("student_number") student_number: String,
                 @Query("student_name") student_name: String,
                 @Query("time") time: Int): Deferred<CommonBody<Any>>

    @POST("${BASE_URL}QrCode/scan")
    fun sign(@Query("activity_id") activity_id: Int,
             @Query("student_number") student_number: String,
             @Query("time") time: Int): Deferred<CommonBody<Any>>

    @GET("${BASE_URL}activity/index")
    fun getActivities(@Query("page") page: Int,
                      @Query("limit") limit: Int,
                      @Query("method") method: Int ): Deferred<CommonBody<List<ActivityBean>>>

    @GET("${BASE_URL}user/register/checkManager")
    fun checkManager(@Query("user_id") user_id: Long): Deferred<CommonBody<Any>>

    @GET("${BASE_URL}user/getNameByNumber")
    fun getNameByNumber(@Query("student_number") student_number:String) :Deferred<CommonBody<String>>

    companion object : ScanActivityApi by ServiceFactory()
}

data class ActivityBean(
        val activity_id: Int,
        val content: String,
        val currentPage: Int,
        val end: String,
        val lastPage: Int,
        val manager: List<String>,
        val numberOfCurrentPage: Int,
        val numberOfManager: Int,
        val position: String,
        val start: String,
        val teacher: String,
        val title: String
)
