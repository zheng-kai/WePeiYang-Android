package com.twt.scan.scanactivity.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.twt.scan.scanactivity.*
import com.twt.scan.scanactivity.home.HomeTitle.MANAGER_TITLE
import com.twt.scan.scanactivity.home.HomeTitle.NOT_JOINED_TITLE
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.refreshAll
import com.twt.wepeiyang.commons.ui.rec.withItems
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.scanactivity_fragment_home.view.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    var title: HomeTitle = NOT_JOINED_TITLE

    companion object {

        fun newInstance(title: HomeTitle): HomeFragment = HomeFragment().apply {
            this.title = title
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.scanactivity_fragment_home, container, false)
        view.rv_home_fragment.layoutManager = LinearLayoutManager(CommonContext.application)
        view.rv_home_fragment.addOnScrollListener(object : EndlessScrollListener() {
            override fun loadMore() {
                GlobalScope.launch(Main) {
                    if (!DataViewModel.isEnd(title)) {
                        DataViewModel.getBeanMore(title)
                    }
                }
            }
        })
        view.srl_home_fragment.setOnRefreshListener {
            GlobalScope.launch(Main) {
                if (DataViewModel.refreshBean(title)) {
                    context?.let {
                        Toasty.normal(it, "刷新成功", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    context?.let {
                        Toasty.error(it, "刷新失败", Toast.LENGTH_SHORT).show()
                    }
                }
                view.srl_home_fragment.isRefreshing = false
            }
        }
        DataViewModel.getLiveData(title).bindNonNull(this.activity!!) {
            Log.d("result", it.toString())
            it?.let {
                if (it.isNotEmpty()) {
                    view.tv_home_fragment_loading.text = "正在加载"
                    view.rv_home_fragment.visibility = View.VISIBLE
                    view.tv_home_fragment_loading.visibility = View.INVISIBLE
                    when (title) {
                        MANAGER_TITLE -> {
                            view.rv_home_fragment.refreshAll {

                                it.forEach { it ->
                                    addManagerActivity(it.title, it.position, formatDate(it.start, it.end), it.teacher, it.activity_id)
                                }
                                if (DataViewModel.isEnd(title)) {
                                    addFooter("没有更多了")
                                } else {
                                    addFooter("正在加载")
                                }
                            }
                        }
                        else -> {
                            view.rv_home_fragment.refreshAll {

                                it.forEach { it ->
                                    addNormalActivity(it.activity_id, it.title, it.position, formatDate(it.start, it.end), it.teacher)
                                }
                                if (DataViewModel.isEnd(title)) {
                                    addFooter("没有更多了")
                                } else {
                                    addFooter("正在加载")
                                }
                            }
                        }
                    }
                } else {
                    view.tv_home_fragment_loading.text = "无活动"
                    view.rv_home_fragment.visibility = View.INVISIBLE
                    view.tv_home_fragment_loading.visibility = View.VISIBLE
                }

            }
        }
        view.tv_home_fragment_loading.setOnClickListener {
            it as TextView
            if (it.text == "无活动") {
                GlobalScope.launch(IO) {
                    DataViewModel.refreshBean(title)

                }
            }
        }
        return view
    }
}