package com.yxd.lvjie.activity

import android.os.Bundle
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseActivity
import com.yxd.lvjie.R
import com.yxd.lvjie.dialog.ProjectDialog
import kotlinx.android.synthetic.main.advanced_settings.*
import kotlinx.android.synthetic.main.enter_advanced_settings.*
import kotlinx.android.synthetic.main.header.*

/**
 * 高级设置
 */
@LayoutId(R.layout.activity_advanced_setting)
class AdvancedSettingActivity : BaseActivity() {

    override fun init(bundle: Bundle?) {

        tvTitle.text = "进入高级设置"

        btnEnter.click {
            if(etPassword.str.isEmpty()){
                "请输入密码".toast()
                return@click
            }
            if(etPassword.str != "123456"){
                ProjectDialog(this).setInfo("密码输入错误，请输入正确的密码",
                    "确定", false){
                    it.dismiss()
                }.show()
                return@click
            }
            tvTitle.text = "高级设置"
            llEnterPassword.gone()
        }

        val list = listOf("设备标定", "主控制板寄存器表")
        rvAdvancedSetting.wrap.rvAdapter(list,{
            h, p->
            h.tv(R.id.tvName).txt(list[p])
            h.itemClick {
                when(p){
                    0->goTo<DeviceBiaoDingActivity>()
                    1->{

                    }
                }
            }
        }, R.layout.item_advanced_setting)

    }

}