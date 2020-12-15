package com.yxd.lvjie.activity

import android.content.Intent
import android.os.Bundle
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.utils.AppUtils
import com.yxd.baselib.utils.BusUtils
import com.yxd.baselib.utils.DialogUtils
import com.yxd.baselib.utils.OK
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.constant.Cmd
import com.yxd.lvjie.constant.Cmd.DEVICE_AWAKE_TIME
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.dialog.ProjectDialog
import com.yxd.lvjie.helper.SPHelper
import com.yxd.lvjie.net.Req
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * 设置
 * @author YXD
 */
@LayoutId(R.layout.activity_setting)
class SettingActivity : ProjectBaseActivity() {

    override fun init(bundle: Bundle?) {
        val listSetting = listOf(
            "固件版本" to "V1.0 升级",
            "line" to "",
            "设备唤醒时间" to "12:42:20",
            "仪器选择" to "调试模式",
            "算法选择" to "算法1",
            "line" to "",
            "高级设置" to "",
            "设备信息" to "",
            "应用更新" to "",
            "退出登录" to ""
        )

        BusUtils.post(MsgWhat.SEND_COMMAND, DEVICE_AWAKE_TIME)

        rvSetting.wrap.generate(listSetting,
            { h, p, item ->
                if (p != 0 && item.first != "line") {
                    h.tv(R.id.tvLeft).txt(item.first)
                    h.tv(R.id.tvRight).txt(item.second)
                    if (p == 3) h.iv(R.id.ivMore).hide()
                }
                h.itemClick {
                    when (item.first) {
                        "高级设置" -> goTo<AdvancedSettingActivity>()
                        "应用更新" -> {
                            Req.getApkVersionInfo {
                                if (it.data.version != AppUtils.getVersionName()) {
                                    ProjectDialog(this).setInfo(
                                        "检测到有可用升级版本${it.data.version}\n确定要更新吗",
                                        "更新", true
                                    ) { dialog ->
                                        dialog.dismiss()
                                        val dialogProgress = DialogUtils.createProgressDialog(
                                            this, "正在下载中...", haveProgressValue = true
                                        )
                                        dialogProgress.show()
                                        OK.downloadFile(it.data.downloadUrl, SDCARD, "渗漏预警平台.apk", {
                                            dialogProgress.dismiss()
                                            AppUtils.install(
                                                this,
                                                "com.yxd.lvjie.fileprovider",
                                                it.absolutePath
                                            )
                                        }) {
                                            dialogProgress.progress = 100.times(it).toInt()
                                        }
                                    }.show()
                                }
                            }
                        }
                        "设备信息" -> goTo<DeviceInfoActivity>()
                        "退出登录" -> {
                            ProjectDialog(this).setInfo("是否要退出登录？", "确定", true) {
                                goTo<LoginActivity>()
                                SPHelper.putToken("")
                                finishAllActivities()
                            }.show()
                        }
                    }
                }
            }, {
                when {
                    it == 0 -> 0
                    listSetting[it].first == "line" -> 2
                    else -> 1
                }
            }, R.layout.item_setting_top, R.layout.item_setting, R.layout.line_setting
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AppUtils.doInOnActivityResult(
            this, "com.yxd.lvjie.fileprovider",
            "${SDCARD}/渗漏预警平台.apk", requestCode
        )
    }

}