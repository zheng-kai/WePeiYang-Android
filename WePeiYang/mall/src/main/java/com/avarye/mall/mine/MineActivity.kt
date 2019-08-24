package com.avarye.mall.mine

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.PopupWindow
import com.avarye.mall.R
import com.avarye.mall.post.PostActivity
import com.avarye.mall.service.MallManager
import com.avarye.mall.service.ViewModel
import com.avarye.mall.service.mineLiveData
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.mall_activity_mine.*
import kotlinx.android.synthetic.main.mall_item_toolbar.*
import kotlinx.android.synthetic.main.mall_pop_setting.*
import kotlinx.android.synthetic.main.mall_pop_setting.view.*
import org.jetbrains.anko.contentView

class MineActivity : AppCompatActivity() {
    private var level = ""
    private var numb = ""
    private var token = ""
    var phone = ""
    var qq = ""
    var email = ""
    var campus = 1
    private val viewModel = ViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_mine)
        window.statusBarColor = ContextCompat.getColor(this, R.color.mallColorMain)

        //toolbar
        tb_main.apply {
            title = "我的"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }

        mineLiveData.refresh(CacheIndicator.LOCAL, CacheIndicator.REMOTE)
        mineLiveData.bindNonNull(this) {
            Glide.with(this@MineActivity)
                    .load(it.avatar)
                    .into(iv_mine_avatar)
            tv_mine_name.text = it.nicheng
            tv_mine_phone.text = dealNull(it.phone)
            tv_mine_qq.text = dealNull(it.qq)
            tv_mine_email.text = dealNull(it.email)
            tv_mine_campus.text = MallManager.getCampus(it.xiaoqu)

            bind(it.id)
            level = it.level
            numb = it.numb
            token = it.token
            phone = it.phone
            qq = it.qq
            email = it.email

            Toasty.info(this@MineActivity, it.id).show()
        }
    }

    private fun dealNull(str: String): String {
        return if (str.isBlank()) {
            "null"
        } else {
            str
        }
    }

    @SuppressLint("InflateParams")
    private fun bind(id: String) {
        cv_mine_sale.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
                    .putExtra("type", "sale")
            startActivity(intent)
        }

        cv_mine_need.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
                    .putExtra("type", "need")
            startActivity(intent)
        }

        cv_mine_fav.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
                    .putExtra("type", "fav")
            startActivity(intent)
        }

        cv_mine_post_sale.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
                    .putExtra("type", 1)
            startActivity(intent)
        }

        cv_mine_post_need.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
                    .putExtra("type", 2)
            startActivity(intent)
        }

        cv_mine_setting.setOnClickListener {
            val popupWindowView: View = LayoutInflater.from(this).inflate(R.layout.mall_pop_setting, null, false)
            val popWindow = PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
            popWindow.apply {
                contentView.bt_setting_cancel.setOnClickListener {
                    dismiss()
                }
                contentView.bt_setting_commit.setOnClickListener {
                    phone = et_setting_phone.text.toString()
                    qq = et_setting_qq.text.toString()
                    email = et_setting_email.text.toString()
                    campus = when (rg_setting_campus.checkedRadioButtonId) {
                        rb_setting_campus1.id -> 1
                        rb_setting_campus2.id -> 2
                        else -> 1
                    }
                    viewModel.changeMyInfo(phone, qq, email, campus)
                    dismiss()
                }
                showAtLocation(this@MineActivity.contentView, Gravity.CENTER, 0, 0)
                isOutsideTouchable = true
                isTouchable = true
                isFocusable = true
                bgAlpha(0.2f)
                setOnDismissListener {
                    bgAlpha(1f)
                }

            }
        }
    }

    private fun bgAlpha(bgAlpha: Float) {
        val lp = window.attributes
        lp.alpha = bgAlpha // 0.0-1.0
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.attributes = lp
    }

}
