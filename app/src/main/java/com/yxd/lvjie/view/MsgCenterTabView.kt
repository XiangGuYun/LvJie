package com.yxd.lvjie.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.yxd.lvjie.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView

class MsgCenterTabView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr), IPagerTitleView {

    init {
        addView(LayoutInflater.from(context).inflate(R.layout.tabview_msg_center, null))
    }

    override fun onDeselected(index: Int, totalCount: Int) {
        findViewById<TextView>(R.id.tvCell).apply {
            textSize = 12f
            setTextColor(Color.parseColor("#333333"))
        }
    }

    override fun onSelected(index: Int, totalCount: Int) {
        findViewById<TextView>(R.id.tvCell).apply {
            textSize = 12f
            setTextColor(Color.parseColor("#333333"))
        }
    }

    override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {

    }

    override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {

    }
}