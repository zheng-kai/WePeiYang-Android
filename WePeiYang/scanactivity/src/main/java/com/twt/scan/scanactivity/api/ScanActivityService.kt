package com.twt.scan.scanactivity.api

import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

const val BASE_URL = "https://activity.twt.edu.cn/api/"

interface ScanActivityService {

    @GET("${BASE_URL}openLogin")
    fun login(@Query("twtid") twtId: Int? = ScanPreferences.twtid): Deferred<CommonBody<LoginBean>>

    @POST("${BASE_URL}QrCode/scan")
    fun sign(
            @Query("activity_id") activity_id: Int,
            @Query("student_number") student_number: String,
            @Query("time") time: Long, // 时间戳单位为毫秒
            @Query("twtid") twtId: Int? = ScanPreferences.twtid): Deferred<CommonBody<Any>>

    @GET("${BASE_URL}activity/index")
    fun getActivitiesAsync(
            @Query("page") page: Int,
            @Query("method") method: Int,
            @Query("limit") limit: Int = 10,
            @Query("twtid") twtId: Int? = ScanPreferences.twtid
    ): Deferred<CommonBody<ActivityBean>>

//    @GET("${BASE_URL}user/register/checkManager")
//    fun checkManager(
//            @Query("user_id") user_id: String,
//            @Query("twtid") twtId: Int? = ScanPreferences.twtid): Deferred<CommonBody<Any>>

    @GET("${BASE_URL}user/getNameByNumber")
    fun getNameByNumber(
            @Query("student_number") student_number: String,
            @Query("twtid") twtId: Int? = ScanPreferences.twtid): Deferred<CommonBody<String>>

    @GET("${BASE_URL}user/info")
    fun getUserInfo(): Deferred<CommonBody<UserInfoBean>>

    companion object : ScanActivityService by ServiceFactory()
}

data class UserInfoBean(
        val permission: Int,
        val token: List<String>,
        val user_id: Int
)

data class LoginBean(
        val permission: Int,
        val token: List<String>,
        val user_id: Int
)

data class ActivityBean(
        val currentPage: Int,//当前页数
        val data: List<Details>,
        val lastPage: Int,//最后一页的页数
        val number: Int//活动的总数量
)

data class Details(
        val activity_id: Int,//活动id
        val content: String, //活动内容
        val start: String, //开始时间的时间戳
        val end: String, //结束时间的时间戳
        val `file`: File?,
        val isStarter: Int,//为1则是该用户发起的活动，为0则不是
        val numberOfManager: Int,//管理者数量
        val manager: List<Manager>,//管理者
        val numberOfWorker: Int,
        val worker: List<Worker>,//工作人员
        val position: String,//活动位置
        val title: String,//活动标题
        val teacher: String//活动发起者的姓名
)

data class File(
        val file_id: Int,//文件的id
        val file_name: String,//文件的名称
        val url: String//文件的位置
)

data class Manager(
        val name: String,//管理员姓名
        val student_number: String//管理员学号
)

data class Worker(
        val name: String,
        val student_number: String
)
