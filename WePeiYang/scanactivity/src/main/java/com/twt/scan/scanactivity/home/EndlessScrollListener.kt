package com.twt.scan.scanactivity.home

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.widget.LinearLayout

abstract class EndlessScrollListener : RecyclerView.OnScrollListener() {
    private var slidingUp = false
    private var enable = true
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val manager = recyclerView.layoutManager
        if(manager is StaggeredGridLayoutManager){
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val array = manager.findLastCompletelyVisibleItemPositions(null)
                val last = array.max()
                val count = manager.itemCount
                if (last == (count - 1) && slidingUp) {
                    if(enable){
                        enable = false
                        loadMore()
                    }
                }
            }
        }else if(manager is LinearLayoutManager){
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val last = manager.findLastCompletelyVisibleItemPosition()
                val count = manager.itemCount
                if (last == (count - 1) && slidingUp) {
                    if(enable){
                        enable = false
                        loadMore()
                    }
                }
            }
        }


    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        slidingUp = dy > 0
    }

    fun enable(){
        enable = true
    }
    abstract fun loadMore()
}