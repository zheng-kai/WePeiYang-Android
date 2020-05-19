package com.twt.scan.scanactivity

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.twt.scan.scanactivity.api.Details
import com.twt.scan.scanactivity.api.ScanActivityService
import com.twt.scan.scanactivity.home.HomeTitle
import com.twt.wepeiyang.commons.experimental.CommonContext
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

object DataViewModel : ViewModel() {
    const val DOING_ACTIVITY = 0
    const val FINISHED_ACTIVITY = 1
    const val MANAGER_ACTIVITY = 2
    private val pageMap = HashMap<Int, Int>()
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

    suspend fun refreshBean(type: HomeTitle): Boolean {
        return when (type) {
            HomeTitle.JOINED_TITLE -> {
                val result = refreshDataBean(FINISHED_ACTIVITY)
                if (result) {
                    pageMap[FINISHED_ACTIVITY] = 1
                }
                result
            }
            HomeTitle.NOT_JOINED_TITLE -> {
                val result = refreshDataBean(DOING_ACTIVITY)
                if (result) {
                    pageMap[DOING_ACTIVITY] = 1
                }
                result
            }
            HomeTitle.MANAGER_TITLE -> {
                val result = refreshDataBean(MANAGER_ACTIVITY)
                if (result) {
                    pageMap[MANAGER_ACTIVITY] = 1
                }
                result
            }
        }
    }

    private suspend fun refreshDataBean(type: Int): Boolean {
        val result = withContext(IO) {
            ScanActivityService.getActivitiesAsync(1, type).await()
        }
        Log.d("result", result.message)

        if (result.error_code == 0) {
            withContext(Main) {
                when (type) {
                    FINISHED_ACTIVITY -> {
                        homeBeanJoinedLiveData.value = ArrayList<Details>().apply {
                            addAll(result.data?.data?.toTypedArray() ?: arrayOf())
                        }
                    }
                    MANAGER_ACTIVITY -> {
                        managerBeanLiveData.value = ArrayList<Details>().apply {
                            addAll(result.data?.data?.toTypedArray() ?: arrayOf())
                        }
                    }
                    DOING_ACTIVITY -> {
                        homeBeanNotJoinLiveData.value = ArrayList<Details>().apply {
                            addAll(result.data?.data?.toTypedArray() ?: arrayOf())
                        }
                    }
                }
            }
            return true
        }
        return false

    }

    suspend fun getBeanMore(type: HomeTitle) {
        when (type) {
            HomeTitle.JOINED_TITLE -> {
                if (getDataBeanMore(FINISHED_ACTIVITY)) {
                    pageMap[FINISHED_ACTIVITY] = (pageMap[FINISHED_ACTIVITY] ?: 0) + 1
                }
            }
            HomeTitle.NOT_JOINED_TITLE -> {
                if (getDataBeanMore(DOING_ACTIVITY)) {
                    pageMap[DOING_ACTIVITY] = (pageMap[DOING_ACTIVITY] ?: 0) + 1
                }
            }
            HomeTitle.MANAGER_TITLE -> {
                if (getDataBeanMore(MANAGER_ACTIVITY)) {
                    pageMap[MANAGER_ACTIVITY] = (pageMap[MANAGER_ACTIVITY] ?: 0) + 1
                }
            }
        }
    }

    suspend fun getAllBeanInit() {

        refreshBean(HomeTitle.NOT_JOINED_TITLE)
        refreshBean(HomeTitle.MANAGER_TITLE)
        refreshBean(HomeTitle.JOINED_TITLE)

    }

    private suspend fun getDataBeanMore(type: Int): Boolean {
        pageMap[type] = pageMap[type] ?: 0 + 1
        val page = pageMap[type] as Int
        val result = withContext(IO) {
            ScanActivityService.getActivitiesAsync(page, type).await()
        }

        if (result.error_code == 0) {
            withContext(Main) {
                when (type) {
                    FINISHED_ACTIVITY -> {
                        homeBeanJoinedLiveData.value = homeBeanJoinedLiveData.value
                                ?.plus(result.data?.data?.toTypedArray() ?: arrayOf())
                                ?: ArrayList<Details>().apply {
                                    addAll(result.data?.data?.toTypedArray() ?: arrayOf())
                                }
                    }
                    MANAGER_ACTIVITY -> {
                        managerBeanLiveData.value = managerBeanLiveData.value
                                ?.plus(result.data?.data?.toTypedArray() ?: arrayOf())
                                ?: ArrayList<Details>().apply {
                                    addAll(result.data?.data?.toTypedArray() ?: arrayOf())
                                }
                    }
                    DOING_ACTIVITY -> {
                        homeBeanNotJoinLiveData.value = homeBeanNotJoinLiveData.value
                                ?.plus(result.data?.data?.toTypedArray() ?: arrayOf())
                                ?: ArrayList<Details>().apply {
                                    addAll(result.data?.data?.toTypedArray() ?: arrayOf())
                                }
                    }
                }
            }
            Toasty.normal(CommonContext.application, result.message).show()
            return true
        } else {
            Toasty.error(CommonContext.application, result.message).show()
            return false
        }



    }
}