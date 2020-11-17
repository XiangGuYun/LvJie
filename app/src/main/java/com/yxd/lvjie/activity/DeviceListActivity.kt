package com.yxd.lvjie.activity

import android.content.Context
import android.os.Bundle
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseActivity
import com.yp.baselib.utils.fragment.FragPagerUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.fragment.DeviceListFragment
import com.yxd.lvjie.view.MsgCenterTabView
import kotlinx.android.synthetic.main.activity_device_list.*
import kotlinx.android.synthetic.main.header.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

/**
 * 设备列表
 */
@LayoutId(R.layout.activity_device_list)
class DeviceListActivity : BaseActivity() {

    override fun init(bundle: Bundle?) {
        tvTitle.text = "设备列表"

        FragPagerUtils(
            this, vpDeviceList, listOf(
                DeviceListFragment.newInstance(),
                DeviceListFragment.newInstance(),
                DeviceListFragment.newInstance(),
                DeviceListFragment.newInstance()
            )
        )

        tlDeviceList.navigator = CommonNavigator(this).apply {
            adapter = object : CommonNavigatorAdapter() {
                override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                    return MsgCenterTabView(this@DeviceListActivity).apply {
                        tv(R.id.tvCell).txt(listOf("全部", "固定", "流动", "观察")[index])
                            .click { vpDeviceList.currentItem = index }.width = srnWidth / 4
                    }
                }

                override fun getCount(): Int {
                    return 4
                }

                override fun getIndicator(context: Context?): IPagerIndicator {
                    return LinePagerIndicator(context).apply {
                        mode = LinePagerIndicator.MODE_EXACTLY
                        roundRadius = 1.dp2px().toFloat()
                        lineWidth = 30.dp2px().toFloat()
                        lineHeight = 2.dp2px().toFloat()
                        setColors("#3C79FB".color())
                    }
                }

            }
        }
        ViewPagerHelper.bind(tlDeviceList, vpDeviceList)


    }

}