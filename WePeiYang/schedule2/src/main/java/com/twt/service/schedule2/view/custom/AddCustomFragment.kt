package com.twt.service.schedule2.view.custom

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatCheckBox
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.twt.service.schedule2.R
import com.twt.service.schedule2.model.Arrange
import com.twt.service.schedule2.model.Week
import com.twt.service.schedule2.model.custom.CustomCourseManager
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import kotlinx.android.synthetic.main.schedule_frag_add_custom.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class AddCustomFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.schedule_frag_add_custom, container, false)
        val mContext = activity

        val button: Button = view.findViewById(R.id.ll_course_arrange)
        val editStartTime: TextInputEditText = view.findViewById(R.id.edit_start_time_name)
        val editEndTime: TextInputEditText = view.findViewById(R.id.edit_end_time_name)
        val editWeekDay: TextInputEditText = view.findViewById(R.id.edit_weekday_name)
        val editStartWeek: TextInputEditText = view.findViewById(R.id.edit_startWeek_name)
        val editEndWeek: TextInputEditText = view.findViewById(R.id.edit_endWeek_name)
        val editList: List<TextInputEditText> = listOf(editStartTime, editEndTime, editWeekDay, editStartWeek, editEndWeek)
        button.apply {
            setBackgroundColor(getColorCompat(R.color.colorPrimary))
            setTextColor(getColorCompat(R.color.white_color))
        }
        editList.forEach {
            it.apply {
                inputType = EditorInfo.TYPE_CLASS_PHONE
            }
        }

        val oddWeekCheckbox = view.findViewById<AppCompatCheckBox>(R.id.odd_week_checkbox)
        val evenWeekCheckbox = view.findViewById<AppCompatCheckBox>(R.id.even_week_checkbox)
        val bothWeekCheckbox = view.findViewById<AppCompatCheckBox>(R.id.both_week_checkbox)
        val checkBoxList: List<CheckBox> = listOf(oddWeekCheckbox, evenWeekCheckbox, bothWeekCheckbox)
        var str: String = ""
        checkBoxList.forEach {
            it.apply {
                setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        str = buttonView.text.toString()
                        checkBoxList.forEach { it.isChecked = false }
                        it.isChecked = true
                    }
                }
            }
        }


        button.setOnClickListener {
            var courseName: String = edit_course_name.text.toString()
            var teacherName = edit_course_teacher.text.toString()
            var week: String = str
            var startTime: Int = edit_start_time_name.text.toString().trim().toInt()
            var endTime: Int = edit_end_time_name.text.toString().trim().toInt()
            var weekday: Int = edit_weekday_name.text.toString().trim().toInt()
            var room: String = edit_room_name.text.toString()
            var startWeek: Int = edit_startWeek_name.text.toString().trim().toInt()
            var endWeek: Int = edit_endWeek_name.text.toString().trim().toInt()

            var weekPeriod = Week(startWeek, endWeek)
            var arrange = Arrange(week, startTime, endTime, weekday, room)

            async(CommonPool + QuietCoroutineExceptionHandler) {
                CustomCourseManager.addCustomCourse(courseName, teacherName, listOf(arrange), weekPeriod)
                Toast.makeText(mContext, "自定义事件：${courseName}添加成功", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}