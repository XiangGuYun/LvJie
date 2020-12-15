package com.yxd.baselib.view.comb

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.ViewDragHelper

/**
 * 方便实现子View的拖拽与滑动
 * 
 * @author YeXuDong
 */
class EasyDragLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        ConstraintLayout(context, attrs, defStyleAttr) {

    private var onViewReleased: ((releasedChild: View, xvel: Float, yvel: Float) -> Unit)? = null
    private var horizontalDraggedArea: ((child: View, left: Int, dx: Int) -> Int)? = null
    private var verticalDraggedArea: ((child: View, top: Int, dy: Int) -> Int)? = null
    private var dragHelper: ViewDragHelper? = null
    private var listDragged: List<View>? = null

    /**
     * 设置哪些View可以被拖拽
     * @param listDragged List<View>
     */
    fun setViewsCanDragged(vararg listDragged: View): EasyDragLayout {
        this.listDragged = listDragged.toList()
        return this
    }

    /**
     * 设置横向拖拽的范围
     * @param callback 
     * @return EasyDragView
     */
    fun setHorizontalDraggedArea(callback: (child: View, left: Int, dx: Int) -> Int): EasyDragLayout {
        this.horizontalDraggedArea = callback
        return this
    }

    /**
     * 设置纵向拖拽的范围
     * @param callback 
     * @return EasyDragView
     */
    fun setVerticalDraggedArea(callback: (child: View, top: Int, dy: Int) -> Int): EasyDragLayout {
        this.verticalDraggedArea = callback
        return this
    }

    /**
     * 设置当View释放后的事件
     * @param callback 
     */
    fun setOnViewReleased(callback: (releasedChild: View, xvel: Float, yvel: Float) -> Unit): EasyDragLayout {
        this.onViewReleased = callback
        return this
    }

    /**
     * 滑动子View
     * @param view View
     * @param x Int
     * @param y Int
     */
    fun slideView(view: View, x: Int, y: Int) {
        dragHelper?.smoothSlideViewTo(view, x, y)
        invalidate()
    }

    init {
        dragHelper = ViewDragHelper.create(this, object : ViewDragHelper.Callback() {
            // 指定捕捉那些子View可以被拖拽
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                return if (listDragged != null) listDragged!!.contains(child) else false
            }

            // 限制子View的横向滑动范围，dx参数描述了横向拖拽的速度。
            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                return if (horizontalDraggedArea != null) horizontalDraggedArea!!.invoke(child, left, dx) else left
            }

            // 限制子View的纵向滑动范围，dy参数描述了纵向拖拽的速度。
            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                return if (verticalDraggedArea != null) verticalDraggedArea!!.invoke(child, top, dy) else top
            }

            // 释放后让子View滑回指定位置
            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                onViewReleased?.invoke(releasedChild, xvel, yvel)
            }

        })
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return dragHelper?.shouldInterceptTouchEvent(ev!!)!!
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        dragHelper?.processTouchEvent(event!!)
        return true
    }

    override fun computeScroll() {
        if (dragHelper?.continueSettling(true)!!)
        {
            invalidate()
        }
    }
    
}