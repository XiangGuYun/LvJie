package com.yxd.lvjie.activity

import android.os.Bundle
import com.yxd.baselib.annotation.LayoutId
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import kotlinx.android.synthetic.main.activity_table.*

/**
 *
 * @author YeXuDong
 */
@LayoutId(R.layout.activity_table)
class TableActivity : ProjectBaseActivity() {

    override fun init(bundle: Bundle?) {

        val listNo = mutableListOf("寄存器编号")
        listNo.addAll((0..11).map { "REG_INFO$it" })
        listNo.addAll((0..25).map { "REG_CONF$it" })
        listNo.addAll((0..45).map { "REG_DATA$it" })

        val listFunction = listOf(
            "寄存器功能",
            "主控板硬件信息",
            "主控板软件版本",
            "设备编号",
            "IMEI",
            "目标IP地址",
            "无线电代码",
            "安装人员（手机号）",
            "安装时间",
            "位置-经纬度",
            "管道材质&管径",
            "安装方式",
            "安装模式",
            "电源模式",
            "工作模式",
            "事件触发",
            "事件触发EX",
            "设备当前时间-年",
            "设备当前时间-月",
            "设备当前时间-日",
            "设备当前时间-时",
            "设备当前时间-分",
            "设备当前时间-秒",
            "历史校准时间-年",
            "历史校准时间-月",
            "历史校准时间-日",
            "历史校准时间-时",
            "历史校准时间-分",
            "历史校准时间-秒",
            "闹钟时间-时",
            "闹钟时间-分",
            "闹钟时间-秒",
            "屏蔽时分秒标志位",
            "当前电池电压",
            "系统报警位",
            "蓝牙报警位",
            "NB-IoT报警位",
            "数据存储地址",
            "日志存储地址",
            "信号强度",
            "信号中心频率",
            "采样频率",
            "原始信号强度",
            "背景空白信号强度",
            "当前校准系数K",
            "信号强度-2",
            "信号中心频率-2",
            "采样频率-2",
            "原始信号强度-2",
            "背景空白信号强度-2",
            "当前校准系数K-2",
            "传感器类型",
            "算法类型",
            "传感器信号放大增益校准点",
            "传感器信号放大增益档位",
            "标定频率1",
            "标定值1",
            "标定测试值1",
            "标定频率2",
            "标定值2",
            "标定测试值2",
            "标定频率3",
            "标定值3",
            "标定测试值3",
            "标定频率4",
            "标定值4",
            "标定测试值4",
            "标定频率5",
            "标定值5",
            "标定测试值5",
            "标定频率6",
            "标定值6",
            "标定测试值6",
            "标定频率7",
            "标定值7",
            "标定测试值7",
            "标定频率8",
            "标定值1",
            "标定测试值8",
            "标定频率9",
            "标定值9",
            "标定测试值9",
            "标定频率10",
            "标定值10",
            "标定测试值10"
        )

        rv1.wrap.generate(
            listNo, { h, i, item ->
                h.tv(R.id.tv).txt(item)
            }, null, R.layout.item_text
        )

        rv2.wrap.generate(
            listFunction, { h, i, item ->
                h.tv(R.id.tv).txt(item)
            }, null, R.layout.item_text
        )

        rv3.wrap.generate(
            listOf("内容"), { h, i, item ->
                h.tv(R.id.tv).txt(item)
            }, null, R.layout.item_text
        )
    }

}