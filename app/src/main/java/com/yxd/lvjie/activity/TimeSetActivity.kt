package com.yxd.lvjie.activity

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Message
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.yxd.baselib.annotation.Bus
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.utils.BusUtils
import com.yxd.baselib.utils.DialogUtils
import com.yxd.baselib.utils.TimerUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.utils.CmdUtils
import kotlinx.android.synthetic.main.activity_time_set.*
import org.greenrobot.eventbus.Subscribe
import java.util.*

/**
 * 时间设置
 */
@Bus
@LayoutId(R.layout.activity_time_set)
class TimeSetActivity : ProjectBaseActivity() {

    private lateinit var pd: ProgressDialog
    private lateinit var timer: Timer

    @Subscribe
    fun handle(msg:Message){
        if(msg.what == MsgWhat.WRITE_TIME_DONE){
            pd.dismiss()
        }
    }

    override fun init(bundle: Bundle?) {

        timer = TimerUtils.schedule(0, 1000) {
            runOnUiThread {
                tvCurrentTime.txt(System.currentTimeMillis().fmtDate())
            }
        }
        pd = DialogUtils.createProgressDialog(this, "正在保存...")
        val pvTime = TimePickerBuilder(this)
        { date, v ->
            pd.show()
            val calendar = Calendar.getInstance()
            calendar.time = date
            val hour =
                if (calendar.get(Calendar.HOUR_OF_DAY) < 10) "0${calendar.get(Calendar.HOUR_OF_DAY)}" else calendar.get(
                    Calendar.HOUR_OF_DAY
                ).toString()
            val minute =
                if (calendar.get(Calendar.MINUTE) < 10) "0${calendar.get(Calendar.MINUTE)}" else calendar.get(
                    Calendar.MINUTE
                ).toString()
            val second =
                if (calendar.get(Calendar.SECOND) < 10) "0${calendar.get(Calendar.SECOND)}" else calendar.get(
                    Calendar.SECOND
                ).toString()
            CmdUtils.writeTime(hour.toInt(), minute.toInt(), second.toInt())
            tvAwakeTimeSet.txt("$hour:$minute:$second")
            BusUtils.post(MsgWhat.CMD_DEVICE_AWAKE_TIME, "$hour:$minute:$second")
        }.setType(booleanArrayOf(false, false, false, true, true, true))
            .setTitleBgColor(Color.WHITE)
            .build()

        tvAwakeTimeSet.click {
            pvTime.setDate(Calendar.getInstance().apply {
                set(this.get(Calendar.YEAR), this.get(Calendar.MONDAY), this.get(Calendar.DAY_OF_MONTH),
                    extraStr("time").split(":")[0].toInt(),
                    extraStr("time").split(":")[1].toInt(),
                    extraStr("time").split(":")[2].toInt())
            })
            pvTime.show()
        }

        tvAwakeTimeSet.txt(extraStr("time"))
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

}