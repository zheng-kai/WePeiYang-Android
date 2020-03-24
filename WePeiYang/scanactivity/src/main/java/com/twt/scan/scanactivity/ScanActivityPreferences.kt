package com.twt.scan.scanactivity

import com.twt.wepeiyang.commons.experimental.preference.shared

object ScanActivityPreferences {
    val userId by shared("pref_user_id_scan_activity",-1)
    val permission by shared("pref_permission_scan_activity",3)
}