package com.twt.scan.scanactivity.home

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.githang.statusbar.StatusBarCompat
import com.twt.scan.scanactivity.DataViewModel
import com.twt.scan.scanactivity.R
import com.twt.scan.scanactivity.add
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twt.scan.scanactivity.api.Details
import com.twt.scan.scanactivity.api.ScanActivityService
import com.twt.scan.scanactivity.api.ScanPreferences
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import kotlinx.android.synthetic.main.scanactivity_activity_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanactivity_activity_home)
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.scanActivityToolBarColor), false)
        iv_home_back.setOnClickListener {
            onBackPressed()
        }

        vp_home.apply {
            this.offscreenPageLimit = 2
            adapter = HomeFragmentPagerAdapter(supportFragmentManager,
                    listOf(HomeFragment.newInstance(HomeTitle.MANAGER_TITLE),
                            HomeFragment.newInstance(HomeTitle.NOT_JOINED_TITLE),
                            HomeFragment.newInstance(HomeTitle.JOINED_TITLE)))
        }
        tl_home.setupWithViewPager(vp_home)
        GlobalScope.launch(Dispatchers.IO + QuietCoroutineExceptionHandler) {
            val info = ScanActivityService.getUserInfo().await()
            ScanPreferences.twtid = info.data?.user_id
            ScanPreferences.permissionLevel = info.data?.permission
        }
        GlobalScope.launch(Dispatchers.IO + QuietCoroutineExceptionHandler) {
            DataViewModel.apply {
//                val result = ScanActivityService.login().await()
//                Log.d("result",result.message)
                getAllBeanInit()
            }
        }
    }

    private fun formatDate(start: String, end: String): String {
        val simple = SimpleDateFormat("yyyy年MM月dd日 HH:mm")
        val calendar = Calendar.getInstance().apply {
            timeInMillis = start.toLong() * 1000
        }
        val simple2 = SimpleDateFormat("\nyyyy年MM月dd日-HH:mm")
        val calendar2 = Calendar.getInstance().apply {
            timeInMillis = end.toLong() * 1000
        }
        return simple.format(calendar.timeInMillis) + simple2.format(calendar2.timeInMillis)
    }

    private fun RecyclerView.addData(data: List<Details>?, isManagerBtn: Boolean) {
        withItems {
            if (isManagerBtn) {
                data?.forEach {
                    add(it.title, it.position, formatDate(it.start, it.end), it.teacher, it.activity_id)
                }
            } else {
                data?.forEach {
                    add(it.title, it.position, formatDate(it.start, it.end), it.teacher)
                }
            }

        }
    }

}