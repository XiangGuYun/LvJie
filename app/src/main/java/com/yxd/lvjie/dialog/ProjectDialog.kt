package com.yxd.lvjie.dialog

import android.app.Dialog
import com.kotlinlib.common.Ctx
import com.kotlinlib.common.dialog.BaseDialog
import com.yp.baselib.annotation.DialogInfo
import com.yxd.lvjie.R

@DialogInfo(270, 170, R.layout.dialog_project)
class ProjectDialog(ctx: Ctx) : BaseDialog(ctx) {

    init {
        dv.tv(R.id.tvCancel).click { dismiss() }
    }

    fun setInfo(
        msg: String,
        confirmText: String,
        isShowCancelButton: Boolean,
        onConfirmClick: (Dialog) -> Unit
    ): ProjectDialog {
        dv.tv(R.id.tvMessage).txt(msg)
        dv.tv(R.id.tvConfirm).txt(confirmText).click {
            onConfirmClick.invoke(this)
        }
        if (!isShowCancelButton) {
            dv.tv(R.id.tvCancel).gone()
            dv.v(R.id.line).gone()
        }
        return this
    }

}