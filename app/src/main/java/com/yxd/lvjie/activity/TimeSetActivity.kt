package com.yxd.lvjie.activity

import android.graphics.Color
import android.os.Bundle
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.utils.BusUtils
import com.yxd.baselib.utils.TimerUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.utils.CmdUtils
import kotlinx.android.synthetic.main.activity_time_set.*
import java.util.*

/**
 * 时间设置
 */
@LayoutId(R.layout.activity_time_set)
class TimeSetActivity : ProjectBaseActivity() {

    private lateinit var timer: Timer

    override fun init(bundle: Bundle?) {

        timer = TimerUtils.schedule(0, 1000) {
            runOnUiThread {
                tvCurrentTime.txt(System.currentTimeMillis().fmtDate())
            }
        }

        val pvTime = TimePickerBuilder(this)
        { date, v ->
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
            pvTime.show()
        }

        tvAwakeTimeSet.txt(extraStr("time"))
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

}