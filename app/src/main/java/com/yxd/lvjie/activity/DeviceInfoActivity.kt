package com.yxd.lvjie.activity

import android.os.Bundle
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.base.BaseActivity
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import kotlinx.android.synthetic.main.activity_device_info.*
import kotlinx.android.synthetic.main.header.*

/**
 * 设备信息
 */
@LayoutId(R.layout.activity_device_info)
class DeviceInfoActivity : ProjectBaseActivity() {

    override fun init(bundle: Bundle?) {
        tvZhuKongBanYingJianXinXi.text = ""

        tvZhuKongBanRuanJianBanBen.text = ""

        tvSheBeiBianHao.text = ""

        tvIMEI.text = ""

        tvMuBiaoIpDiZhi.text = ""

        tvWuXianDianDaiMa.text = ""
    }

}