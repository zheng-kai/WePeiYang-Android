package com.twt.scan.scanactivity.api

import com.twt.wepeiyang.commons.experimental.preference.hawk

object ScanPreferences {
    var twtid: Int? by hawk("twt_id", null)
    var permissionLevel :Int? by hawk("permission",null)
}