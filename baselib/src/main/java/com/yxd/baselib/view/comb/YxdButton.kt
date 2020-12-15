package com.yxd.baselib.view.comb

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.Gravity
import com.yxd.baselib.R
import com.yxd.baselib.ex.BaseEx
import com.yxd.baselib.utils.ShapeUtils

class YxdButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatButton(context, attrs, defStyleAttr), BaseEx {

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.YxdButton)
        val bgCommonColor = ta.getColor(R.styleable.YxdButton_bgCommonColor, Color.GRAY)
        val bgPressedColor = ta.getColor(R.styleable.YxdButton_bgPressedColor, bgCommonColor)
        val cornerRadius = ta.getDimension(R.styleable.YxdButton_cornerRadius, 0f)
        val strokeWidth = ta.getDimension(R.styleable.YxdButton_strokeWidth, 0f)
        val strokeColor = ta.getColor(R.styleable.YxdButton_strokeColor, Color.BLACK)

        gravity = Gravity.CENTER

        val select = StateListDrawable()

        val bgUnPressed = ShapeUtils.getRectangleDrawable(
            cornerRadius = cornerRadius,
            solidColor = bgCommonColor,
            strokeWidth = strokeWidth.toInt(),
            strokeColor = strokeColor
        )

        val bgPressed = ShapeUtils.getRectangleDrawable(
            cornerRadius,
            bgPressedColor
        )

        select.addState(intArrayOf(-android.R.attr.state_pressed), bgUnPressed)
        select.addState(intArrayOf(android.R.attr.state_pressed), bgPressed)

        background = select

        // 防止未设置点击事件前select变化背景色无效
        click {

        }

        ta.recycle()
    }

}