package com.twt.scan.scanactivity.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twt.scan.scanactivity.DataViewModel
import com.twt.scan.scanactivity.R
import com.twt.scan.scanactivity.add
import com.twt.scan.scanactivity.api.ScanActivityService
import com.twt.scan.scanactivity.formatDate
import com.twt.scan.scanactivity.home.HomeTitle.MANAGER_TITLE
import com.twt.scan.scanactivity.home.HomeTitle.NOT_JOINED_TITLE
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.ui.rec.withItems
import kotlinx.android.synthetic.main.scanactivity_fragment_home.view.*
import kotlinx.coroutines.Dispatchers
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
        view.srl_home_fragment.setOnRefreshListener {
            GlobalScope.launch(Dispatchers.Main) {
                DataViewModel.getBean(title)
                view.srl_home_fragment.isRefreshing = false
            }
        }
        DataViewModel.getLiveData(title).bindNonNull(this.activity!!) {
            it?.let {
                if (it.isNotEmpty()) {
                    view.rv_home_fragment.visibility = View.VISIBLE
                    view.tv_home_fragment_loading.visibility = View.INVISIBLE
                    when (title) {
                        MANAGER_TITLE -> {
                            view.rv_home_fragment.withItems {
                                it.forEach { it ->
                                    add(it.title, it.position, formatDate(it.start, it.end), it.teacher, it.activity_id)
                                }
                            }
                        }
                        else -> {
                            view.rv_home_fragment.withItems {
                                it.forEach { it ->
                                    add(it.title, it.position, formatDate(it.start, it.end), it.teacher)
                                }
                            }
                        }
                    }
                } else {
                    view.rv_home_fragment.visibility = View.INVISIBLE
                    view.tv_home_fragment_loading.visibility = View.VISIBLE
                }

            }


        }

        return view
    }
}