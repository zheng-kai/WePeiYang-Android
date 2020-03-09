package com.twt.scan.scanactivity.home

import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twt.scan.scanactivity.DataViewModel
import com.twt.scan.scanactivity.R
import com.twt.scan.scanactivity.add
import com.twt.scan.scanactivity.formatDate
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.refreshAll
import kotlinx.android.synthetic.main.scanactivity_fragment_home.view.*

class HomeFragment : Fragment(){
    var title = ""
    companion object {
        const val MANAGER_TITLE = "管理"
        const val NOT_JOINED_TITLE = "待参加"
        const val JOINED_TITLE = "已参加"
        fun newInstance(title:String): HomeFragment = HomeFragment().apply {
            this.title = title
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.scanactivity_fragment_home, container, false)
        view.rv_home_fragment.layoutManager = LinearLayoutManager(CommonContext.application)
        view.srl_home_fragment.setOnRefreshListener {
            when(title){
                MANAGER_TITLE -> DataViewModel.getManagerBean()
                NOT_JOINED_TITLE -> DataViewModel.getNotJoinBean()
                JOINED_TITLE -> DataViewModel.getJoinBean()
            }
            view.srl_home_fragment.isRefreshing = false
        }
        when(title){

            MANAGER_TITLE ->{
                DataViewModel.managerBeanLiveData.bindNonNull(this.activity as LifecycleOwner){
                    view.rv_home_fragment.refreshAll {
                        it?.forEach {
                            add(it.title, it.position, formatDate(it.start, it.end), it.teacher, it.activity_id)
                        }
                    }
                }
            }
            NOT_JOINED_TITLE ->{
                DataViewModel.homeBeanNotJoinLiveData.bindNonNull(this.activity as LifecycleOwner){
                    view.rv_home_fragment.refreshAll {
                        it?.forEach {
                            add(it.title,it.position, formatDate(it.start, it.end),it.teacher)
                        }
                    }
                }
            }
            JOINED_TITLE ->{
                DataViewModel.homeBeanJoinLiveData.bindNonNull(this.activity as LifecycleOwner) {
                    view.rv_home_fragment.refreshAll {
                        it?.forEach {
                            add(it.title,it.position, formatDate(it.start, it.end),it.teacher)
                        }
                    }
                }
            }
        }
        return view
    }
}