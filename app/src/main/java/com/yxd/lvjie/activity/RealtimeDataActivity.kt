package com.yxd.lvjie.activity

import android.os.Bundle
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseActivity
import com.yxd.lvjie.R
import kotlinx.android.synthetic.main.activity_realtime_data.*
import kotlinx.android.synthetic.main.header.*

/**
 * 实时数据
 */
@LayoutId(R.layout.activity_realtime_data)
class RealtimeDataActivity : BaseActivity() {

    override fun init(bundle: Bundle?) {
        tvTitle.txt("实时数据")

        tvDianQianShiJian.txt("当前时间：")

        tvQiangDu.txt("强度：")

        tvPinLv.txt("频率：")

        btnRefresh.click {

        }
    }

}