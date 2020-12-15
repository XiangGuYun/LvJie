package com.yxd.lvjie.activity

import android.os.Bundle
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.utils.fragment.FragPagerUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.fragment.DeviceListFragment
import com.yxd.lvjie.view.TabView
import kotlinx.android.synthetic.main.activity_device_list.*

/**
 * 设备列表
 */
@LayoutId(R.layout.activity_device_list)
class DeviceListActivity : ProjectBaseActivity() {

    override fun init(bundle: Bundle?) {
        val tabList = listOf("全部", "固定", "流动", "观察")

        val fragList = (1..tabList.size).map {
            when(it){
                1->DeviceListFragment.newInstance(DeviceListFragment.Type.QUAN_BU)
                2->DeviceListFragment.newInstance(DeviceListFragment.Type.GU_DING_DIAN)
                3->DeviceListFragment.newInstance(DeviceListFragment.Type.LIU_DONG_DIAN)
                else->DeviceListFragment.newInstance(DeviceListFragment.Type.GUAN_CHA_DIAN)
            }
        }

        FragPagerUtils(
            this, vpDeviceList, fragList
        ).bindIndicator(
                indicator,
            tabList.size,
            "#3C79FB".color(),
            30,
            2,
            1
        ) {
            TabView(this).apply {
                tv(R.id.tvCell).txt(tabList[it])
            }
        }

    }

}