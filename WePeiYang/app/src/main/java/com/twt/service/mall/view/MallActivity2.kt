package com.twt.service.mall.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.widget.EditText
import android.widget.ImageView
import com.twt.service.R
import com.twt.service.mall.Presenter
import com.twt.service.mall.model.Goods
import com.twt.service.mall.model.Login
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.withItems

import kotlinx.android.synthetic.main.mall_activity_main.*
import kotlinx.android.synthetic.main.mall_content_main.*
import kotlinx.android.synthetic.main.mall_item_header.*

class MallActivity2 : AppCompatActivity() {
    private val headerKey = "Authorization"
    private val headerToken = "Bearer{${CommonPreferences.token}}"

    private lateinit var editText: EditText
    lateinit var searchImg: ImageView
    lateinit var viewPager: ViewPager
    lateinit var recyclerView: RecyclerView
    lateinit var fabMine: FloatingActionButton
    lateinit var menu: ImageView

    private var presenter = Presenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_main)
        setSupportActionBar(tb_mall_main)
        init()

        login()

        searchImg.setOnClickListener { search() }

        menu.setOnClickListener { getMenu() }

        fabMine.setOnClickListener { }
    }

    private fun init() {
        editText = et_mall_search
        searchImg = iv_mall_search
        viewPager = mall_vp_header
        recyclerView = mall_rv_main
        fabMine = mall_fab_mine
        //menuButton
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adaptRec(this)
    }

    private fun adaptRec(context: Context) {
        recyclerView.withItems {
            for (it in presenter.infoGoods!!) {
                addItem(this@MallActivity2, it)
            }
        }
    }

    private fun MutableList<Item>.addItem(context: Context, info: Goods) = add(RecItem(context, info))

    private fun refresh() {

    }

    private fun search() {
        val key = editText.text.toString()
        presenter.search(key)
    }

    private fun getMenu() {
        presenter.getMenu()
    }

    private fun login() {
        val map = HashMap<String, String>()
        map[headerKey] = headerToken
        presenter.login(headerToken)
    }


    /*
其他界面的一些方法

private fun sale() {}
private fun need() {}
private fun signOut() {}
*/

}