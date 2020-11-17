package com.yxd.lvjie.activity

import android.os.Bundle
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseActivity
import com.yxd.lvjie.R
import kotlinx.android.synthetic.main.activity_device_detail.*
import kotlinx.android.synthetic.main.header.*

/**
 * 设备详情
 */
@LayoutId(R.layout.activity_device_detail)
class DeviceDetailActivity : BaseActivity() {

    var isEditable = true

    override fun init(bundle: Bundle?) {
        tvTitle.text = "设备详情"

        tvSubTitle.show().txt("编辑").click {

        }

        tvXingHao.txt("型号：")

        tvDianLiang.txt("电量：")

        tvSheBeiBianHao.txt("设备编号：")

        tvShuJuGengXinShiJian.txt("数据更新时间：")

        val list = listOf(
            "设备位置：",
            "阀门编号：",
            "阀门位置：",
            "管道材质：",
            "管道口径：",
            "安装人员：",
            "公        司：",
            "安装时间：",
            "安装模式：",
            "安装方式："
        )

        rvDeviceDetail.wrap.rvMultiAdapter(list,
            {
                h,p->
                h.tv(R.id.tv1).txt(list[p])
            },
            {
                when {
                    !isEditable -> 0
                    it !in 8..9 -> {
                        1
                    }
                    else -> {
                        2
                    }
                }
            }, R.layout.item_deivie_detail1, R.layout.item_deivie_detail2, R.layout.item_deivie_detail3)
    }

}