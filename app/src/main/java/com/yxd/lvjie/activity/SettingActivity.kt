package com.yxd.lvjie.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Message
import com.yxd.baselib.annotation.Bus
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.utils.AppUtils
import com.yxd.baselib.utils.BusUtils
import com.yxd.baselib.utils.DialogUtils
import com.yxd.baselib.utils.OK
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.constant.Cmd
import com.yxd.lvjie.constant.Cmd.DEVICE_AWAKE_TIME
import com.yxd.lvjie.constant.Cmd.READ_ARITHMETIC
import com.yxd.lvjie.constant.Cmd.WRITE_ARITHMETIC1
import com.yxd.lvjie.constant.Cmd.WRITE_ARITHMETIC2
import com.yxd.lvjie.constant.Cmd.WRITE_ARITHMETIC3
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.constant.MsgWhat.CMD_DEVICE_AWAKE_TIME
import com.yxd.lvjie.constant.MsgWhat.CMD_READ_ARTH
import com.yxd.lvjie.dialog.ProjectDialog
import com.yxd.lvjie.helper.SPHelper
import com.yxd.lvjie.net.Req
import kotlinx.android.synthetic.main.activity_setting.*
import org.greenrobot.eventbus.Subscribe

/**
 * 设置
 * @author YXD
 */
@Bus
@LayoutId(R.layout.activity_setting)
class SettingActivity : ProjectBaseActivity() {

    private lateinit var dialogClose: ProgressDialog
    var currentAwakeTime = ""

    var checkItem = 0

    @Subscribe
    fun handle(msg: Message) {
        when (msg.what) {
            CMD_DEVICE_AWAKE_TIME -> {
                currentAwakeTime = msg.obj.toString()
                rvSetting.getChildAt(2).tv(R.id.tvRight).txt(msg.obj)
            }
            CMD_READ_ARTH -> {
                checkItem = msg.obj.toString().toInt() - 1
                rvSetting.getChildAt(4).tv(R.id.tvRight).txt("算法${msg.obj}")
            }
            MsgWhat.DEVICE_DISCONNECT -> {
                dialogClose.dismiss()
                ProjectDialog(this).setInfo(
                    "设备已断开连接", "确定",
                    false
                ) {
                    it.dismiss()
                }.show()
            }
        }
    }

    override fun init(bundle: Bundle?) {
        val listSetting = listOf(
            "固件版本" to "V1.0 升级",
            "line" to "",
            "设备唤醒时间" to "",
            "仪器选择" to "调试模式",
            "算法选择" to "",
            "line" to "",
            "高级设置" to "",
            "设备信息" to "",
            "应用更新" to "",
            "退出登录" to ""
        )

        if (HomeActivity.isConnectedDevice) {
            val pd = DialogUtils.createProgressDialog(this, "正在获取数据...")
            pd.show()
            BusUtils.post(MsgWhat.SEND_COMMAND, DEVICE_AWAKE_TIME)
            doDelayTask(2000) {
                pd.dismiss()
                BusUtils.post(MsgWhat.SEND_COMMAND, READ_ARITHMETIC)
            }
        }

        val dialogArth = DialogUtils.createOptionsDialog(
            this, arrayOf("算法1", "算法2", "算法3"),
            checkItem, {
                checkItem = it
                when (it) {
                    0 -> BusUtils.post(MsgWhat.SEND_COMMAND, WRITE_ARITHMETIC1)
                    1 -> BusUtils.post(MsgWhat.SEND_COMMAND, WRITE_ARITHMETIC2)
                    2 -> BusUtils.post(MsgWhat.SEND_COMMAND, WRITE_ARITHMETIC3)
                }
                rvSetting.getChildAt(4).tv(R.id.tvRight).txt("算法${checkItem + 1}")
            }, "算法选择"
        )

        dialogClose = DialogUtils.createProgressDialog(this, "正在关闭设备")

        tvQuitDebugMode.click(2) {
            if (!HomeActivity.isConnectedDevice) {
                "请先连接设备".toast()
                return@click
            }
            ProjectDialog(this).setInfo("退出后将会关闭设备，确定要退出吗", "确定", true) {
                it.dismiss()
                dialogClose.show()
                BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.POWER_OFF)
            }.show()
        }

        rvSetting.wrap.generate(listSetting,
            { h, p, item ->
                if (p != 0 && item.first != "line") {
                    h.tv(R.id.tvLeft).txt(item.first)
                    h.tv(R.id.tvRight).txt(item.second)
                    if (p == 3) h.iv(R.id.ivMore).hide()
                }
                h.itemClick {
                    when (item.first) {
                        "算法选择" -> {
                            if (HomeActivity.isConnectedDevice)
                                dialogArth.show()
                            else
                                "请先连接设备".toast()
                        }
                        "设备唤醒时间" -> {
                            if (HomeActivity.isConnectedDevice)
                                goTo<TimeSetActivity>("time" to currentAwakeTime)
                            else
                                "请先连接设备".toast()
                        }
                        "高级设置" -> {
                            if (HomeActivity.isConnectedDevice)
                                goTo<AdvancedSettingActivity>()
                            else
                                "请先连接设备".toast()
                        }
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
                                } else {
//                                    "当前已经是最新版本！".toast()
                                    runOnUiThread {
                                        ProjectDialog(this).setInfo("当前已经是最新版本", "确认", false) {
                                            it.dismiss()
                                        }.show()
                                    }
                                }
                            }
                        }
                        "设备信息" -> {
//                            goTo<DeviceInfoActivity>()
                            if (HomeActivity.isConnectedDevice)
                                goTo<DeviceInfoActivity>()
                            else
                                "请先连接设备".toast()
                        }
                        "退出登录" -> {
                            ProjectDialog(this).setInfo("是否要退出登录？", "确定", true) {
                                goTo<LoginActivity>()
                                SPHelper.putToken("")
                                SPHelper.putPassword("")
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

    /**
     * 处理安装APK权限回调
     * @param requestCode Int
     * @param resultCode Int
     * @param data Intent?
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AppUtils.doInOnActivityResult(
            this, "com.yxd.lvjie.fileprovider",
            "${SDCARD}/渗漏预警平台.apk", requestCode
        )
    }

}