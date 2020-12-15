package com.kotlinlib.common.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import com.yxd.baselib.annotation.DialogInfo
import com.yxd.baselib.ex.BaseEx
import com.yxd.baselib.utils.DensityUtils

/**
 * 对话框基类
 * @property dv View
 * @constructor
 */
open class BaseDialog(ctx: Context) : Dialog(ctx), BaseEx{

    var dv: View

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogInject = this::class.annotations[0] as DialogInfo
        dv = LayoutInflater.from(context).inflate(dialogInject.layoutId, null)
        setContentView(dv)
        val dialogWindow = window
        dialogWindow!!.setBackgroundDrawableResource(android.R.color.transparent)
        val lp = dialogWindow.attributes
        if(dialogInject.width!=-1){
            lp.width = DensityUtils.dp2px( dialogInject.width.toFloat())
        } else {
            lp.width = ctx.resources.displayMetrics.widthPixels
        }
        if(dialogInject.height!=-1){
            lp.height = DensityUtils.dp2px( dialogInject.height.toFloat())
        } else {
            lp.height = ctx.resources.displayMetrics.heightPixels
        }
        dialogWindow.attributes = lp
//        setCanceledOnTouchOutside(false)
    }


}