package com.yxd.lvjie.fragment

import android.os.Bundle
import android.util.Log
import com.yp.baselib.Holder
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseFragment
import com.yp.baselib.utils.OK
import com.yxd.lvjie.R
import com.yxd.lvjie.activity.DeviceDetailActivity
import com.yxd.lvjie.net.Req
import kotlinx.android.synthetic.main.fragment_device_list.*
import java.io.Serializable

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

    override fun init() {

        val type = arguments!!.getSerializable("type") as Type

        Req.getDeviceList(
            when (type) {
                Type.QUAN_BU -> OK.OPTIONAL
                Type.GU_DING_DIAN -> "0"
                Type.GUAN_CHA_DIAN -> "1"
                else -> "2"
            }
        ) {
            if(it.data?.list != null){
                rvDevice.wrap.generate(
                    it.data.list,
                    { h, i, item ->
                        when (item.installPattern) {
                            0 -> h.setGuDingDian()
                            1 -> h.setLiuDongDian()
                            2 -> h.setGuanChaDian()
                        }
                        h.tv(R.id.tvDeviceNo).txt("设备编号：${item.equipNo}")
                        h.tv(R.id.tvDeviceLocation).txt("设备位置：${item.longitude}, ${item.latitude}")
                        h.tv(R.id.tvInstallDate).txt("安装时间：${item.installTime?.fmtDate("yyyy-MM-dd")}")
                        h.tv(R.id.tvDataUpdateDate).txt("数据更新时间：${item.dataUpdateTime?.fmtDate("yyyy-MM-dd HH:mm:ss")}")
                        h.itemClick {
                            goTo<DeviceDetailActivity>("id" to item.id.toString())
                        }
                    }, null, R.layout.item_device_list
                )
            }
        }

    }

    companion object {
        fun newInstance(type: Type): DeviceListFragment {
            return DeviceListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("type", type)
                }
            }
        }
    }

    enum class Type : Serializable {
        QUAN_BU, GU_DING_DIAN, LIU_DONG_DIAN, GUAN_CHA_DIAN
    }

}