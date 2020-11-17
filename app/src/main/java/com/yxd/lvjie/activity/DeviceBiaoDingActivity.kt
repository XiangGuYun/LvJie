package com.yxd.lvjie.activity

import android.os.Bundle
import com.yp.baselib.annotation.LayoutId
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import kotlinx.android.synthetic.main.header.*

/**
 * 设备标定
 */
@LayoutId(R.layout.activity_device_biao_ding)
class DeviceBiaoDingActivity : ProjectBaseActivity() {

    override fun init(bundle: Bundle?) {
        tvTitle.txt("设备标定")
    }

}