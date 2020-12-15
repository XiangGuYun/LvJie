package com.yxd.baselib.view.comb

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.drawerlayout.widget.DrawerLayout
import com.yxd.baselib.ex.BaseEx

class YxdDrawer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DrawerLayout(context, attrs, defStyleAttr), BaseEx {

    fun setLeft(layoutId: Int, width: Int = -1) {
        val leftMenu = LayoutInflater.from(context).inflate(layoutId, null)
        leftMenu.layoutParams = LayoutParams(if (width == -1) MP else width, MP).apply {
            gravity = Gravity.START
        }
        leftMenu.isClickable = true
        addView(leftMenu)
    }

    fun setRight(layoutId: Int, width: Int = -1) {
        val leftMenu = LayoutInflater.from(context).inflate(layoutId, null)
        leftMenu.layoutParams = LayoutParams(if (width == -1) MP else width, MP).apply {
            gravity = Gravity.END
        }
        leftMenu.isClickable = true
        addView(leftMenu)
    }

    fun openLeft() {
        openDrawer(Gravity.LEFT)
    }

    fun openRight() {
        openDrawer(Gravity.RIGHT)
    }

    fun closeLeft() {
        closeDrawer(Gravity.LEFT)
    }

    fun closeRight() {
        closeDrawer(Gravity.RIGHT)
    }

    fun closeAll() {
        closeDrawers()
    }

}