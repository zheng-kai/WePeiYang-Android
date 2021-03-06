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
import com.twt.scan.scanactivity.api.ScanActivityService
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.scanactivity_fragment_sign_type.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class FragmentTypeSign : Fragment() {
    var studentId = ""
    var studentName = ""
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
//                    val res = ScanActivityService.getNameByNumber(studentId).await()
                AlertDialog.Builder(context)
                        .setTitle("录入信息为\n")
                        .setMessage("$studentId  ${studentName}\n\n是否录入?")
                        .setPositiveButton("取消") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setNegativeButton("确认") { dialog, _ ->
                            GlobalScope.launch(Dispatchers.IO + QuietCoroutineExceptionHandler) {
                                val resultBody = ScanActivityService.sign(activityId, studentId, System.currentTimeMillis()).await()
                                withContext(Main) {
                                    if (resultBody.error_code != 0) {
                                        context?.let {
                                            Toasty.error(it, resultBody.message).show()
                                        }
                                    } else {
                                        context?.let {
                                            Toasty.normal(it, resultBody.message).show()

                                        }
                                    }
                                    dialog.dismiss()
                                }
                            }
                        }
                        .create()
                        .show()
//                    }

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