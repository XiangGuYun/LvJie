package com.yxd.lvjie.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.base.BaseWebActivity
import com.yxd.lvjie.R
import kotlinx.android.synthetic.main.activity_html.*
import kotlinx.android.synthetic.main.header.*

/**
 * 网页
 */
@LayoutId(R.layout.activity_html)
class LvJieHtmlActivity : BaseWebActivity() {

    override fun beforeInitWebView() {

        ivBack.click {
            finish()
        }

        setOnGetTitle {
            tvTitle.txt("历史数据曲线")
        }

        setWebViewLP {
            it.topMargin = 44.dp
        }
    }

    override fun getParentView(): FrameLayout {
        return flParent
    }

    companion object {
        private const val WEB_URL = "webUrl"
        fun start(ctx: Context, webUrl: String) {
            ctx.startActivity(Intent(ctx, LvJieHtmlActivity::class.java).putExtra(WEB_URL, webUrl))
        }
    }

}




