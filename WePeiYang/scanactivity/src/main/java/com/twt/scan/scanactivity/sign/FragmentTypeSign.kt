package com.twt.scan.scanactivity.sign

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.twt.scan.scanactivity.R
import com.twt.scan.scanactivity.StringUtils
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.scanactivity_fragment_sign_type.view.*
import kotlinx.coroutines.*
import com.twt.scan.scanactivity.api.ScanActivityApi
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences

class FragmentTypeSign : Fragment() {
    var studentId = ""
    var activityId = 0
    private lateinit var tvHint: TextView
    private lateinit var etId: EditText
    //    private lateinit var tvLoadHint: TextView
    private lateinit var listener: CanChangeFragment

    companion object {
        fun newInstance(): FragmentTypeSign {
            return FragmentTypeSign()
        }
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        if (activity is CanChangeFragment) {
            listener = activity
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.scanactivity_fragment_sign_type, container, false)
        etId = view.et_fragment_sign_type_id
        tvHint = view.tv_fragment_sign_type_hint
//        tvLoadHint = view.tv_fragment_sign_type_load_hint
        view.btn_fragment_sign_change.setOnClickListener {
            listener.changeFragment()
        }
        view.btn_fragment_sign_type_confirm.setOnClickListener {
            if (isIdValid()) {
                GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                    //                    val res = ScanActivityApi.getNameByNumber(studentId).await()
//                    if (res.error_code != 0) {
//                        Toasty.error(CommonContext.application, res.message)
//                    } else {
                    AlertDialog.Builder(context)
                            .setTitle("录入信息为\n")
                            .setMessage("$studentId  张三\n\n是否录入?")
                            .setPositiveButton("取消") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .setNegativeButton("确认") { dialog, _ ->
                                GlobalScope.launch(Dispatchers.Default + QuietCoroutineExceptionHandler) {
//                                    val result = ScanActivityApi.sign(activityId, studentId, (System.currentTimeMillis() / 1000).toInt()).await()
//                                    if (result.error_code != 0) {
//                                        Toasty.error(CommonContext.application, result.message)
//                                    } else {
//                                        Toasty.normal(CommonContext.application, result.message)
//                                    }
// TODO
                                }
                                Toasty.normal(CommonContext.application, "录入成功！").show()
                                dialog.dismiss()
                            }
                            .create()
                            .show()
//                    }
                }
            }
        }
        retainInstance = true
        return view
    }

    private fun isIdValid(): Boolean {
        studentId = StringUtils.replaceBlank(etId.text.toString())
        if (studentId == "") {
            tvHint.text = "学号不能为空"
            return false
        } else if (studentId.length != 10) {
            tvHint.text = "学号为10位"
            return false
        }
        tvHint.text = ""
        return true
    }
}