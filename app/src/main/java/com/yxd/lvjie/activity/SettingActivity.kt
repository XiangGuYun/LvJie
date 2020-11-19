package com.yxd.lvjie.activity

import android.os.Bundle
import com.yp.baselib.annotation.LayoutId
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import kotlinx.android.synthetic.main.activity_setting.*

@LayoutId(R.layout.activity_setting)
class SettingActivity : ProjectBaseActivity() {

    override fun init(bundle: Bundle?) {
        val listSetting = listOf(
                "固件版本" to "V1.0 升级",
                "line" to "",
                "设备唤醒时间" to "12:42:20",
                "仪器选择" to "调试模式",
                "算法选择" to "算法1",
                "line" to "",
                "高级设置" to "",
                "设备信息" to "",
                "应用更新" to "",
                "退出登录" to ""
        )

        rvSetting.wrap.rvMultiAdapter(listSetting,
                {
                    h,p->
                    if(p != 0 && listSetting[p].first != "line"){
                        h.tv(R.id.tvLeft).txt(listSetting[p].first)
                        h.tv(R.id.tvRight).txt(listSetting[p].second)
                        if(p == 3) h.iv(R.id.ivMore).hide()
                    }
                   h.itemClick {
                       when(listSetting[p].first){
                           "高级设置"->{
                               goTo<AdvancedSettingActivity>()
                           }
                       }
                   }
                },{
            when{
                it == 0 -> 0
                listSetting[it].first == "line" -> 2
                else -> 1
            }
        }, R.layout.item_setting_top, R.layout.item_setting, R.layout.line_setting
        )

    }

}