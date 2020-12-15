package com.yxd.baselib.ex

interface BaseEx : ContextEx, EditTextEx, FileEx, RvEx, StringEx, TextViewEx,
    ViewEx, NumberEx, ActivityEx, ViewPager2Ex {

    /**
     * 裁切列表（同时修改自身）
     *
     * 对于ByteArray的裁剪，要使用sliceArray
     * @receiver R
     * @param range IntRange 区间范围
     */
    fun <T, R : MutableList<T>> R.sliceSelf(range: IntRange): R {
        val tempoList = this.slice(range)
        this.clear()
        this.addAll(tempoList)
        return this
    }

}