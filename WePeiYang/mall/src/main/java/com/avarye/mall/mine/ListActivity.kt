package com.avarye.mall.mine

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import com.avarye.mall.R
import com.avarye.mall.detail.DetailActivity
import com.avarye.mall.main.RecItem
import com.avarye.mall.main.recItem
import com.avarye.mall.service.*
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.mall_activity_list.*
import kotlinx.android.synthetic.main.mall_item_toolbar.*

class ListActivity : AppCompatActivity() {

    private var isLoading = false
    private val itemManager = ItemManager()
    private val viewModel = ViewModel()
    private var token = ""
    private var uid = ""
    private var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_list)
        window.statusBarColor = ContextCompat.getColor(this, R.color.mallColorMain)

        mineLiveData.bindNonNull(this) {
            uid = it.id
            token = it.token
        }
        type = intent.getStringExtra("type")
        Toasty.info(this, uid).show()

        tb_main.apply {
            title = when (type) {
                "sale" -> getString(R.string.mallStringMySale)
                "need" -> getString(R.string.mallStringMyNeed)
                "fav" -> getString(R.string.mallStringMyFav)
                else -> "薛定谔的页面"
            }
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }

        srl_list.apply {
            setColorSchemeResources(R.color.mallColorMain)
            //下拉刷新加载监听
            setOnRefreshListener { refresh() }
        }

        rv_list.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = ItemAdapter(itemManager)
        }

        //发请求 bind
        post()
        when (type) {
            "sale" -> bindSale()
            "need" -> bindNeed()
            "fav" -> bindFav()
            else -> Unit
        }
    }

    fun post() = when (type) {
        "sale" -> viewModel.getMyList(uid, 1)
        "need" -> viewModel.getMyList(uid, 2)
        "fav" -> viewModel.getFavList(token)
        else -> Unit
    }

    fun refresh() {
        if (!isLoading) {
            isLoading = true
            //redo
            post()
            srl_list.isRefreshing = false
            Toasty.success(this@ListActivity, "已刷新").show()
        }
        isLoading = false
    }

    private fun bindSale() {
        myListLiveData.bindNonNull(this) { list ->
            val items = mutableListOf<Item>().apply {
                for (i in 1 until list.size) {
                    recItem {
                        Glide.with(this@ListActivity)
                                .load("https://mall.twt.edu.cn/api.php/Upload/img_redirect?id=${list[i].imgurl}")
                                .into(image)
                        name.text = list[i].name
                        price.text = list[i].price
                        locate.text = MallManager.dealText(list[i].location)
                        card.apply {
                            setOnClickListener {
                                val intent = Intent(this@ListActivity, DetailActivity::class.java).putExtra("id", list[i].id)
                                this@ListActivity.startActivity(intent)
                            }
                            setOnLongClickListener {
                                deleteDialog(list[i].id)
                                refresh()
                                true
                            }
                        }
                    }
                }
            }
            itemManager.removeAll { it is RecItem }
            itemManager.addAll(items)
        }
    }

    private fun bindNeed() {
        myListLiveData.bindNonNull(this) { list ->
            val items = mutableListOf<Item>().apply {
                for (i in 1 until list.size) {
                    recItem {
                        name.text = list[i].name
                        price.text = list[i].price
                        locate.text = MallManager.dealText(list[i].location)
                        card.apply {
                            setOnClickListener {
                                val intent = Intent(this@ListActivity, DetailActivity::class.java).putExtra("id", list[i].id)
                                this@ListActivity.startActivity(intent)
                            }
                        }

                    }
                }
            }
            itemManager.removeAll { it is RecItem }
            itemManager.addAll(items)
        }
    }

    private fun bindFav() {
        myFavLiveData.bindNonNull(this) { list ->
            val items = mutableListOf<Item>().apply {
                for (i in 1 until list.size) {
                    recItem {
                        Glide.with(this@ListActivity)
                                .load("https://mall.twt.edu.cn/api.php/Upload/img_redirect?id=${list[i].imgurl}")
                                .into(image)
                        name.text = list[i].name
                        price.text = list[i].price
                        locate.text = MallManager.dealText(list[i].location)
                        card.apply {
                            setOnClickListener {
                                val intent = Intent(this@ListActivity, DetailActivity::class.java).putExtra("id", list[i].id)
                                this@ListActivity.startActivity(intent)
                            }
                            setOnLongClickListener {
                                deleteDialog(list[i].id)
                                refresh()
                                true
                            }
                        }
                    }
                }
            }
            itemManager.removeAll { it is RecItem }
            itemManager.addAll(items)
        }
    }

    private fun deleteDialog(gid: String) = AlertDialog.Builder(this)
            .setTitle(when (type) {
                "sale" -> "商品下架"
                "fav" -> "取消收藏"
                else -> "薛定谔又来了"
            })
            .setCancelable(false)
            .setPositiveButton("取消") { dialog, _ -> dialog.dismiss() }
            .setNegativeButton("确定") { dialog, _ ->
                run {
                    when (type) {
                        "sale" -> {
                            viewModel.deleteSale(gid, token)
                            viewModel.getMyList(gid, 1)
                        }
                        "fav" -> {
                            viewModel.deFav(gid, token)
                            viewModel.getFavList(token)
                        }
                        else -> Unit
                    }
                    dialog.dismiss()
                }
            }
            .create()
            .show()

}