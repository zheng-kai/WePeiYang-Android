package com.avarye.mall.service

import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import kotlinx.coroutines.Deferred
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object MallManager {
    private val cookies = mutableListOf<String>()//测试用

    //manager
    fun loginAsync() = MallApi.loginAsync(getToken())

//    fun getMyInfoAsync() = MallApi.getMyInfoAsync()

    fun changeMyInfoAsync(phone: String, email: String, qq: String, campus: Int) = MallApi.changeMyInfoAsync(
            phone = toReqBody(phone),
            email = toReqBody(email),
            qq = toReqBody(qq),
            campus = toReqBody(campus))

    fun changeCampusAsync(campus: Int) = MallApi.changeCampusAsync(toReqBody(campus))

    fun latestSaleAsync(page: Int) = MallApi.latestSaleAsync(toReqBody(page), toReqBody(1))

    fun latestNeedAsync(page: Int) = MallApi.latestNeedAsync(toReqBody(page), toReqBody(2))

    fun selectAsync(category: String, which: Int, page: Int) = MallApi.selectAsync(
            which = toReqBody(which),
            page = toReqBody(page),
            category = toReqBody(category))

    fun searchAsync(key: String, page: Int) = MallApi.searchAsync(toReqBody(key), toReqBody(page))

//    fun getMenuAsync() = MallApi.getMenuAsync()

    fun getDetailAsync(id: String) = MallApi.getDetailAsync(id)

    fun getSellerInfoAsync(gid: String, token: String) = MallApi.getSellerInfoAsync(toReqBody(token), toReqBody(gid))

    fun getUserInfoAsync(id: String) = MallApi.getUserInfoAsync(id)

    fun postImgAsync(file: File, token: String): Deferred<Result> {

        val imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)

        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("file", file.name, imageBody)

        val list = builder.build().parts()
        return MallApi.postImgAsync(list)
    }

    fun favAsync(gid: String, token: String) = MallApi.favAsync(toReqBody(token), toReqBody(gid))

    fun deFavAsync(gid: String, token: String) = MallApi.deFavAsync(toReqBody(token), toReqBody(gid))

    fun getFavListAsync(token: String) = MallApi.getFavListAsync(toReqBody(token))

    fun getCommentListAsync(id: String, which: Int, token: String) = MallApi.getCommentListAsync(
            token = toReqBody(token),
            id = toReqBody(id),
            which = toReqBody(which))

    fun commentAsync(id: String, which: Int, content: String, token: String) = MallApi.commentAsync(
            token = toReqBody(token),
            content = toReqBody(content),
            which = toReqBody(which),
            tid = toReqBody(id))

    fun getReplyListAsync(id: String, token: String) = MallApi.getReplyListAsync(toReqBody(token), toReqBody(id))

    fun replyAsync(id: String, content: String, token: String) = MallApi.replyAsync(
            token = toReqBody(token),
            cid = toReqBody(id),
            content = toReqBody(content))

    fun postSaleAsync(map: Map<String, Any>, token: String): Deferred<Result> {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("name", map["name"].toString())
                .addFormDataPart("desc", map["detail"].toString())
                .addFormDataPart("campus", map["campus"].toString())
                .addFormDataPart("location", map["location"].toString())
                .addFormDataPart("price", map["price"].toString())
                .addFormDataPart("bargain", map["bargain"].toString())
                .addFormDataPart("category", map["category"].toString())
                .addFormDataPart("category_main", map["categoryMain"].toString())
                .addFormDataPart("status", map["status"].toString())
                .addFormDataPart("iid", map["iid"].toString())
                .addFormDataPart("exchange", map["exchange"].toString())
                .addFormDataPart("phone", map["phone"].toString())
                .addFormDataPart("email", map["email"].toString())
                .addFormDataPart("qq", map["qq"].toString())
        val list = builder.build().parts()
        return MallApi.postSaleAsync(list)
    }

    fun postNeedAsync(map: Map<String, Any>, token: String): Deferred<Result> {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("name", map["name"].toString())
                .addFormDataPart("gdesc", map["detail"].toString())
                .addFormDataPart("campus", map["campus"].toString())
                .addFormDataPart("location", map["location"].toString())
                .addFormDataPart("price", map["price"].toString())
                .addFormDataPart("category", map["category"].toString())
                .addFormDataPart("category_main", map["categoryMain"].toString())
                .addFormDataPart("exchange", map["exchange"].toString())
                .addFormDataPart("phone", map["phone"].toString())
                .addFormDataPart("email", map["email"].toString())
                .addFormDataPart("qq", map["qq"].toString())
        val list = builder.build().parts()
        return MallApi.postNeedAsync(list)
    }

    fun deleteSaleAsync(gid: String, token: String) = MallApi.deleteSaleAsync(toReqBody(token), toReqBody(gid))

    //data Utils

    fun setLogin(data: Login) {
        loginLiveData.postValue(data)
    }

    //数据处理
    fun getCampus(i: String?) = when (i) {
        "1" -> "卫津路"
        "2" -> "北洋园"
        else -> "未知"
    }

    fun getBargin(i: String) = when (i) {
        "0" -> "不可刀"
        "1" -> "可小刀"
        "2" -> "可刀"
        else -> "未知"
    }

    fun getStatus(i: String) = when (i) {
        "1" -> "旧"
        "5" -> "5成新"
        "6" -> "6成新"
        "7" -> "7成新"
        "8" -> "8成新"
        "9" -> "9成新"
        "99" -> "99新"
        "10" -> "全新"
        else -> ""
    }

    fun setStatus(i: String) = when (i) {
        "旧" -> 1
        "5成新" -> 5
        "6成新" -> 6
        "7成新" -> 7
        "8成新" -> 8
        "9成新" -> 9
        "99新" -> 99
        "全新" -> 10
        else -> ""
    }

    fun dealText(text: String): String {
        return if (text.length <= 3) {
            text
        } else {
            text.substring(0, 4) + "..."
        }
    }

    //微北洋的token
    private fun getToken(): String {
        return "Bearer{${CommonPreferences.token}}"
    }

    //Cookies(测试用
    fun saveCookie(cookie: String) {
        cookies.add(cookie)
    }

    fun getCookie(): MutableList<String> {
        return cookies
    }

    //处理request body
    private fun toReqBody(int: Int): RequestBody {
        return RequestBody.create(MediaType.parse("multipart/form-data"), int.toString())
    }

    private fun toReqBody(key: String): RequestBody {
        return RequestBody.create(MediaType.parse("multipart/form-data"), key)
    }

    private fun toReqBody(file: File): MultipartBody.Part {
        val body: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        return MultipartBody.Part.createFormData("file", file.name, body)
    }
}