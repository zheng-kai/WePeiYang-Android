package com.twt.service.schedule2.model.exam

import com.twt.wepeiyang.commons.experimental.preference.hawk

object ExamTablePreference {
    var exams: List<ExamTableBean> by hawk("exam_table_key", mutableListOf())
}
