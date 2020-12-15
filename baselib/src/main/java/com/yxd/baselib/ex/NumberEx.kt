package com.yxd.baselib.ex

import com.yxd.baselib.utils.DensityUtils
import java.math.BigDecimal

interface NumberEx {

    val Number.dp get() = DensityUtils.dp2px(this.toFloat())

    val Number.sp get() = DensityUtils.sp2px(this.toFloat())

    fun Number.px2dp(): Int = DensityUtils.px2dip(this.toFloat())

    fun Number.px2sp(): Int = DensityUtils.px2sp(this.toFloat())

}