package com.twt.scan.scanactivity.sign

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cn.bingoogolapple.qrcode.core.BarcodeType
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.bingoogolapple.qrcode.zxing.ZXingView
import com.google.gson.Gson
import com.twt.scan.scanactivity.R
import com.twt.wepeiyang.commons.experimental.CommonContext
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.scanactivity_fragment_sign_scan.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions
import kotlinx.coroutines.delay

class FragmentScanSign : Fragment(), QRCodeView.Delegate, EasyPermissions.PermissionCallbacks {

    private val REQUEST_CODE_QRCODE_PERMISSIONS = 1
    var activityId = 0
    private val gson = Gson()
    lateinit var zxyScan: ZXingView
    private lateinit var listener: CanChangeFragment
    companion object {
        fun newInstance(): FragmentScanSign {
            return FragmentScanSign()
        }
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        if(activity is CanChangeFragment){
            listener = activity
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.scanactivity_fragment_sign_scan, container, false)
        zxyScan = view.zxv_fragment_scan_sign
        zxyScan.setDelegate(this)
        view.tv_fragment_scan_change.setOnClickListener {
            listener.changeFragment()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        requestCodeQRCodePermissions()
    }

    override fun onStop() {
        zxyScan.stopCamera() // 关闭摄像头预览，并且隐藏扫描框
        super.onStop()
    }

    override fun onDestroy() {
        zxyScan.onDestroy() // 销毁二维码扫描控件
        super.onDestroy()

    }

    override fun onScanQRCodeSuccess(result: String?) {
        vibrate()
//        val qrInfo = gson.fromJson<QRInfo>(result, QRInfo::class.java)
//        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
//            AlertDialog.Builder(context)
//                    .setTitle("录入信息为")
//                    .setMessage("${qrInfo.id}  ${qrInfo.name}\n\n是否录入?")
//                    .setNegativeButton("否") { dialog, _ ->
//                        dialog.dismiss()
//                    }
//                    .setPositiveButton("是") { dialog, _ ->
//                        GlobalScope.launch(Dispatchers.Default + QuietCoroutineExceptionHandler) {
//                            val commonBody = ScanActivityApi.sign(activityId, qrInfo.id, qrInfo.name, (System.currentTimeMillis() / 1000).toInt()).await()
//                            if (commonBody.error_code != 0) {
//                                Toasty.error(CommonContext.application, commonBody.message)
//                            } else {
//                                Toasty.normal(CommonContext.application, commonBody.message)
//                            }
//
//                        }
//                        Toasty.normal(CommonContext.application, "录入成功！").show()
//                        dialog.dismiss()
//                    }
//                    .create()
//                    .show()
//        }
        Toasty.normal(CommonContext.application,"录入成功", Toast.LENGTH_SHORT).show()
        GlobalScope.launch {
            delay(1000)
            zxyScan.startSpot()

        }
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        var tipText = zxyScan.scanBoxView.tipText
        val ambientBrightnessTip = "\n环境过暗，请打开闪光灯"
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                zxyScan.scanBoxView.tipText = tipText + ambientBrightnessTip
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip))
                zxyScan.scanBoxView.tipText = tipText
            }
        }
    }

    override fun onScanQRCodeOpenCameraError() {
        Toasty.error(CommonContext.application, "扫码失败").show()
    }

    private fun vibrate() {
        val vibrator = CommonContext.application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(200L)
    }

    private fun requestCodeQRCodePermissions() {
        val perms = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (!EasyPermissions.hasPermissions(CommonContext.application, *perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, *perms)
        } else {
            zxyScan.startCamera() // 打开后置摄像头开始预览，但是并未开始识别
            //        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
            zxyScan.setType(BarcodeType.TWO_DIMENSION, null) // 只识别二维条码
            zxyScan.startSpotAndShowRect() // 显示扫描框，并开始识别
        }
    }

    // 权限申请
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
        zxyScan.startCamera() // 打开后置摄像头开始预览，但是并未开始识别
        //        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
        zxyScan.setType(BarcodeType.TWO_DIMENSION, null) // 只识别二维条码
        zxyScan.startSpotAndShowRect() // 显示扫描框，并开始识别
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}