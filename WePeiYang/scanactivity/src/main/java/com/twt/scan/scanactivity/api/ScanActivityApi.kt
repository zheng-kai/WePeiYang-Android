package com.twt.scan.scanactivity.api

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ScanActivityApi {

    @POST("api/QrCode/scan")
    fun sign(@Query("activity_id") activity_id: Int,
                 @Query("student_number") student_number: String,
                 @Query("student_name") student_name: String,
                 @Query("time") time: Int): Deferred<CommonBody<Any>>

    @POST("api/QrCode/scan")
    fun sign(@Query("activity_id") activity_id: Int,
             @Query("student_number") student_number: String,
             @Query("time") time: Int): Deferred<CommonBody<Any>>

    @GET("api/activity/index")
    fun getActivities(@Query("page") page: Int,
                      @Query("limit") limit: Int,
                      @Query("token") token: String,
                      @Query("method") method: Int ): Deferred<CommonBody<List<ActivityBean>>>

    @GET("api/user/register/checkManager")
    fun checkManager(@Query("user_id") user_id: Long): Deferred<CommonBody<Any>>

    @GET("user/getNameByNumber")
    fun getNameByNumber(@Query("student_number") student_number:String) :Deferred<CommonBody<String>>

    companion object : ScanActivityApi by ActivityServiceFactory()
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

data class QRInfo(
        val name: String,
        val id: String,
        val time: Long
)