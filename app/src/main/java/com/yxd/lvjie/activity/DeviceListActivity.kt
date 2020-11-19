package com.yxd.lvjie.activity

import android.os.Bundle
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.utils.fragment.FragPagerUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.fragment.DeviceListFragment
import com.yxd.lvjie.view.MsgCenterTabView
import kotlinx.android.synthetic.main.activity_device_list.*

/**
 * 设备列表
 */
@LayoutId(R.layout.activity_device_list)
class DeviceListActivity : ProjectBaseActivity() {

    override fun init(bundle: Bundle?) {
        val tabList = listOf("全部", "固定", "流动", "观察")

        val fragList = (1..tabList.size).map {
            DeviceListFragment.newInstance()
        }

        FragPagerUtils(
            this, vpDeviceList, fragList
        ).bindMI(
            tlDeviceList,
            tabList.size,
            "#3C79FB".color(),
            30,
            2,
            1
        ) {
            MsgCenterTabView(this).apply {
                tv(R.id.tvCell).txt(tabList[it])
            }
        }

    }

}