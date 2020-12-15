package com.yxd.baselib.view.header

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshKernel
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.yxd.baselib.R

/**
 * 文本型Header
 *
 * RefreshLayout设置头部或尾部的方法
 *
 * 1.在Application中做全局设置
 *
 *   SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
 *      return CustomRefreshHeader(context)
 *   }
 *
 *   SmartRefreshLayout.setDefaultRefreshFooterCreator() { context, layout ->
 *      return CustomRefreshFooter(context)
 *   }
 *
 * 2.针对单独的RefreshLayout进行设置
 *
 *   refresh.setRefreshHeader(TextHeader(context))
 *
 */
class TextHeader @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr), RefreshHeader {


    var refreshView = LayoutInflater.from(context).inflate(R.layout.header_text, null) as TextView

    init {
        addView(refreshView)
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {

        return 0
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
    }

    override fun onPulling(percent: Float, offset: Int, height: Int, extendHeight: Int) {
    }

    override fun onReleasing(percent: Float, offset: Int, height: Int, extendHeight: Int) {
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun getView(): View {
        return this
    }

    override fun setPrimaryColors(vararg colors: Int) {
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        when (newState) {
            RefreshState.PullDownToRefresh -> {
                //下拉刷新开始。正在下拉还没松手时调用
                refreshView.text = "↓下拉刷新"
            }
            RefreshState.Refreshing -> {
                //状态切换为正在刷新状态时，设置图片资源为小人卖萌的动画并开始执行
                refreshView.text = "正在刷新..."
            }
            RefreshState.ReleaseToRefresh -> {
                refreshView.text = "↑释放刷新"
            }
        }

    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }
}