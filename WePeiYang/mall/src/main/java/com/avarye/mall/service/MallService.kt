package com.avarye.mall.service

import android.arch.lifecycle.MutableLiveData
import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.cache.Cache
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * 商城接口、LiveData、数据类
 */
interface MallApi {

    @GET("/api.php/Login/wpyLogin?model=1")
    fun loginAsync(@Header("Authorization") token: String): Deferred<CommonBody<Login>>

    @GET("api.php/User/myself_info")
    fun getMyInfoAsync(): Deferred<MyInfo>

    @Multipart
    @POST("api.php/User/myself_change")
    fun changeMyInfoAsync(@Part("phone") phone: RequestBody? = null,
                          @Part("email") email: RequestBody? = null,
                          @Part("qq") qq: RequestBody? = null,
                          @Part("xiaoqu") campus: RequestBody? = null): Deferred<Result>

    @Multipart
    @POST("api.php/User/campus_change")
    fun changeCampusAsync(@Part("xiaoqu") campus: RequestBody): Deferred<Result>

    @Multipart
    @POST("api.php/Items/item_new")
    fun latestSaleAsync(@Part("yeshu") page: RequestBody,
                        @Part("which") which: RequestBody): Deferred<List<Sale>>

    @Multipart
    @POST("api.php/Items/item_new")
    fun latestNeedAsync(@Part("yeshu") page: RequestBody,
                        @Part("which") which: RequestBody): Deferred<List<Sale>>

    @Multipart
    @POST("api.php/Items/search")
    fun searchAsync(@Part("key") key: RequestBody,
                    @Part("yeshu") page: RequestBody): Deferred<List<Sale>>

    @GET("api.php/Items/menu")
    fun getMenuAsync(): Deferred<List<Menu>>

    @Multipart
    @POST("api.php/Items/items_fenlei")
    fun selectAsync(@Part("category") category: RequestBody,
                    @Part("yeshu") page: RequestBody,
                    @Part("which") which: RequestBody): Deferred<List<Sale>>

    @GET("api.php/Items/item_one")
    fun getDetailAsync(@Query("id") id: String): Deferred<Sale>

    @Multipart
    @POST("api.php/Items/saler_info")
    fun getSellerSaleAsync(@Part("token") token: RequestBody,
                           @Part("gid") gid: RequestBody): Deferred<Seller>

    @Multipart
    @POST("api.php/Items/saler_info")
    fun getSellerNeedAsync(@Part("token") token: RequestBody,
                           @Part("nid") gid: RequestBody): Deferred<Seller>

    @GET("api.php/User/userinfo_for_app")
    fun getUserInfoAsync(@Query("id") id: String): Deferred<UserInfo>

    @Multipart
    @POST("api.php/Upload/img_upload")
    fun postImgAsync(@Part partList: List<MultipartBody.Part>): Deferred<Result>

    @Multipart
    @POST("api.php/Items/shoucang")
    fun favAsync(@Part("token") token: RequestBody,
                 @Part("gid") gid: RequestBody): Deferred<Result>

    @Multipart
    @POST("api.php/Items/shoucang_quxiao")
    fun deFavAsync(@Part("token") token: RequestBody,
                   @Part("gid") gid: RequestBody): Deferred<Result>

    @Multipart
    @POST("api.php/Items/shoucang_list")
    fun getFavListAsync(@Part("token") token: RequestBody): Deferred<List<Sale>>

    @Multipart
    @POST("api.php/Items/comment_list")
    fun getCommentListAsync(@Part("token") token: RequestBody,
                            @Part("which") which: RequestBody,
                            @Part("id") id: RequestBody): Deferred<List<CommentList>>

    @Multipart
    @POST("api.php/Items/comment_do")
    fun commentAsync(@Part("content") content: RequestBody,
                     @Part("token") token: RequestBody,
                     @Part("which") which: RequestBody,
                     @Part("tid") tid: RequestBody): Deferred<Result>

    @Multipart
    @POST("api.php/Items/huifu_list")
    fun getReplyListAsync(@Part("token") token: RequestBody,
                          @Part("cid") cid: RequestBody): Deferred<List<CommentList>>

    @Multipart
    @POST("api.php/Items/hui_do")
    fun replyAsync(@Part("token") token: RequestBody,
                   @Part("cid") cid: RequestBody,
                   @Part("content") content: RequestBody): Deferred<Result>

    @Multipart
    @POST("api.php/Items/sale_fabu")
    fun postSaleAsync(@Part partList: List<MultipartBody.Part>): Deferred<Result>

    @Multipart
    @POST("api.php/Items/item_off")
    fun deleteSaleAsync(@Part("token") token: RequestBody,
                        @Part("gid") gid: RequestBody): Deferred<Result>

    @Multipart
    @POST("api.php/Items/need_off")
    fun deleteNeedAsync(@Part("token") token: RequestBody,
                        @Part("nid") nid: RequestBody): Deferred<Result>

    @Multipart
    @POST("api.php/Items/need_fabu")
    fun postNeedAsync(@Part partList: List<MultipartBody.Part>): Deferred<Result>

    companion object : MallApi by MallApiService()
}

val loginLiveData = MutableLiveData<Login>()//登陆相关信息 主要是token
val loadingLiveData = MutableLiveData<Boolean>()//loading flag
val saleLiveData = MutableLiveData<List<Sale>>()//主页面sale数据
val needLiveData = MutableLiveData<List<Sale>>()//主页面need数据
val selectLiveData = MutableLiveData<List<Sale>>()//搜索 分类获得的数据
val myListLiveData = MutableLiveData<List<Sale>>()//我的发布 求购 搜藏
val commentLiveData = MutableLiveData<List<CommentList>>()//评论的数据  TODO:还没看orz
val detailLiveData = MutableLiveData<Sale>()//具体信息 其实只需要里面的imageUrl
val sellerLiveData = MutableLiveData<Seller>()//卖家信息

private val menuLocalData = Cache.hawk<List<Menu>>("MALL_MENU")//分类菜单
private val menuRemoteData = Cache.from(MallApi.Companion::getMenuAsync)
val menuLiveData = RefreshableLiveData.use(menuLocalData, menuRemoteData)

private val mineLocalData = Cache.hawk<MyInfo>("MALL_MINE")//我的信息
private val mineRemoteData = Cache.from(MallApi.Companion::getMyInfoAsync)
val mineLiveData = RefreshableLiveData.use(mineLocalData, mineRemoteData)


object MallApiService {

    private val loggingInterceptor = HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.BODY }

    private val cookie = object : CookieJar {
        val map = HashMap<String, Cookie>()
        val list = mutableListOf<Cookie>()

        override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
            cookies.forEach {
                when (it.name()) {
                    "twt_id" -> map["twt_id"] = it
                    "uid" -> map["uid"] = it
                    "token" -> map["token"] = it
                    "PHPSESSID" -> map["PHPSESSID"] = it
                }
            }
            list.clear()
            for (entry in map) {
                list.add(entry.value)
            }
        }

        override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
            return list
        }
    }

    private val clientBuilder = OkHttpClient.Builder()
            .cookieJar(cookie)
    private val client: OkHttpClient = clientBuilder
            .addNetworkInterceptor(loggingInterceptor)
            .build()
    private const val baseUrl = "https://mall.twt.edu.cn"
    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    inline operator fun <reified T> invoke(): T = retrofit.create(T::class.java)
}

data class Result(
        val msg: String,
        val result_code: String,
        val img_url: String?,
        val id: String?,
        val cid: String?,
        val hid: String?
)

data class Login(
        val token: String,
        val twt_id: Int,
        val uid: String
)

data class MyInfo(
        val avatar: String,
        val email: String,
        val icon: String,
        val id: String,
        val level: String,
        val nicheng: String,
        val numb: String,
        val phone: String,
        val qq: String,
        val token: String,
        val xiaoqu: String,
        val goods_count: Int,
        val needs_count: Int
)

data class Sale(
        val bargain: Any,
        val campus: String,
        val ctime: String,
        val gdesc: String,
        val id: String,
        val imgurl: String,
        val email: String,
        val icon: String,
        val label_name: Any,
        val location: String,
        val name: String,
        val page: Int,
        val phone: String,
        val price: String,
        val qq: String,
        val uid: String,
        val username: String,
        val exchange: String,
        val state: String,
        val is_collected: Boolean
)

data class Menu(
        val icon: String,
        val id: String,
        val name: String,
        val smalllist: List<SmallList>
)

data class SmallList(
        val b_id: String,
        val id: String,
        val name: String
)

data class Seller(
        val email: String,
        val phone: String,
        val qq: String
)

data class UserInfo(
        val needs_list: List<Sale>?,
        val goods_list: List<Sale>?,
        val user_info: MyInfo
)

data class CommentList(
        val cid: String,
        val content: String,
        val ctime: String,
        val icon: String,
        val name: String,
        val uid: String
)


