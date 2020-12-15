package com.yxd.baselib.utils

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yxd.baselib.FLLP
import com.yxd.baselib.ex.ViewEx
import java.util.*


/**
 * 对话框工具类
 */
object DialogUtils : ViewEx {

    /**
     * 创建系统进度对话框
     * @param context Context
     * @param message String
     * @param isCanceledOnTouchOutside Boolean
     * @param haveProgressValue Boolean
     * @param max Int
     * @param initProgress Int
     * @return ProgressDialog
     */
    fun createProgressDialog(
        context: Context,
        message: String,
        isCanceledOnTouchOutside: Boolean = false,
        haveProgressValue: Boolean = false,
        max: Int = 100,
        initProgress: Int = 0
    ): ProgressDialog {
        val dialog = ProgressDialog(context)
        dialog.max = max
        dialog.setMessage(message)
        dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside)
        dialog.setProgressStyle(
            if (haveProgressValue)
                ProgressDialog.STYLE_HORIZONTAL
            else
                ProgressDialog.STYLE_SPINNER
        )
        dialog.progress = initProgress
        return dialog
    }

    /**
     * 创建系统自带日期选择器
     * @param ctx Context
     * @param yearMonthDay 默认显示的年月日，如果不传则表示显示当前时间
     * @param getDate 获取选择日期的回调
     * @return DatePickerDialog
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun createDatePicker(
        ctx: Context,
        yearMonthDay: Triple<Int, Int, Int>? = null,
        getDate: (Date) -> Unit
    ): DatePickerDialog {

        return if (yearMonthDay != null) {
            DatePickerDialog(
                ctx,
                { view, year, monthOfYear, dayOfMonth ->
                    val calendar: Calendar = Calendar.getInstance()
                    calendar.set(year, monthOfYear, dayOfMonth)
                    getDate.invoke(calendar.time)
                }, yearMonthDay.first, yearMonthDay.second, yearMonthDay.third
            )
        } else {
            val calendar: Calendar = Calendar.getInstance()
            val date = Triple(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            DatePickerDialog(
                ctx,
                { view, year, monthOfYear, dayOfMonth ->
                    val calendar: Calendar = Calendar.getInstance()
                    calendar.set(year, monthOfYear, dayOfMonth)
                    getDate.invoke(calendar.time)
                }, date.first, date.second, date.third
            )
        }

    }

    /**
     * 创建系统自带时间选择器
     * @param ctx Context
     * @param hourMinute Pair<Int, Int>?
     * @param getTime Function2<Int, Int, Unit>
     * @return TimePickerDialog
     */
    fun createTimePicker(
        ctx: Context,
        hourMinute: Pair<Int, Int>? = null,
        getTime: (Int, Int) -> Unit
    ): TimePickerDialog {
        if (hourMinute != null) {
            return TimePickerDialog(
                ctx,
                { view, hourOfDay, minute ->
                    getTime.invoke(hourOfDay, minute)
                }, hourMinute.first, hourMinute.second, true
            )
        } else {
            val calendar: Calendar = Calendar.getInstance()
            val date = Pair(
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
            )
            return TimePickerDialog(
                ctx,
                { view, hourOfDay, minute ->
                    getTime.invoke(hourOfDay, minute)
                }, date.first, date.second, true
            )
        }
    }

    /**
     * 获取底部弹窗BottomSheetDialog
     */
    fun createBottomSheetDialog(ctx: Context, viewId: Int): BottomSheetDialog {
        val dialog = BottomSheetDialog(ctx)
        val dialogView = LayoutInflater.from(ctx).inflate(viewId, null)
        dialog.setContentView(dialogView)
        dialog.delegate.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.setBackgroundColor(ctx.resources.getColor(android.R.color.transparent))
        return dialog
    }

    /**
     * 创建消息弹窗
     */
    fun createMessageDialog(
        ctx: Context,
        message: String,
        title: String? = null,
        yes: String? = null,
        no: String? = null,
        yesCallback: ((dialog: DialogInterface) -> Unit)? = null,
        noCallback: ((dialog: DialogInterface) -> Unit)? = null,
        isMaterial: Boolean = true
    ): AlertDialog {
        val builder = if (isMaterial) {
            MaterialAlertDialogBuilder(ctx)
        } else {
            AlertDialog.Builder(ctx)
        }
        builder.setMessage(message)
        if (title != null) {
            builder.setTitle(title)
        }
        if (yes != null) {
            builder.setPositiveButton(yes) { dialog, _ ->
                dialog.dismiss()
                yesCallback?.invoke(dialog!!)
            }
        }
        if (no != null) {
            builder.setNegativeButton(no) { dialog, _ ->
                dialog.dismiss()
                noCallback?.invoke(dialog!!)
            }
        }
        return builder.create()
    }

    /**
     * 创建单选弹窗
     * @param ctx Context
     * @param checkItems Array<String>
     * @param checkedItem Int
     * @param onChecked Function1<Int, Unit>
     * @param title String?
     * @return AlertDialog
     */
    fun createOptionsDialog(
        ctx: Context,
        checkItems: Array<String>,
        checkedItem: Int,
        onChecked: (Int) -> Unit,
        title: String? = null,
        isMaterial: Boolean = true
    ): AlertDialog {
        val builder = if (isMaterial) {
            MaterialAlertDialogBuilder(ctx)
        } else {
            AlertDialog.Builder(ctx)
        }
        builder.setSingleChoiceItems(
            checkItems, checkedItem
        ) { dialog, which ->
            onChecked.invoke(which)
            dialog.dismiss()
        }
        if (title != null) {
            builder.setTitle(title)
        }
        return builder.create()
    }

    fun createMessageBuilder(
        ctx: Context,
        message: String,
        title: String? = null,
        yes: String? = null,
        no: String? = null,
        yesCallback: ((dialog: DialogInterface) -> Unit)? = null,
        noCallback: ((dialog: DialogInterface) -> Unit)? = null,
        isMaterial: Boolean = true
    ): AlertDialog.Builder {
        val builder = if (isMaterial) {
            MaterialAlertDialogBuilder(ctx)
        } else {
            AlertDialog.Builder(ctx)
        }
        builder.setMessage(message)
        if (title != null) {
            builder.setTitle(title)
        }
        if (yes != null) {
            builder.setPositiveButton(yes) { dialog, _ ->
                yesCallback?.invoke(dialog!!)
            }
        }
        if (no != null) {
            builder.setNegativeButton(no) { dialog, _ ->
                noCallback?.invoke(dialog!!)
            }
        }
        return builder
    }

    /**
     * 设置自定义视图弹窗
     */
    fun createCustomViewDialog(
        ctx: Context,
        layoutId: Int,
        title: String? = null,
        yes: String? = null,
        no: String? = null,
        yesCallback: ((dialog: DialogInterface) -> Unit)? = null,
        noCallback: ((dialog: DialogInterface) -> Unit)? = null
    ): AlertDialog {
        val builder = AlertDialog.Builder(ctx)
        builder.setView(layoutId)
        if (title != null) {
            builder.setTitle(title)
        }
        if (yes != null) {
            builder.setPositiveButton(yes) { dialog, _ ->
                yesCallback?.invoke(dialog!!)
            }
        }
        if (no != null) {
            builder.setNegativeButton(no) { dialog, _ ->
                noCallback?.invoke(dialog!!)
            }
        }
        return builder.create()

    }

    fun createCustomViewDialog(
        ctx: Context,
        viewId: View,
        title: String? = null,
        yes: String? = null,
        no: String? = null,
        yesCallback: ((dialog: DialogInterface) -> Unit)? = null,
        noCallback: ((dialog: DialogInterface) -> Unit)? = null,
        isMaterial: Boolean = true
    ): AlertDialog {
        val builder = if (isMaterial) {
            MaterialAlertDialogBuilder(ctx)
        } else {
            AlertDialog.Builder(ctx)
        }
        builder.setView(viewId)
        if (title != null) {
            builder.setTitle(title)
        }
        if (yes != null) {
            builder.setPositiveButton(yes) { dialog, _ ->
                yesCallback?.invoke(dialog!!)
            }
        }
        if (no != null) {
            builder.setNegativeButton(no) { dialog, _ ->
                noCallback?.invoke(dialog!!)
            }
        }
        return builder.create()

    }


    /**
     * 创建输入对话框
     *
     * DialogUtils.createInputDialog(this, "标题", "请输入密码","确定", "取消",
     * {
     *      dialog, text ->
     *      text.toast()
     * }, doEditText = {
     *      it.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
     * }).show()
     * @param ctx Context
     * @param title String?
     * @param hint String?
     * @param yes String?
     * @param no String?
     * @param yesCallback Function2<[@kotlin.ParameterName] DialogInterface, [@kotlin.ParameterName] String, Unit>?
     * @param noCallback Function1<[@kotlin.ParameterName] DialogInterface, Unit>?
     * @param doEditText Function1<EditText, Unit>?
     * @param isMaterial Boolean
     * @return AlertDialog
     */
    fun createInputDialog(
        ctx: Context,
        title: String? = null,
        hint: String? = null,
        yes: String? = null,
        no: String? = null,
        yesCallback: ((dialog: DialogInterface, text: String) -> Unit)? = null,
        noCallback: ((dialog: DialogInterface) -> Unit)? = null,
        doEditText: ((EditText) -> Unit)? = null,
        isMaterial: Boolean = true
    ): AlertDialog {
        val builder = if (isMaterial) {
            MaterialAlertDialogBuilder(ctx)
        } else {
            AlertDialog.Builder(ctx)
        }
        val editText = EditText(ctx)
        editText.hint = hint
        builder.setView(FrameLayout(ctx).apply { addView(editText) })
        editText.doLP<FLLP> {
            it.leftMargin = DensityUtils.dp2px(20)
            it.rightMargin = DensityUtils.dp2px(20)
            it.topMargin = DensityUtils.dp2px(20)
            it.bottomMargin = DensityUtils.dp2px(20)
        }
        doEditText?.invoke(editText)
        if (title != null) {
            builder.setTitle(title)
        }
        if (yes != null) {
            builder.setPositiveButton(yes) { dialog, _ ->
                yesCallback?.invoke(dialog!!, editText.text.toString())
            }
        }
        if (no != null) {
            builder.setNegativeButton(no) { dialog, _ ->
                noCallback?.invoke(dialog!!)
            }
        }
        return builder.create()
    }


}