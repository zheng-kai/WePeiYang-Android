package com.twt.scan.scanactivity.home

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.githang.statusbar.StatusBarCompat
import com.twt.scan.scanactivity.DataViewModel
import com.twt.scan.scanactivity.R
import com.twt.scan.scanactivity.api.ScanActivityService
import com.twt.scan.scanactivity.api.ScanPreferences
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import kotlinx.android.synthetic.main.scanactivity_activity_home.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
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
        
        val adapter = HomeFragmentPagerAdapter(supportFragmentManager,
                arrayListOf(HomeFragment.newInstance(HomeTitle.NOT_JOINED_TITLE),
                        HomeFragment.newInstance(HomeTitle.JOINED_TITLE)))

        vp_home.apply {
            this.offscreenPageLimit = 2
            this.adapter = adapter
        }

        tl_home.setupWithViewPager(vp_home)
        GlobalScope.launch(IO) {
            if (ScanPreferences.permissionLevel == null || ScanPreferences.twtid == null) {
                val info = ScanActivityService.getUserInfo().await()
                ScanPreferences.twtid = info.data?.user_id
                ScanPreferences.permissionLevel = info.data?.permission
            }
            withContext(Main) {
                if (ScanPreferences.permissionLevel == 2) {
                    adapter.addFragment(HomeFragment.newInstance(HomeTitle.MANAGER_TITLE))

                }
            }

        }

        GlobalScope.launch(Dispatchers.IO + QuietCoroutineExceptionHandler) {
            DataViewModel.apply {
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


}