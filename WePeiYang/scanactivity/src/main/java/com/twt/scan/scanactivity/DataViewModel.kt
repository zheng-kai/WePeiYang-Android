package com.twt.scan.scanactivity

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.twt.scan.scanactivity.api.Details
import com.twt.scan.scanactivity.api.ScanActivityService
import com.twt.scan.scanactivity.home.HomeTitle
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object DataViewModel : ViewModel() {
    const val DOING_ACTIVITY = 0
    const val FINISHED_ACTIVITY = 1
    const val MANAGER_ACTIVITY = 2

    private val managerBeanLiveData = MutableLiveData<List<Details>>()
    private val homeBeanJoinedLiveData = MutableLiveData<List<Details>>()
    private val homeBeanNotJoinLiveData = MutableLiveData<List<Details>>()
    fun getLiveData(type: HomeTitle): MutableLiveData<List<Details>> {
        return when (type) {
            HomeTitle.JOINED_TITLE -> homeBeanJoinedLiveData
            HomeTitle.NOT_JOINED_TITLE -> homeBeanNotJoinLiveData
            HomeTitle.MANAGER_TITLE -> managerBeanLiveData
        }

    }

    suspend fun getBean(type: HomeTitle) {
        when (type) {
            HomeTitle.JOINED_TITLE -> getDataBean(FINISHED_ACTIVITY)
            HomeTitle.NOT_JOINED_TITLE -> getDataBean(DOING_ACTIVITY)
            HomeTitle.MANAGER_TITLE -> getDataBean(MANAGER_ACTIVITY)
        }
    }

    suspend fun getAllBean() {
        getDataBean(FINISHED_ACTIVITY)
        getDataBean(DOING_ACTIVITY)
        getDataBean(MANAGER_ACTIVITY)
    }

    private suspend fun getDataBean(type: Int) {

        val result = ScanActivityService.getActivitiesAsync(1,  type).await()
        if (result.error_code == 0) {
            when (type) {
                FINISHED_ACTIVITY -> homeBeanJoinedLiveData.value = homeBeanJoinedLiveData.value
                        ?.plus(result.data?.details?.toTypedArray() ?: arrayOf())
                MANAGER_ACTIVITY -> managerBeanLiveData.value = managerBeanLiveData.value
                        ?.plus(result.data?.details?.toTypedArray() ?: arrayOf())
                DOING_ACTIVITY -> homeBeanNotJoinLiveData.value = homeBeanNotJoinLiveData.value
                        ?.plus(result.data?.details?.toTypedArray() ?: arrayOf())
            }
            Toasty.normal(CommonContext.application, result.message)
        } else {
            Toasty.error(CommonContext.application, result.message)
        }
        // 假数据
        managerBeanLiveData.value = listOf(Details(1, "content!", "1588882342", "1588892342", null, 0, 0, listOf(), 0, listOf(), "position", "title", "teacher"))
        homeBeanJoinedLiveData.value = listOf(Details(1, "content!", "1588882342", "1588892342", null, 0, 0, listOf(), 0, listOf(), "position", "title", "teacher"))

    }
}