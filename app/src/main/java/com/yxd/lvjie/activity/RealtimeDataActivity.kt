package com.yxd.lvjie.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Message
import com.yxd.baselib.annotation.Bus
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.utils.DialogUtils
import com.yxd.baselib.utils.TimerUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.activity.HomeActivity.Companion.isConnectedDevice
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.dialog.ProjectDialog
import com.yxd.lvjie.helper.SPHelper
import com.yxd.lvjie.utils.CmdUtils
import kotlinx.android.synthetic.main.activity_realtime_data.*
import kotlinx.android.synthetic.main.item_device_list.*
import org.greenrobot.eventbus.Subscribe
import java.math.RoundingMode

/**
 * 实时数据
 */
@Bus
@LayoutId(R.layout.activity_realtime_data)
class RealtimeDataActivity : ProjectBaseActivity() {

    private lateinit var timer: CountDownTimer

    @Subscribe
    fun handle(msg:Message){
        when(msg.what){
            MsgWhat.DEVICE_DISCONNECT->{
                ProjectDialog(this).setInfo("设备已断开连接，请重新连接！", "确定",
                    true){
                    it.dismiss()
                    goTo<DeviceConnectActivity>()
                }.show()
            }
            MsgWhat.CMD_STRENGTH_FREQ->{
                val pair = msg.obj as Pair<Float, Float>
                tvQiangDu.txt("强度：${pair.first.toBigDecimal().divide(1.toBigDecimal(), 2, RoundingMode.HALF_UP)}")
                tvPinLv.txt("频率：${pair.second.toInt()}Hz")
            }
            MsgWhat.CMD_EQ->{
                dialogRefresh.dismiss()
                tvDianLiang.txt("电量：${msg.obj}%")
            }
            MsgWhat.CMD_DEVICE_NO->{
                tvSheBeiBianHao.txt("设备编号：${msg.obj}")
            }
        }
    }

    lateinit var dialogRefresh: ProgressDialog

    override fun init(bundle: Bundle?) {
        flDisconnect.showOrGone(!isConnectedDevice)
        dialogRefresh = DialogUtils.createProgressDialog(this, "正在刷新...")
        tvSheBeiBianHao.txt("设备编号：${SPHelper.getEquipNo()}")
        sendCmd()

        timer = TimerUtils.countdown(Long.MAX_VALUE, 1000, {
            tvDianQianShiJian.txt("当前时间：${System.currentTimeMillis().fmtDate("yyyy-MM-dd HH:mm:ss")}")
        })
        timer.start()

        btnRefresh.click(2) {
            sendCmd()
        }
    }

    private fun sendCmd() {
        dialogRefresh.setMessage("正在获取数据...")
        dialogRefresh.show()
        CmdUtils.sendCmdForStrengthAndFrequency()
        doDelayTask(2000){
            dialogRefresh.dismiss()
            CmdUtils.sendCmdForElectricQuantity()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

}