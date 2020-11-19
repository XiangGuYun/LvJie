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
            when (javaClass.simpleName) {
                "DeviceListActivity" -> it.txt("设备列表")
                "RealtimeDataActivity"-> it.txt("实时数据")
                else->{}
            }
        }

    }

}