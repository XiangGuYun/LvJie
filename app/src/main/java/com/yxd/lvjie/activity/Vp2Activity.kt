package com.yxd.lvjie.activity

import android.os.Bundle
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.base.BaseActivity
import com.yxd.lvjie.R
import com.yxd.lvjie.fragment.DeviceListFragment
import com.yxd.lvjie.view.TabView
import kotlinx.android.synthetic.main.activity_vp2.*

/**
 *
 * @author YeXuDong
 */
@LayoutId(R.layout.activity_vp2)
class Vp2Activity : BaseActivity() {

    override fun init(bundle: Bundle?) {
//        vpTest.setVertical().generate((1..10).toList(),
//            {
//                h,p,item->
//                h.tv(R.id.btnT
        vpTest.setAllowUserInput(false)
            .bindFragment(this, (1..3).toList().map {
                DeviceListFragment.newInstance(DeviceListFragment.Type.GUAN_CHA_DIAN)
            }).bindIndicator(
                tlTest,
                3,
                "#3C79FB".color(),
                30,
                2,
                1
            ) {
                TabView(this).apply {
                    tv(R.id.tvCell).txt("Page${it + 1}")
                }
            }
    }

}