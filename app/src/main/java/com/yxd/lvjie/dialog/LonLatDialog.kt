package com.yxd.lvjie.dialog

import android.content.Context
import com.kotlinlib.common.dialog.BaseDialog
import com.yxd.baselib.annotation.DialogInfo
import com.yxd.baselib.utils.ToastUtils
import com.yxd.lvjie.R

/**
 * 经纬度对话框
 * @constructor
 */
@DialogInfo(270, 170, R.layout.dialog_latlon)
class LonLatDialog(ctx: Context) : BaseDialog(ctx) {

    init {
        dv.tv(R.id.tvCancel).click { dismiss() }
    }

    fun init(lon: String, lat: String, yesClick: (String, String) -> Unit): LonLatDialog {
        dv.et(R.id.etLon).txt(lon)
        dv.et(R.id.etLat).txt(lat)
        dv.v(R.id.tvConfirm).click {
            if (dv.et(R.id.etLon).isEmpty) {
                ToastUtils.toast("请输入经度！")
                return@click
            }
            if (dv.et(R.id.etLat).isEmpty) {
                ToastUtils.toast("请输入纬度！")
                return@click
            }
            yesClick.invoke(dv.et(R.id.etLon).str, dv.et(R.id.etLat).str)
            dismiss()
        }
        return this
    }


}