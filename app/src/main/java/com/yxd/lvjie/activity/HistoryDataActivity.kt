package com.yxd.lvjie.activity

import android.os.Bundle
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseActivity
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import kotlinx.android.synthetic.main.activity_history_data.*
import kotlinx.android.synthetic.main.header.*

/**
 * 历史数据
 */
@LayoutId(R.layout.activity_history_data)
class HistoryDataActivity : ProjectBaseActivity() {

    override fun init(bundle: Bundle?) {
        tvSubTitle.show().txt("同步").click {

        }

        tvStartTime.txt("")

        tvEndTime.txt("")

        tvChaKanQuXianTu.click {

        }

        refreshHistoryData.apply {
            isEnableRefresh = false
        }

        rvHistoryData.wrap.generate(
            (1..10).toList(),
            { h, p, item ->
                h.tv(R.id.tv1).txt("2020-10-21 18:55:00")
                h.tv(R.id.tv2).txt("1600Hz")
                h.tv(R.id.tv3).txt("18")
            },
            {
                if (it % 2 == 0) 1 else 0
            }, R.layout.item_history_data, R.layout.item_history_data1
        )

    }

}