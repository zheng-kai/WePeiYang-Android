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
    private val currentPageMap = HashMap<Int, Int>()
    private val maxPageMap = HashMap<Int, Int>()
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

    fun isEnd(type: HomeTitle): Boolean {
        return when (type) {
            HomeTitle.JOINED_TITLE -> {
                maxPageMap[FINISHED_ACTIVITY] ?: 1 <= currentPageMap[FINISHED_ACTIVITY] ?: 1
            }
            HomeTitle.NOT_JOINED_TITLE -> {
                maxPageMap[DOING_ACTIVITY] ?: 1 <= currentPageMap[DOING_ACTIVITY] ?: 1

            }
            HomeTitle.MANAGER_TITLE -> {
                maxPageMap[MANAGER_ACTIVITY] ?: 1 <= currentPageMap[MANAGER_ACTIVITY] ?: 1
            }
        }
    }

    suspend fun refreshBean(type: HomeTitle): Boolean {
        return when (type) {
            HomeTitle.JOINED_TITLE -> {
                refreshDataBean(FINISHED_ACTIVITY)
            }
            HomeTitle.NOT_JOINED_TITLE -> {
                refreshDataBean(DOING_ACTIVITY)
            }
            HomeTitle.MANAGER_TITLE -> {
                refreshDataBean(MANAGER_ACTIVITY)
            }
        }
    }

    private suspend fun refreshDataBean(type: Int): Boolean {
        val result = withContext(IO) {
            ScanActivityService.getActivitiesAsync(1, type).await()
        }
        Log.d("result", result.message)

        if (result.error_code == 0) {
            maxPageMap[type] = result.data?.lastPage ?: 1
            currentPageMap[type] = result.data?.currentPage ?: 1 // 在livedata改变之前 改变页号
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
                getDataBeanMore(FINISHED_ACTIVITY)

            }
            HomeTitle.NOT_JOINED_TITLE -> {
                getDataBeanMore(DOING_ACTIVITY)

            }
            HomeTitle.MANAGER_TITLE -> {
                getDataBeanMore(MANAGER_ACTIVITY)

            }
        }
    }

    suspend fun getAllBeanInit() {

        refreshBean(HomeTitle.NOT_JOINED_TITLE)
        refreshBean(HomeTitle.MANAGER_TITLE)
        refreshBean(HomeTitle.JOINED_TITLE)

    }

    private suspend fun getDataBeanMore(type: Int): Boolean {
        val page = currentPageMap[type] as Int + 1
        val result = withContext(IO) {
            ScanActivityService.getActivitiesAsync(page, type).await()
        }

        if (result.error_code == 0) {
            maxPageMap[type] = result.data?.lastPage ?: 1
            currentPageMap[type] = result.data?.currentPage ?: 1
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