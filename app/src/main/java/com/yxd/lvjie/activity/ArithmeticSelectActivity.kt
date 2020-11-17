package com.yxd.lvjie.activity

import android.os.Bundle
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseActivity
import com.yxd.lvjie.R
import kotlinx.android.synthetic.main.activity_arithmetic_select.*
import kotlinx.android.synthetic.main.header.*

/**
 * 算法选择
 */
@LayoutId(R.layout.activity_arithmetic_select)
class ArithmeticSelectActivity : BaseActivity() {

    override fun init(bundle: Bundle?) {
        tvTitle.text = "时间设置"

        val list = listOf("算法1", "算法2", "算法3")

        var currentIndex = 0

        rvArithmetic.wrap.rvAdapter(list, {
            h,p->
            h.tv(R.id.tvName).txt(list[p])
            h.iv(R.id.ivGou).showOrGone(currentIndex == p)
            h.itemClick {
                currentIndex = p
                rvArithmetic.update()
            }
        },R.layout.item_arithmetic)
    }

}