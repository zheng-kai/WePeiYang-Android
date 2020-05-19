package com.twt.scan.scanactivity

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.twt.scan.scanactivity.sign.SignActivity
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import kotlinx.android.synthetic.main.scanactivity_item_footer.view.*
import kotlinx.android.synthetic.main.scanactivity_item_home.view.*
import kotlinx.android.synthetic.main.scanactivity_item_manager.view.*
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.wrapContent
import java.text.SimpleDateFormat
import java.util.*

class HomeItem(val title: String, val location: String, val time: String, val person: String, val id: Int) : Item {
    override fun areItemsTheSame(newItem: Item): Boolean {
        if (newItem is HomeItem) {
            return id == newItem.id
        }
        return false
    }

    override fun areContentsTheSame(newItem: Item): Boolean {
        if (newItem is HomeItem) {
            return id == newItem.id
        }
        return false
    }

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = CommonContext.application.layoutInflater.inflate(R.layout.scanactivity_item_home, parent, false)
            val tvTitle = view.tv_home_item_title
            val tvLocation = view.tv_home_location
            val tvTime = view.tv_home_time
            val tvPerson = view.tv_home_person
            return ViewHolder(view, tvTitle, tvLocation, tvTime, tvPerson)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            if (holder is ViewHolder && item is HomeItem) {
                val locationStr = "活动地点：${item.location}"
                val timeStr = "活动时间：${item.time}"
                val personStr = "发起人：${item.person}"
                holder.tvTitle.text = item.title
                holder.tvLocation.text = locationStr
                holder.tvTime.text = timeStr
                holder.tvPerson.text = personStr
            }
        }

    }

    private class ViewHolder(
            itemView: View,
            val tvTitle: TextView,
            val tvLocation: TextView,
            val tvTime: TextView,
            val tvPerson: TextView) : RecyclerView.ViewHolder(itemView)

    override val controller: ItemController
        get() = Controller
}

class ManagerItem(val title: String, val location: String, val time: String, val person: String, val id: Int) : Item {

    override fun areItemsTheSame(newItem: Item): Boolean {
        if (newItem is ManagerItem) {
            return id == newItem.id
        }
        return false
    }

    override fun areContentsTheSame(newItem: Item): Boolean {
        if (newItem is ManagerItem) {
            return id == newItem.id
        }
        return false
    }

    companion object Controller : ItemController {

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = CommonContext.application.layoutInflater.inflate(R.layout.scanactivity_item_manager, parent, false)
            val tvTitle = view.tv_manager_item_title
            val llScanSign = view.ll_manager_item_scan_sign
            val llIdSign = view.ll_manager_item_id_sign
            val llDetail = view.ll_manager_item_detail
            return ViewHolder(view, tvTitle, llScanSign, llIdSign, llDetail)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {

            if (holder is ViewHolder && item is ManagerItem) {
                val locationStr = "活动地点：${item.location}"
                val timeStr = "活动时间：${item.time}"
                val personStr = "发起人：${item.person}"
                holder.tvTitle.text = item.title
                holder.llDetail.tv_manager_detail_location.text = locationStr
                holder.llDetail.tv_manager_detail_time.text = timeStr
                holder.llDetail.tv_manager_detail_initiator.text = personStr

                holder.llIdSign.setOnClickListener {
                    val intent = Intent(CommonContext.application, SignActivity::class.java)
                    intent.putExtra("fragmentType", "Type")
                    intent.putExtra("activityId", item.id)
                    CommonContext.application.startActivity(intent)
                }
                holder.llScanSign.setOnClickListener {
                    val intent = Intent(CommonContext.application, SignActivity::class.java)
                    intent.putExtra("fragmentType", "Scan")
                    intent.putExtra("activityId", item.id)
                    CommonContext.application.startActivity(intent)
                }
            }
        }

    }

    private class ViewHolder(
            itemView: View,
            val tvTitle: TextView,
            val llScanSign: LinearLayout,
            val llIdSign: LinearLayout,
            val llDetail: LinearLayout) : RecyclerView.ViewHolder(itemView)

    override val controller: ItemController
        get() = Controller
}

class Footer(val content: String) : Item {
    override fun areContentsTheSame(newItem: Item): Boolean {
        if (newItem is Footer) {
            return content == newItem.content
        }
        return false
    }

    override fun areItemsTheSame(newItem: Item): Boolean {
        return areContentsTheSame(newItem)
    }

    companion object Companion : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = CommonContext.application.layoutInflater.inflate(R.layout.scanactivity_item_footer, parent, false)
            return MyViewHolder(view, view.tv_item_footer)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            if (holder is MyViewHolder && item is Footer) {
                holder.tvContent.text = item.content
            }
        }

    }

    private class MyViewHolder(itemView: View, val tvContent: TextView) : RecyclerView.ViewHolder(itemView)

    override val controller: ItemController = Companion
}

fun MutableList<Item>.addNormalActivity(normalActivityId: Int, title: String?, location: String?, time: String?, teacher: String?) = add(HomeItem(title
        ?: "无", location ?: "未知", time ?: "待定", teacher ?: "无", normalActivityId))

fun MutableList<Item>.addManagerActivity(title: String?, position: String?, time: String?, teacher: String?, activityId: Int) = add(ManagerItem(title
        ?: "无", position ?: "未知", time ?: "待定", teacher ?: "无", activityId))

fun MutableList<Item>.addFooter(content: String) = Footer(content)
fun formatDate(start: String, end: String): String {
    val simple = SimpleDateFormat("yyyy年MM月dd日 HH:mm")
    val calendar = Calendar.getInstance().apply {
        timeInMillis = start.toLong() * 1000
    }
    val simple2 = SimpleDateFormat("-HH:mm")
    val calendar2 = Calendar.getInstance().apply {
        timeInMillis = end.toLong() * 1000
    }
    return simple.format(calendar.timeInMillis) + simple2.format(calendar2.timeInMillis)
}