package com.yxd.lvjie.base

import android.view.View
import com.kotlinlib.common.ResUtils
import com.yp.baselib.base.BaseActivity

abstract class ProjectBaseActivity : BaseActivity()  {

    override fun beforeInit() {
        findViewById<View>(ResUtils.getId(this, "ivBack"))?.click {
            finish()
        }
    }

}