package com.twt.service.qrcode

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.google.gson.Gson
import com.twt.service.R
import com.twt.service.R.id.*
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
    private val CARDVIEW_MARGIN = 22f
    private val CONSTRAINTLAYOUT_MARGIN = 13f
    private val QRCODE_MARGIN = 26
    private lateinit var job :Job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_qrcode)
        window.statusBarColor = getColorCompat(R.color.white)
        enableLightStatusBarMode(true)
        ivQRCode = findViewById(iv_qrcode_display)



        job = GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                //TODO
//                generateQRCode()
//                delay(60000) // 设置时间，1分钟刷新一次
            }

        }
        ivQRCode.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
//                generateQRCode()
//TODO
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
        // 此处为了计算二维码的宽度，单位px
        val outSize = Point()

        windowManager.defaultDisplay.getRealSize(outSize)
        val x = outSize.x // x为屏幕的px值
        val marginStartAndEnd = 2 * dip2px(this, CARDVIEW_MARGIN + CONSTRAINTLAYOUT_MARGIN + QRCODE_MARGIN) // xml布局中 二维码图片外两层布局margin分别是CARDVIEW_MARGIN和CONSTRAINTLAYOUT_MARGIN.(还没找到动态获取的方法）然后再加上图片的margin QRCODE_MARGIN

        bean = QRInfo(CommonPreferences.realName, CommonPreferences.studentid, System.currentTimeMillis())
        bitmap = QRCodeEncoder.syncEncodeQRCode(gson.toJson(bean), x - marginStartAndEnd)
        bitmap?.let {
            ivQRCode.setImageBitmap(it)
            Toasty.normal(this@MyQRCode, "二维码生成成功", Toast.LENGTH_SHORT).show()
        } ?: Toasty.error(this@MyQRCode, "二维码生成失败").show()

    }

    private fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }


}