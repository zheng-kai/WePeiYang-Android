package com.twt.service.qrcode

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.Constraints
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.google.gson.Gson
import com.twt.service.R
import com.twt.service.R.id.*
import com.twt.wepeiyang.commons.data.QRInfo
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.*


class MyQRCode : AppCompatActivity() {
    private var bitmap: Bitmap? = null
    private var bean: QRInfo? = null
    private val gson = Gson()
    private lateinit var ivQRCode: ImageView
    private var width = 0
    private lateinit var job: Job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_qrcode)
        window.statusBarColor = getColorCompat(R.color.white)
        enableLightStatusBarMode(true)
        ivQRCode = findViewById(iv_qrcode_display)

        ivQRCode.post {
            // 获取 imgView 的宽度
            width = ivQRCode.measuredWidth

            job = GlobalScope.launch(Dispatchers.Main) {
                while (true) {
                    generateQRCode()
                    delay(60000) // 设置时间，1分钟刷新一次
                }
            }
        }

        ivQRCode.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                generateQRCode()
            }
        }
        findViewById<TextView>(tv_my_qrcode_name).text = CommonPreferences.realName
        findViewById<TextView>(tv_my_qrcode_id).text = CommonPreferences.studentid
        findViewById<ImageView>(iv_my_qrcode_back).setOnClickListener {
            onBackPressed()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun generateQRCode() {
        bean = QRInfo(CommonPreferences.realName, CommonPreferences.studentid, System.currentTimeMillis())
        bitmap = QRCodeEncoder.syncEncodeQRCode(gson.toJson(bean), width)
        bitmap?.let {
            ivQRCode.setImageBitmap(it)
            Toasty.normal(this@MyQRCode, "二维码生成成功", Toast.LENGTH_SHORT).show()
        } ?: Toasty.error(this@MyQRCode, "二维码生成失败").show()

    }

}