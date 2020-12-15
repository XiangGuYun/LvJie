package com.yxd.lvjie.activity

import android.os.Bundle
import com.yxd.baselib.annotation.LayoutId
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import kotlinx.android.synthetic.main.activity_arithmetic_select.*

/**
 * 算法选择
 */
@LayoutId(R.layout.activity_arithmetic_select)
class ArithmeticSelectActivity : ProjectBaseActivity() {

    override fun init(bundle: Bundle?) {
        val list = listOf("算法1", "算法2", "算法3")

        var currentIndex = 0

        rvArithmetic.wrap.generate(list, { h, p, item ->
            h.tv(R.id.tvName).txt(item)
            h.iv(R.id.ivGou).showOrGone(currentIndex == p)
            h.itemClick {
                currentIndex = p
                rvArithmetic.update()
            }
        }, null, R.layout.item_arithmetic)
    }

}