package com.yxd.lvjie.activity

import android.os.Bundle
import android.os.Message
import com.yp.baselib.annotation.Bus
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.utils.PopupUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.dialog.ProjectDialog
import com.yxd.lvjie.utils.CmdUtils
import kotlinx.android.synthetic.main.activity_device_biao_ding.*
import org.greenrobot.eventbus.Subscribe

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
                    goTo<DeviceConnectActivity>(true)
                }.show()
            }
            MsgWhat.CMD_STRENGTH_FREQ -> {
                val pair = msg.obj as Pair<Float, Float>
                tvStrength.txt("强度：${pair.first.toInt()}")
                tvFreq.txt("频率：${pair.second.toInt()}Hz")
            }
            MsgWhat.CMD_STARTED_FREQ -> {

            }
            MsgWhat.CMD_STARTED_VALUE -> {

            }
            MsgWhat.CMD_STARTED_TEST_VALUE -> {

            }
        }
    }

    override fun init(bundle: Bundle?) {

        CmdUtils.getStrengthAndFrequency()

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

        }

        val dialogSaveSetting = ProjectDialog(this).setInfo(
            "请确认是否保存标定点设置?", "确定", true
        ) {

        }

        tvAutoAdjust.click {
            dialogAutoAdjust.show()
        }

        tvSaveSetting.click {
            dialogSaveSetting.show()
        }

    }

}