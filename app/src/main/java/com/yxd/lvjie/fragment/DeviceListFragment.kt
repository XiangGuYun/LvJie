package com.yxd.lvjie.fragment

import android.os.Bundle
import android.os.Message
import com.yxd.baselib.Holder
import com.yxd.baselib.annotation.Bus
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.base.BaseFragment
import com.yxd.baselib.utils.OK
import com.yxd.lvjie.R
import com.yxd.lvjie.activity.DeviceDetailActivity
import com.yxd.lvjie.bean.DeviceListBean
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.net.Req
import kotlinx.android.synthetic.main.fragment_device_list.*
import org.greenrobot.eventbus.Subscribe
import java.io.Serializable

/**
 * 设备列表
 * @author
 */
@Bus
@LayoutId(R.layout.fragment_device_list)
class DeviceListFragment : BaseFragment() {

    @Subscribe
    fun handle(msg:Message){
        when(msg.what){
            // 更新设备列表
            MsgWhat.UPDATE_DEVICE_LIST -> {
                list.clear()
                reqData(true)
            }
        }
    }

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

    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        reqData()
    }

    private var currentPage = 1
    private var totalPage = 1

    /**
     * 设备列表
     */
    private val list = ArrayList<DeviceListBean.Data.Device>()

    /**
     * 请求设备列表数据
     * @param isRefresh Boolean
     * @param isLoadMore Boolean
     * @param callback Function0<Unit>?
     */
    private fun reqData(
        isRefresh: Boolean = false,
        isLoadMore: Boolean = false,
        callback: (() -> Unit)? = null
    ) {
        if (isRefresh) currentPage = 1
        if (isLoadMore) currentPage++
        Req.getDeviceList(
            currentPage,
            when (arguments!!.getSerializable("type") as Type) {
                Type.QUAN_BU -> OK.OPTIONAL
                Type.GU_DING_DIAN -> "0"
                Type.GUAN_CHA_DIAN -> "2"
                else -> "1"
            }
        ) {
            callback?.invoke()
            if(it.data?.list != null){
                totalPage = it.data.total!!.div(10)+1
                list.addAll(it.data.list)
                refreshDevice.set({ wrap ->
                    wrap.generate(
                        list,
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
                                goTo<DeviceDetailActivity>("id" to item.id.toString(), "mode" to (item.installPattern?:0))
                            }
                        }, null, R.layout.item_device_list
                    )
                }, onRefresh = {
                    list.clear()
                    reqData(isRefresh = true) {
                        it.finishRefresh()
                    }
                }, onLoadMore = {
                    if (currentPage + 1 > totalPage) {
                        "没有更多了".toast()
                        it.finishLoadMore()
                    } else {
                        reqData(isLoadMore = true) {
                            it.finishLoadMore()
                            refreshDevice.rv.layoutManager?.scrollToPosition(list.lastIndex)
                        }
                    }
                })
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

    /**
     * 设备类型
     */
    enum class Type : Serializable {
        QUAN_BU,
        GU_DING_DIAN,
        LIU_DONG_DIAN,
        GUAN_CHA_DIAN
    }

}