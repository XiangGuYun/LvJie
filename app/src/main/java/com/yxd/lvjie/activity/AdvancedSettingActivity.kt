package com.yxd.lvjie.activity

import android.os.Bundle
import com.yxd.baselib.annotation.LayoutId
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.dialog.ProjectDialog
import com.yxd.lvjie.helper.SPHelper
import com.yxd.lvjie.net.Req
import kotlinx.android.synthetic.main.advanced_settings.*
import kotlinx.android.synthetic.main.enter_advanced_settings.*
import kotlinx.android.synthetic.main.header.*

/**
 * 高级设置
 */
@LayoutId(R.layout.activity_advanced_setting)
class AdvancedSettingActivity : ProjectBaseActivity() {

    override fun init(bundle: Bundle?) {

        llEnterPassword.setOnTouchListener { v, event ->
            true
        }

        if(SPHelper.getAdvancedPwd().isNotEmpty()){
            closeKeyboard()
            tvTitle.text = "高级设置"
            llEnterPassword.gone()
        }

        btnEnter.click {
            if (etPassword.isEmpty) {
                "请输入密码".toast()
                return@click
            }
            Req.verifyPassword(etPassword.str) {
                if (it.code == 0) {
                    SPHelper.putAdvancedPwd(etPassword.str)
                    tvTitle.text = "高级设置"
                    llEnterPassword.gone()
                } else {
                    ProjectDialog(this).setInfo(
                        it.message.toString(),
                        "确定", false
                    ) {
                        it.dismiss()
                        etPassword.txt("")
                    }.show()
                }
            }
        }

        val list = listOf("设备标定", "主控制板寄存器表")
        rvAdvancedSetting.wrap.generate(list, { h, p, item ->
            h.tv(R.id.tvName).txt(item)
            h.itemClick {
                when (p) {
                    0 -> goTo<DeviceMarkActivity>()
                    1 -> goTo<TableActivity>()
                }
            }
        }, null, R.layout.item_advanced_setting)

        if(SPHelper.getAdvancedPwd().isEmpty()){
            openKeyboardDelay(etPassword, 100)
        }

    }

}