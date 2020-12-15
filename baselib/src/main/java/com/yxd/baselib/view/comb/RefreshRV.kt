package com.yxd.baselib.view.comb

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.yxd.baselib.ex.BaseEx
import com.yxd.baselib.utils.RVUtils

class RefreshRV @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    SmartRefreshLayout(context, attrs, defStyleAttr), BaseEx {

    private val rv = RecyclerView(context)

    init {
        rv.lp(SmartRefreshLayout.LayoutParams(MP, MP))
        addView(rv)
    }

    fun set(
        getWrap: (RVUtils) -> Unit,
        isRefreshEnable: Boolean = true,
        isLoadMoreEnable: Boolean = true,
        header: RefreshHeader = ClassicsHeader(context),
        footer: RefreshFooter = ClassicsFooter(context),
        onRefresh: ((RefreshLayout) -> Unit)? = null,
        onLoadMore: ((RefreshLayout) -> Unit)? = null
    ) {
        getWrap.invoke(rv.wrap)

        isEnableRefresh = isRefreshEnable

        isEnableLoadMore = isLoadMoreEnable

        setRefreshHeader(header)

        setRefreshFooter(footer)

        setOnRefreshListener {
            onRefresh?.invoke(it)
        }

        setOnLoadMoreListener {
            onLoadMore?.invoke(it)
        }
    }

    fun update(){
        rv.update()
    }


}