package com.yxd.lvjie.fragment

import android.content.ClipData
import com.yp.baselib.Holder
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseFragment
import com.yxd.lvjie.R
import kotlinx.android.synthetic.main.fragment_device_list.*

@LayoutId(R.layout.fragment_device_list)
class DeviceListFragment : BaseFragment() {

    private fun Holder.setGuDingDian() {
        iv(R.id.ivPoint).sIR(R.mipmap.gudingdian)
        tv(R.id.tvPoint).txt("固定点").color("#FA6164".color)
    }

    private fun Holder.setLiuDongDian() {
        iv(R.id.ivPoint).sIR(R.mipmap.liudongdian)
        tv(R.id.tvPoint).txt("流动点").color("#3C79FB".color)
    }

    private fun Holder.setGuanChaDian() {
        iv(R.id.ivPoint).sIR(R.mipmap.guanchadian)
        tv(R.id.tvPoint).txt("观察点").color("#FEA900".color)
    }

//    @Rv(R.layout.item_device)
//    fun rvDevice(h:Holder, i:Int, item:ClipData.Item): () -> Int {
//        h.tv(R.id.tv).txt(item.htmlText)
//        return { 0 }
//    }

    override fun init() {
        rvDevice.wrap.generate(listOf(1, 1, 1),
            { h, i, it ->
                when (i) {
                    0 -> {
                        h.setGuDingDian()
                    }
                    1 -> {
                        h.setLiuDongDian()
                    }
                    2 -> {
                        h.setGuanChaDian()
                    }
                }
                h.tv(R.id.tvDeviceNo).txt("设备编号：")
                h.tv(R.id.tvDeviceLocation).txt("设备位置：")
                h.tv(R.id.tvInstallDate).txt("安装时间：")
                h.tv(R.id.tvDataUpdateDate).txt("数据更新时间：")
            }, null, R.layout.item_device_list
        )
    }

    companion object {
        fun newInstance(): DeviceListFragment {
            return DeviceListFragment()
        }
    }

}