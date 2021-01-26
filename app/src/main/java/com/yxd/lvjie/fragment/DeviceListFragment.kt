package com.yxd.lvjie.fragment

import android.os.Bundle
import android.os.Message
import android.util.Log
import com.yxd.baselib.Holder
import com.yxd.baselib.annotation.Bus
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.base.BaseActivity
import com.yxd.baselib.base.BaseFragment
import com.yxd.baselib.utils.BusUtils
import com.yxd.baselib.utils.OK
import com.yxd.lvjie.R
import com.yxd.lvjie.activity.DeviceDetailActivity
import com.yxd.lvjie.activity.LoginActivity
import com.yxd.lvjie.bean.DeviceListBean
import com.yxd.lvjie.bean.HistoryDataBean
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.net.Req
import kotlinx.android.synthetic.main.activity_history_data.*
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
            MsgWhat.UPDATE_DEVICE_LIST->{
//                val result = list.find { it.id == msg.arg1 }
//                if(result != null){
//                    val index = list.indexOf(result)
//                    list[index].installTime = (msg.obj as Pair<Long, String>).first
//                    list[index].dataUpdateTime = (msg.obj as Pair<String, String>).second.toLong()
//                    if(msg.arg2 != -1){
//                        list.remove(result)
//                        BusUtils.post(MsgWhat.CHANGE_DEVICE_PATTERN, result, arg1 = msg.arg2)
//                    }
//                    refreshDevice.update()
//                }
                list.clear()
                reqData(true)
            }
            MsgWhat.CHANGE_DEVICE_PATTERN -> {
//                val type =  when (arguments!!.getSerializable("type") as Type) {
//                    Type.QUAN_BU -> OK.OPTIONAL
//                    Type.GU_DING_DIAN -> "0"
//                    Type.GUAN_CHA_DIAN -> "2"
//                    else -> "1"
//                }
//                if(msg.arg1.toString() == type){
//                    list.add(msg.obj as DeviceListBean.Data.Device)
//                    refreshDevice.update()
//                }
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
    private val list = ArrayList<DeviceListBean.Data.Device>()

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
//                "加了${it.data.list.size}条数据".toast()
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

    enum class Type : Serializable {
        QUAN_BU, GU_DING_DIAN, LIU_DONG_DIAN, GUAN_CHA_DIAN
    }

}