package com.twt.service.ecard.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twt.service.ecard.R
import com.twt.service.ecard.model.EcardPref
import com.twt.service.ecard.model.EcardPref.IS_CONSUME
import com.twt.service.ecard.model.EcardPref.IS_RECHARGE
import com.twt.service.ecard.model.EcardService
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.mta.mtaClick
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.lightText
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twt.wepeiyang.commons.ui.text.spanned
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.dip
import org.jetbrains.anko.horizontalPadding

class EcardPreviousFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    private val itemManager by lazy { recyclerView.withItems(listOf()) }
    var typeOfPrecious = EcardPref.PRE_LIST

    companion object {
        fun newInstance(type: String): EcardPreviousFragment {
            val args = Bundle()
            args.putString(EcardPref.INDEX_KEY, type)
            val fragment = EcardPreviousFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.ecard_fragment_prescious, container, false)
        recyclerView = view.findViewById(R.id.rv_ecard_precious)
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        val bundle = arguments
        typeOfPrecious = bundle!!.getString(EcardPref.INDEX_KEY)

        when (typeOfPrecious) {
            EcardPref.PRE_LIST -> refreshDataForHistory()
            EcardPref.PRE_CHART -> refreshDataForChart()
        }

        return view
    }

    fun refreshDataForHistory() {
        itemManager.refreshAll {
            lightText("正在加载数据")
        }

        launch(UI + QuietCoroutineExceptionHandler) {
            val transactionList = EcardService.getEcardTransaction(term = EcardPref.ecardHistoryLength).awaitAndHandle {
                it.printStackTrace()
                itemManager.refreshAll {
                    lightText("") {
                        horizontalPadding = dip(16)
                        text = ("<span style=\"color:#E70C57\";>拉取${EcardPref.ecardHistoryLength}天校园卡数据失败，这可能是因为您的拉取时长超越了校园卡有效期，" +
                                "此情况通常会发生在补办饭卡之后，查询期限超越补办期 \n 当然也可能是是饭卡的账号密码错了...</span>").spanned
                    }
                }
            }?.data

            val historyData = transactionList ?: return@launch
            Toasty.success(this@EcardPreviousFragment.context!!, "拉取数据成功：${EcardPref.ecardHistoryLength}天").show()

            itemManager.refreshAll {
                historyData.forEach {
                    transactionItem(it, it.type == IS_CONSUME)
                }
            }
            recyclerView.scrollToPosition(0)
        }
    }

    fun refreshDataForChart() {
        itemManager.refreshAll {
            lightText("正在加载数据")
        }

        launch(UI + QuietCoroutineExceptionHandler) {
            val transactionList = EcardService.getEcardTransaction(term = EcardPref.ecardHistoryLength).awaitAndHandle {
                it.printStackTrace()
                itemManager.refreshAll {
                    lightText("") {
                        horizontalPadding = dip(16)
                        text = ("<span style=\"color:#E70C57\";>拉取${EcardPref.ecardHistoryLength}天校园卡数据失败，这可能是因为您的拉取时长超越了校园卡有效期，" +
                                "此情况通常会发生在补办饭卡之后，查询期限超越补办期 \n 当然也可能是是饭卡的账号密码错了...</span>").spanned
                    }
                }
            }?.data
            val lineChartDataList = EcardService.getEcardLineChartData().awaitAndHandle {
                it.printStackTrace()
                itemManager.refreshAll {
                    lightText("") {
                        horizontalPadding = dip(16)
                        text = ("<span style=\"color:#E70C57\";>拉取${EcardPref.ecardHistoryLength}天校园卡数据失败，这可能是因为您的拉取时长超越了校园卡有效期，" +
                                "此情况通常会发生在补办饭卡之后，查询期限超越补办期 \n 当然也可能是是饭卡的账号密码错了...</span>").spanned
                    }
                }
            }?.data

            val historyData = transactionList ?: return@launch
            val lineChartHistoryData = lineChartDataList ?: return@launch

            Toasty.success(this@EcardPreviousFragment.context!!, "拉取数据成功：${EcardPref.ecardHistoryLength}天").show()

            itemManager.refreshAll {
                ecardPreTotalItem(historyData.toMutableList().filter { it.type == IS_CONSUME }, historyData.toMutableList().filter { it.type == IS_RECHARGE })
                Log.d("momom", lineChartHistoryData.size.toString())
                ecardChartItem(lineChartHistoryData.reversed().take(EcardPref.ecardHistoryLength))
            }
        }
    }

}