package com.yxd.lvjie.activity

import android.os.Bundle
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseActivity
import com.yxd.lvjie.R
import kotlinx.android.synthetic.main.activity_device_info.*
import kotlinx.android.synthetic.main.header.*

/**
 * 设备信息
 */
@LayoutId(R.layout.activity_device_info)
class DeviceInfoActivity : BaseActivity() {

    override fun init(bundle: Bundle?) {
        tvTitle.text = "设备信息"

        tvZhuKongBanYingJianXinXi.text = ""

        tvZhuKongBanRuanJianBanBen.text = ""

        tvSheBeiBianHao.text = ""

        tvIMEI.text = ""

        tvMuBiaoIpDiZhi.text = ""

        tvWuXianDianDaiMa.text = ""
    }

}