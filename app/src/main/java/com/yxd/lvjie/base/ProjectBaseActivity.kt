package com.yxd.lvjie.base

import android.view.View
import android.widget.TextView
import com.kotlinlib.common.ResUtils
import com.yp.baselib.base.BaseActivity

abstract class ProjectBaseActivity : BaseActivity() {

    override fun beforeInit() {
        findViewById<View>(ResUtils.getId(this, "ivBack"))?.click {
            finish()
        }

        findViewById<TextView>(ResUtils.getId(this, "tvTitle"))?.let {
            it.txt(
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
                    else -> ""
                }
            )
        }

        showLoadingCallback = {
            "正在请求".toast()
        }

        hideLoadingCallback = {
            "请求结束了".toast()
        }

        onHttpErrorCallback = {
            "请求失败: $it".toast()
        }

    }

}