package com.twt.service.theory.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.twt.service.theory.R
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.ui.rec.withItems
import kotlinx.android.synthetic.main.theory_activity_answer.*
import kotlinx.android.synthetic.main.theory_activity_exam_detail.*
import kotlinx.android.synthetic.main.theory_common_toolbar.*
import kotlinx.android.synthetic.main.theory_dialog_exam.*
import kotlinx.android.synthetic.main.theory_dialog_exam.view.*
import kotlinx.android.synthetic.main.theory_popupwindow_layout.view.*
import org.jetbrains.anko.dip

@RequiresApi(Build.VERSION_CODES.M)
class AnswerActivity : AppCompatActivity() {
    override fun onBackPressed() {
        if (AnswerManager.isPopUPWindowInstalled()) {
            AnswerManager.getPopUpWindow()?.dismiss()
            AnswerManager.uninstall()
        } else {
            val popupWindow = PopupWindow(this)
            val view = LayoutInflater.from(this).inflate(R.layout.theory_dialog_exam, null, false)
            view.theory_enter_word.text = "你将退出考试"
            popupWindow.apply {
                isFocusable = true
                contentView = view
                width = WindowManager.LayoutParams.MATCH_PARENT
                animationStyle = R.style.style_pop_animation
                setBackgroundDrawable(null)
                showAtLocation(
                        LayoutInflater.from(contentView.context).inflate(R.layout.theory_activity_answer, null),
                        Gravity.CENTER,
                        0,
                        0
                )
                contentView.theory_enter_cancel.setOnClickListener {
                    popupWindow.dismiss()
                }
                contentView.theory_enter_comfirm.setOnClickListener {
                    popupWindow.dismiss()
                    super.onBackPressed()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.theory_activity_answer)
        initView()
        loadQuestions()
    }

    private fun initView() {
        window.statusBarColor = Color.parseColor("#FFFFFF")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        enableLightStatusBarMode(true)
        theory_submit.visibility = View.VISIBLE
        theory_person_profile.visibility = View.GONE
        theory_search.visibility = View.GONE
        theory_constraintLayout.setOnClickListener {
            val popupWindow = PopupWindow(this)
            val view = LayoutInflater.from(this).inflate(R.layout.theory_popupwindow_layout, null, false)
            popupWindow.apply {
                contentView = view
                width = WindowManager.LayoutParams.MATCH_PARENT
                animationStyle = R.style.style_pop_animation
                setBackgroundDrawable(null)
                height = dip(280)
                showAtLocation(
                        LayoutInflater.from(contentView.context).inflate(R.layout.theory_activity_answer, null),
                        Gravity.BOTTOM,
                        0,
                        0
                )
                contentView.theory_constraintLayout2.setOnClickListener {
                    popupWindow.dismiss()
                    AnswerManager.uninstall()
                }

                contentView.theory_recyclerView.layoutManager = GridLayoutManager(contentView.context, 8, GridLayoutManager.VERTICAL, false)

            }
            AnswerManager.installPopUpWindow(popupWindow, theory_exam_questions)
        }
        theory_back.setOnClickListener {
            val popupWindow = PopupWindow(this)
            val view = LayoutInflater.from(this).inflate(R.layout.theory_dialog_exam, null, false)
            view.theory_enter_word.text = "你将退出考试"
            popupWindow.apply {
                isFocusable = true
                contentView = view
                width = WindowManager.LayoutParams.MATCH_PARENT
                animationStyle = R.style.style_pop_animation
                setBackgroundDrawable(null)
                showAtLocation(
                        LayoutInflater.from(contentView.context).inflate(R.layout.theory_activity_answer, null),
                        Gravity.CENTER,
                        0,
                        0
                )
                contentView.theory_enter_cancel.setOnClickListener {
                    popupWindow.dismiss()
                }
                contentView.theory_enter_comfirm.setOnClickListener {
                    popupWindow.dismiss()
                    finish()
                }
            }
        }
        theory_submit.setOnClickListener {
            val popupWindow = PopupWindow(this)
            val view = LayoutInflater.from(this).inflate(R.layout.theory_dialog_exam, null, false)
            if (AnswerManager.getNumberHasDone() != AnswerManager.getTotalNumber()) view.theory_enter_word.text = "你还有${AnswerManager.getTotalNumber() - AnswerManager.getNumberHasDone()}题未完成，确认提交?"
            else view.theory_enter_word.text = "你将提交答案"
            popupWindow.apply {
                isFocusable = true
                contentView = view
                width = WindowManager.LayoutParams.MATCH_PARENT
                animationStyle = R.style.style_pop_animation
                setBackgroundDrawable(null)
                showAtLocation(
                        LayoutInflater.from(contentView.context).inflate(R.layout.theory_activity_answer, null),
                        Gravity.CENTER,
                        0,
                        0
                )
                contentView.theory_enter_cancel.setOnClickListener {
                    popupWindow.dismiss()
                }
                contentView.theory_enter_comfirm.setOnClickListener {
                    popupWindow.dismiss()
                    finish()
                }
            }
        }
    }

    private fun loadQuestions() {
        val recyclerView = findViewById<RecyclerView>(R.id.theory_exam_questions)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.withItems {
            repeat(6) {
                setMultiAnsQues(3)
                setMultiAnsQues(4)
                setMultiAnsQues(5)
                setMultiAnsQues(6)
            }
            AnswerManager.init(this.size)
        }

    }

}