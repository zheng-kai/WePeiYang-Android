package com.twt.scan.scanactivity

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.twt.scan.scanactivity.api.ActivityBean
import com.twt.scan.scanactivity.home.HomeTitle

object DataViewModel : ViewModel() {
    private val managerBeanLiveData = MutableLiveData<List<ActivityBean>>()
    private val homeBeanJoinLiveData = MutableLiveData<List<ActivityBean>>()
    private val homeBeanNotJoinLiveData = MutableLiveData<List<ActivityBean>>()
    fun getLiveData(type: HomeTitle): MutableLiveData<List<ActivityBean>> {
        return when (type) {
            HomeTitle.JOINED_TITLE -> homeBeanJoinLiveData
            HomeTitle.NOT_JOINED_TITLE -> homeBeanNotJoinLiveData
            HomeTitle.MANAGER_TITLE -> managerBeanLiveData
        }

    }
    fun getBean(type:HomeTitle){
        when (type) {
            HomeTitle.JOINED_TITLE -> getJoinBean()
            HomeTitle.NOT_JOINED_TITLE -> getNotJoinBean()
            HomeTitle.MANAGER_TITLE -> getManagerBean()
        }
    }
    fun getManagerBean() {
//        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
//            val result = ScanActivityApi.getActivities(1, 20, CommonPreferences.token, MANAGER_ACTIVITY).await()
//            if (result.error_code == 0) {
//                managerBeanLiveData.value = result.data
//                Toasty.normal(CommonContext.application, result.message)
//            } else {
//                Toasty.error(CommonContext.application, result.message)
//            }
//            Log.d("GetBeanManager",result.toString())
//        }
        val bean = ActivityBean(1, "test,管理", 1, "55555555", 2, listOf("aaa", "bbb"), 1, 2, "55", "44444444", "海绵宝宝", "派大星我们去抓水母吧！")
        managerBeanLiveData.value = arrayListOf(bean, bean, bean, bean).apply {
            this.addAll(managerBeanLiveData.value?.toTypedArray() ?: Array(0) {
                bean
            })
        }

    }

    fun getJoinBean() {
//        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
//            val result = ScanActivityApi.getActivities(1, 20, CommonPreferences.token, FINISHED_ACTIVITY).await()
//            if (result.error_code == 0) {
//                homeBeanJoinLiveData.value = result.data
//                Toasty.normal(CommonContext.application, result.message)
//            } else {
//                Toasty.error(CommonContext.application, result.message)
//            }
//            Log.d("GetBeanJoined",result.toString())
//
//        }
        val bean = ActivityBean(1, "test,已参加", 1, "55555555", 2, listOf("aaa", "bbb"), 1, 2, "55", "44444444", "派大星", "快乐是怎么消失的呢，海绵宝宝？")
        homeBeanJoinLiveData.value = arrayListOf(bean, bean, bean).apply {
            this.addAll(homeBeanJoinLiveData.value?.toTypedArray() ?: Array(0) {
                bean
            })
        }
    }

    fun getNotJoinBean() {
//        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
//            val result = ScanActivityApi.getActivities(1, 20, CommonPreferences.token, DOING_ACTIVITY).await()
//            if (result.error_code == 0) {
//                homeBeanNotJoinLiveData.value = result.data
//                Toasty.normal(CommonContext.application, result.message)
//            } else {
//                Toasty.error(CommonContext.application, result.message)
//            }
//            Log.d("GetBeanNotJoined",result.toString())
//
//        }
        val bean = ActivityBean(1, "test,待参加", 1, "55555555", 2, listOf("aaa", "bbb"), 1, 2, "55", "44444444", "海绵宝宝", "我准备好了 我准备好了 我准备好了")
        homeBeanNotJoinLiveData.value = arrayListOf(bean, bean).apply {
            this.addAll(homeBeanNotJoinLiveData.value?.toTypedArray() ?: Array(0) {
                bean
            })
        }
    }
}