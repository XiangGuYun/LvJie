package com.yxd.lvjie.activity

import android.os.Bundle
import android.os.Message
import com.yxd.baselib.annotation.Bus
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.utils.BusUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.constant.Cmd
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.constant.MsgWhat.CMD_DEVICE_INFO_1
import com.yxd.lvjie.constant.MsgWhat.CMD_DEVICE_INFO_2
import com.yxd.lvjie.constant.MsgWhat.CMD_DEVICE_INFO_3
import com.yxd.lvjie.constant.MsgWhat.CMD_DEVICE_INFO_4
import com.yxd.lvjie.constant.MsgWhat.CMD_DEVICE_INFO_5
import com.yxd.lvjie.constant.MsgWhat.CMD_DEVICE_INFO_6
import kotlinx.android.synthetic.main.activity_device_info.*
import org.greenrobot.eventbus.Subscribe

/**
 * 设备信息
 */
@Bus
@LayoutId(R.layout.activity_device_info)
class DeviceInfoActivity : ProjectBaseActivity() {

    @Subscribe
    fun handle(msg: Message) {
        when (msg.what) {
            CMD_DEVICE_INFO_1 -> tvZhuKongBanYingJianXinXi.txt(msg.obj)
            CMD_DEVICE_INFO_2 -> tvZhuKongBanRuanJianBanBen.txt(msg.obj)
            CMD_DEVICE_INFO_3 -> tvSheBeiBianHao.txt(msg.obj)
            CMD_DEVICE_INFO_4 -> tvIMEI.txt(msg.obj)
            CMD_DEVICE_INFO_5 -> tvMuBiaoIpDiZhi.txt(msg.obj)
            CMD_DEVICE_INFO_6 -> tvWuXianDianDaiMa.txt(msg.obj.toString().replace(" ",""))
        }
    }

    override fun init(bundle: Bundle?) {
        BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.DEVICE_INFO)
    }

}