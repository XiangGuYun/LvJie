package com.yxd.lvjie.activity

import android.graphics.Color
import android.os.Bundle
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseActivity
import com.yxd.lvjie.R
import kotlinx.android.synthetic.main.activity_time_set.*
import kotlinx.android.synthetic.main.header.*


@LayoutId(R.layout.activity_time_set)
class TimeSetActivity : BaseActivity() {

    override fun init(bundle: Bundle?) {
        tvTitle.text = "时间设置"

        tvCurrentTime.txt(System.currentTimeMillis().fmtDate("yyyy-MM-dd HH:mm:ss"))

        val pvTime = TimePickerBuilder(
            this
        ) { date, v ->
            tvAwakeTimeSet.txt(date.time.fmtDate("HH:mm:ss"))
        }.setType(booleanArrayOf(false, false, false, true, true, true))
            .setTitleBgColor(Color.WHITE)
            .build()

        tvAwakeTimeSet.click {
            pvTime.show()
        }
    }

}