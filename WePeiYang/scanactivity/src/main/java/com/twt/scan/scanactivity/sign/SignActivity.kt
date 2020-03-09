package com.twt.scan.scanactivity.sign

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import com.githang.statusbar.StatusBarCompat
import com.twt.scan.scanactivity.R
import kotlinx.android.synthetic.main.scanactivity_activity_sign.*

class SignActivity : AppCompatActivity() , CanChangeFragment {
    private var tag = ""
    private var toolbarBackgroundTransparent: Drawable? = null
    private var toolbarBackgoundBlue: Drawable? = null
    private var colorBlack: Int = 0
    private var colorBlue: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanactivity_activity_sign)
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.scanActivityToolBarColor), false)
        iv_sign_back.setOnClickListener {
            onBackPressed()
        }
        initBar()
        initFragment()
    }

    private fun initBar() {
        toolbarBackgroundTransparent = ResourcesCompat.getDrawable(resources, R.color.transparent, null)
        toolbarBackgoundBlue = ResourcesCompat.getDrawable(resources, R.color.scanActivityToolBarColor, null)
        colorBlack = ContextCompat.getColor(this, R.color.black_color)
        colorBlue = ContextCompat.getColor(this, R.color.scanActivityToolBarColor)

    }

    private fun initFragment() {
        tag = intent.getStringExtra("fragmentType")
        val fragment: Fragment =
                when (tag) {
                    "Scan" -> {
                        window.statusBarColor = colorBlack
                        tb_sign.background = toolbarBackgroundTransparent
                        FragmentScanSign.newInstance().apply {
                            activityId = intent.getIntExtra("activityId",-1)
                        }
                    }
                    else -> FragmentTypeSign.newInstance().apply {
                        activityId = intent.getIntExtra("activityId",-1)
                    }
                }

        supportFragmentManager.beginTransaction()
                .add(R.id.fl_sign_fragment_container, fragment, tag)
                .commit()
    }

    override fun changeFragment() {
        tag = if (tag == "Scan") {
            "Type"
        } else {
            "Scan"
        }
        val fragment = this.supportFragmentManager.findFragmentByTag(tag) ?: if (tag == "Scan") {
            window.statusBarColor = colorBlack
            tb_sign.background = toolbarBackgroundTransparent
            FragmentScanSign.newInstance().apply {
                activityId = intent.getIntExtra("activityId",-1)
            }
        } else {
            window.statusBarColor = colorBlue
            tb_sign.background = toolbarBackgoundBlue
            FragmentTypeSign.newInstance().apply {
                activityId = intent.getIntExtra("activityId",-1)
            }
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.fl_sign_fragment_container, fragment, tag)
                .commit()
    }
}

interface CanChangeFragment{
    fun changeFragment()
}