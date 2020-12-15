package com.yxd.lvjie.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Message
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.yxd.baselib.annotation.Bus
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.utils.BusUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.bean.HistoryDataBean
import com.yxd.lvjie.constant.Cmd
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.constant.MsgWhat.CMD_DEVICE_NO
import com.yxd.lvjie.helper.SPHelper
import com.yxd.lvjie.net.Req
import com.yxd.lvjie.net.URL
import com.yxd.lvjie.utils.CmdUtils
import kotlinx.android.synthetic.main.activity_history_data.*
import kotlinx.android.synthetic.main.header.*
import org.greenrobot.eventbus.Subscribe

/**
 * 历史数据
 */
@Bus
@LayoutId(R.layout.activity_history_data)
class HistoryDataActivity : ProjectBaseActivity() {

    @Subscribe
    fun handle(msg: Message){
        when(msg.what){
            CMD_DEVICE_NO -> {
                number = msg.obj.toString()
                reqData()
            }
        }
    }

    override fun init(bundle: Bundle?) {
        initView()
    }

    private fun initView() {
        tvSubTitle.show().txt("同步").click {
            BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.GET_HISTORY_DATA)
        }

        tvStartTime.txt("")

        tvEndTime.txt("")

        CmdUtils.sendCmdForDeviceNumber()

        // 查看曲线图
        /*
        domain/h5/index.html#/show?token=1212&total=12&startTime=时间戳&endTime=时间戳&equipNo=equipNo
        total 为你当前页面显示数据的长度
        时间戳没有传空字符串
        equipNo为设备编号
        token为你app登录的token
         */
        tvChaKanQuXianTu.click {
            val url = "http://47.96.4.50/h5/index.html#/show?token=${SPHelper.getToken()}${if(startDate != 0L)"&startTime=${startDate}" else ""}${if(endDate != 0L)"&endTime=${endDate}" else ""}&equipNo=$number"
            url.logD("YXD_URL")
            LvJieHtmlActivity.start(this, url)
        }

        val pvTime1 = TimePickerBuilder(this)
        { date, v ->
            if(date.time > endDate && endDate != 0L){
                "开始时间不能小于结束时间".toast()
                return@TimePickerBuilder
            }
            startDate = date.time
            reqData(isRefresh = true)
            tvStartTime.txt(date.time.fmtDate())
        }.setType(booleanArrayOf(true, true, true, true, true, true))
            .setTitleBgColor(Color.WHITE)
            .build()

        val pvTime2 = TimePickerBuilder(this)
        { date, v ->
            if(date.time < startDate && startDate != 0L){
                "结束时间不能小于开始时间".toast()
                return@TimePickerBuilder
            }
            endDate = date.time
            reqData(isRefresh = true)
            tvEndTime.txt(date.time.fmtDate())
        }.setType(booleanArrayOf(true, true, true, true, true, true))
            .setTitleBgColor(Color.WHITE)
            .build()

        tvStartTime.click {
            pvTime1.show()
        }

        tvEndTime.click {
            pvTime2.show()
        }
    }

    private var number: String = ""
    private var startDate: Long = 0
    private var endDate: Long = 0
    private var currentPage = 1
    private var totalPage = 1
    private val listHistory = ArrayList<HistoryDataBean.Data.History>()

    private fun reqData(
        isRefresh: Boolean = false,
        isLoadMore: Boolean = false,
        callback: (() -> Unit)? = null
    ) {
        if (isRefresh) currentPage = 1
        if (isLoadMore) currentPage++
        Req.getHistoryData(
            equipNo = number,
            pageNum = currentPage,
            startTime = startDate.toString(),
            endTime = endDate.toString()) {
            callback?.invoke()
            totalPage = it.data?.total?.div(20)!!
            when {
                isRefresh -> {
                    listHistory.clear()
                    listHistory.addAll(it.data.list ?: listOf())
                    refreshHistory.update()
                }
                isLoadMore -> {
                    listHistory.addAll(it.data.list ?: listOf())
                    refreshHistory.update()
                }
                else -> {
                    listHistory.addAll(it.data.list ?: listOf())
                    refreshHistory.set({
                        it.generate(
                            listHistory,
                            { h, p, item ->
                                h.tv(R.id.tv1).txt(item.time?.fmtDate())
                                h.tv(R.id.tv2).txt("${item.frequency}Hz")
                                h.tv(R.id.tv3).txt("${item.strength}")
                            },
                            {
                                if (it % 2 == 0) 1 else 0
                            }, R.layout.item_history_data, R.layout.item_history_data1
                        )
                    }, onRefresh = {
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
                            }
                        }
                    })
                }
            }

        }
    }

}