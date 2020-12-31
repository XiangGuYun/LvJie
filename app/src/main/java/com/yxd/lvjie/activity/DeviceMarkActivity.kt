package com.yxd.lvjie.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Message
import com.yxd.baselib.annotation.Bus
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.utils.BusUtils
import com.yxd.baselib.utils.PopupUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.bean.DeviceMarkEditJson
import com.yxd.lvjie.constant.Cmd
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.dialog.ProjectDialog
import com.yxd.lvjie.helper.SPHelper
import com.yxd.lvjie.net.Req
import com.yxd.lvjie.utils.CmdUtils
import kotlinx.android.synthetic.main.activity_device_biao_ding.*
import kotlinx.android.synthetic.main.activity_realtime_data.*
import kotlinx.android.synthetic.main.header.*
import org.greenrobot.eventbus.Subscribe
import java.math.BigDecimal

/**
 * 设备标定
 */
@Bus
@LayoutId(R.layout.activity_device_biao_ding)
class DeviceMarkActivity : ProjectBaseActivity() {

    private lateinit var pu: PopupUtils

    var selectedIndex = 0

    @Subscribe
    fun handle(msg: Message) {
        when (msg.what) {
            MsgWhat.DEVICE_DISCONNECT -> {
                ProjectDialog(this).setInfo(
                    "设备已断开连接，请重新连接！", "确定",
                    true
                ) {
                    it.dismiss()
                    goTo<DeviceConnectActivity>()
                }.show()
            }
            MsgWhat.CMD_STRENGTH_FREQ -> {
                val pair = msg.obj as Pair<Float, Float>
//                tvStrength.txt("强度：${pair.first.toInt()}")
                tvFreq.txt("频率：${pair.second}Hz")
            }
            MsgWhat.CMD_STARTED_FREQ -> {
                // 标定频率
                tvMarkFreq.txt("${msg.obj.toString()}Hz")
                // 标定值
                tvMarkNumber.txt(
                    (tvMarkStrength.str.toBigDecimal().divide(
                        tvStrength.str.replace("原始强度：", "").toBigDecimal(),
                        2,
                        BigDecimal.ROUND_HALF_UP
                    )).toString()
                )
            }
            MsgWhat.CMD_STARTED_VALUE -> {
                // 标定强度
                tvMarkStrength.txt("${msg.obj}")
            }
            MsgWhat.ORIGIN_STRENGTH -> {
                // 原始强度
                tvStrength.txt("原始强度：${msg.obj}")
            }
        }
    }

    private val listMarkFreqAddress = listOf(
        528, 540, 552, 564, 576
    )

    private val listMarkValueAddress = listOf(
        532, 544, 556, 568, 580
    )

    private val listMarkTestValueAddress = listOf(
        536, 548, 560, 572, 584
    )

    override fun init(bundle: Bundle?) {

        val pdRefresh = ProgressDialog(this)
        pdRefresh.setMessage("正在刷新...")
        pdRefresh.setCanceledOnTouchOutside(false)

        tvSubTitle.show().txt("刷新").click(3) {
            pdRefresh.show()
            // 读取强度和频率
            BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.ORIGIN_STRENGTH)
            doDelayTask(1000){
                pdRefresh.dismiss()
                CmdUtils.sendCmdForStrengthAndFrequency()
            }
        }

        CmdUtils.sendCmdForStrengthAndFrequency()

        val pd = ProgressDialog(this)
        pd.setMessage("正在保存...")
        pd.setCanceledOnTouchOutside(false)

        doDelayTask(1000) {
            CmdUtils.sendCmdForMarkPoint(this, 0)
        }

        flMarkPoint.post {
            pu = PopupUtils(this, R.layout.mark_point, flMarkPoint.width to 200.dp)
            val list = (1..10).toList().map { "标定点$it" }
            pu.windowView.rv(R.id.rvMarkPoint).wrap.decorate()
                .generate(
                    list,
                    { h, i, item ->
                        h.tv(R.id.tv).txt(item)
                        h.itemClick {
                            selectedIndex = i
                            pu.window.dismiss()
                            tvMarkPoint.txt(item)
                            CmdUtils.sendCmdForMarkPoint(this, selectedIndex)
                        }
                    }, null, R.layout.item_mark_point
                )
            flMarkPoint.click {
                pu.window.showAsDropDown(flMarkPoint)
            }
        }

        val dialogAutoAdjust = ProjectDialog(this).setInfo(
            "请确认是否开始设备自校准?", "确定", true
        ) {
            CmdUtils.autoAdjust()
            it.dismiss()
        }

        val dialogSaveSetting = ProjectDialog(this).setInfo(
            "请确认是否保存标定点设置?", "确定", true
        ) {
            it.dismiss()
            pd.show()
            Req.editDeviceMark(DeviceMarkEditJson(
                equipNo = SPHelper.getEquipNo(),
                frequency = tvFreq.str.replace("Hz", "").replace("频率：",""),
                number = selectedIndex+1,
                ratio = (tvMarkStrength.str.toBigDecimal().divide(
                    tvStrength.str.replace("原始强度：", "").toBigDecimal(),
                    2,
                    BigDecimal.ROUND_HALF_UP
                )).toString(),
                strength = tvMarkStrength.str
            )
            ) {
                // 原始强度
                CmdUtils.write(
                    tvStrength.str.replace("原始强度：", "").toFloat(),
                    listMarkTestValueAddress[selectedIndex],
                    "4"
                )
                doDelayTask(1500) {
                    // 标定强度
                    CmdUtils.write(
                        tvMarkStrength.str.toFloat(),
                        listMarkValueAddress[selectedIndex],
                        "4"
                    )
                    doDelayTask(1500) {
                        CmdUtils.write(
                            // 标定频率
                            tvFreq.str.replace("Hz", "").replace("频率：","").toFloat(),
                            listMarkFreqAddress[selectedIndex],
                            "4"
                        )
                        pd.dismiss()
                        tvMarkNumber.txt(
                            (tvMarkStrength.str.toBigDecimal().divide(
                                tvStrength.str.replace("原始强度：", "").toBigDecimal(),
                                2,
                                BigDecimal.ROUND_HALF_UP
                            )).toString())
                    }
                }
            }
        }

        tvAutoAdjust.click {
            dialogAutoAdjust.show()
        }

        tvSaveSetting.click {
            dialogSaveSetting.show()
        }

    }

    private fun refresh() {

    }


}