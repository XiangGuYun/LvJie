package com.yxd.lvjie.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Message
import com.yp.baselib.annotation.Bus
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.utils.TimerUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.dialog.ProjectDialog
import com.yxd.lvjie.utils.CmdUtils
import kotlinx.android.synthetic.main.activity_realtime_data.*
import kotlinx.android.synthetic.main.item_device_list.*
import org.greenrobot.eventbus.Subscribe

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
                tvQiangDu.txt("强度：${pair.first.toInt()}")
                tvPinLv.txt("频率：${pair.second.toInt()}Hz")
            }
            MsgWhat.CMD_EQ->{
                tvDianLiang.txt("电量：${msg.obj}%")
            }
            MsgWhat.CMD_IMEI->{
                tvSheBeiBianHao.txt("设备编号：${msg.obj}")
            }
        }
    }

    override fun init(bundle: Bundle?) {
        sendCmd()

        timer = TimerUtils.countdown(Long.MAX_VALUE, 1000, {
            tvDianQianShiJian.txt("当前时间：${System.currentTimeMillis().fmtDate("yyyy-MM-dd HH:mm:ss")}")
        })
        timer.start()

        btnRefresh.click {
            sendCmd()
        }
    }

    private fun sendCmd() {
        CmdUtils.getStrengthAndFrequency()
        doDelayTask(300){
            CmdUtils.getElectricQuantity()
            doDelayTask(300){
                CmdUtils.getDeviceIMEI()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

}