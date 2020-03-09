package com.twt.scan.scanactivity

import android.content.Intent
import android.support.v7.widget.RecyclerView
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
import kotlinx.android.synthetic.main.scanactivity_item_home.view.*
import kotlinx.android.synthetic.main.scanactivity_item_manager.view.*
import org.jetbrains.anko.layoutInflater
import java.text.SimpleDateFormat
import java.util.*

class HomeItem(val title: String, val location: String, val time: String, val person: String) : Item {
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
            holder as ViewHolder
            item as HomeItem
            val locationStr = "活动地点：${item.location}"
            val timeStr = "活动时间：${item.time}"
            val personStr = "发起人：${item.person}"
            holder.tvTitle.text = item.title
            holder.tvLocation.text = locationStr
            holder.tvTime.text = timeStr
            holder.tvPerson.text = personStr
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

fun MutableList<Item>.add(title: String, location: String, time: String, person: String) = add(HomeItem(title, location, time, person))
class ManagerItem(val title: String, val location: String, val time: String, val person: String, val id: Int) : Item {
//    private var isVisible = false

    companion object Controller : ItemController {
//        private val detailAnimationEnter = AnimationUtils.loadAnimation(CommonContext.application, R.anim.detail_animation_enter)
//
//        private val rotateAnimationEnter = RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
//            fillAfter = true
//            duration = 200
//            repeatCount = 0
//            interpolator = LinearInterpolator()
//        }
//        private val rotateAnimationExit = RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
//            fillAfter = true
//            duration = 200
//            repeatCount = 0
//            interpolator = LinearInterpolator()
//        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = CommonContext.application.layoutInflater.inflate(R.layout.scanactivity_item_manager, parent, false)
            val tvTitle = view.tv_manager_item_title
            val llScanSign = view.ll_manager_item_scan_sign
            val llIdSign = view.ll_manager_item_id_sign
            val llDetail = view.ll_manager_item_detail
            return ViewHolder(view, tvTitle, llScanSign, llIdSign, llDetail)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as ManagerItem
            val locationStr = "活动地点：${item.location}"
            val timeStr = "活动时间：${item.time}"
            val personStr = "发起人：${item.person}"
            holder.tvTitle.text = item.title
            holder.llDetail.tv_manager_detail_location.text = locationStr
            holder.llDetail.tv_manager_detail_time.text = timeStr
            holder.llDetail.tv_manager_detail_initiator.text = personStr
//            holder.ivDetail.setOnClickListener {
//                //                item.showDialog()
//                holder.llDetail.apply {
//                    if (item.isVisible) {
//                        it.startAnimation(rotateAnimationExit)
//                        item.isVisible = false
////                        startAnimation(detailAnimationExit)
//                        visibility = View.GONE
//                    } else {
//                        it.startAnimation(rotateAnimationEnter)
//                        item.isVisible = true
//                        startAnimation(detailAnimationEnter)
//                        visibility = View.VISIBLE
//                    }
////                    visibility = if (item.isVisible) {
////                        it.startAnimation(rotateAnimationExit)
////                        item.isVisible = false
////                        View.GONE
////                    } else {
////                        it.startAnimation(rotateAnimationEnter)
////                        item.isVisible = true
////                        View.VISIBLE
////                    }
//
//                }
//            }
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

    private class ViewHolder(
            itemView: View,
            val tvTitle: TextView,
            val llScanSign: LinearLayout,
            val llIdSign: LinearLayout,
            val llDetail: LinearLayout) : RecyclerView.ViewHolder(itemView)

    override val controller: ItemController
        get() = Controller
}

fun MutableList<Item>.add(title: String, position: String, time: String, teacher: String, activityId: Int) = add(ManagerItem(title, position, time, teacher, activityId))
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