package com.yxd.lvjie.base

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.kotlinlib.common.ResUtils
import com.yxd.baselib.base.BaseActivity
import com.yxd.baselib.utils.DialogUtils

abstract class ProjectBaseActivity : BaseActivity() {

    override fun beforeInit(savedInstanceState: Bundle?) {
        vNull(ResUtils.getId("ivBack"))?.click {
            finish()
        }

        tvNull(ResUtils.getId("tvTitle"))?.txt(
            when (javaClass.simpleName) {
                "DeviceListActivity" -> "设备列表"
                "RealtimeDataActivity" -> "实时数据"
                "DeviceMarkActivity" -> "设备标定"
                "HistoryDataActivity" -> "历史数据"
                "DeviceInfoActivity" -> "设备信息"
                "DeviceDetailActivity" -> "设备详情"
                "ArithmeticSelectActivity" -> "算法选择"
                "AdvancedSettingActivity" -> "进入高级设置"
                "TimeSetActivity" -> "时间设置"
                "TableActivity" -> "主控制板寄存器表"
                "SettingActivity" -> "设备设置"
                else -> ""
            }
        )

        val dialog = DialogUtils.createProgressDialog(this, "正在请求...")

        showLoadingCallback = {
            dialog.show()
        }

        hideLoadingCallback = {
            dialog.dismiss()
        }

        onHttpErrorCallback = {
            dialog.dismiss()
        }

    }

}