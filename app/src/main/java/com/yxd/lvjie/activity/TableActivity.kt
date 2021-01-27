package com.yxd.lvjie.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Message
import com.yxd.baselib.annotation.Bus
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.utils.BusUtils
import com.yxd.baselib.utils.DialogUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.bluetooth.Utils
import com.yxd.lvjie.constant.Cmd
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.utils.CmdUtils
import kotlinx.android.synthetic.main.activity_table.*
import kotlinx.android.synthetic.main.header.*
import org.greenrobot.eventbus.Subscribe

/**
 * 寄存器表
 * @author YeXuDong
 */
@Bus
@LayoutId(R.layout.activity_table)
class TableActivity : ProjectBaseActivity() {

    /**
     * 第一列表单数据
     */
    val listColumn1 = mutableListOf("内容")

    /**
     * 第三列表单数据
     */
    var listColumn3 = byteArrayOf().toMutableList()

    fun ByteArray.toGBK(): String {
        return Utils.ByteArraytoHex(this)
    }

    fun ByteArray.toUTF8(log: Boolean, pre: String): String {
        val str = Utils.ByteArraytoHex(this)
        if (log) {
            ("转换前：" + Utils.ByteArraytoHex(this) + " 转换后：" + str).logD(pre = pre)
        }
        return str
    }

    fun ByteArray.toUTF16(log: Boolean = false, pre:String = ""): String {
        val str = Utils.ByteArraytoHex(this)
        if(log){
            ("转换前："+Utils.ByteArraytoHex(this)+" 转换后："+str).logD(pre = pre)
        }
        return str
    }

    fun ByteArray.toUTF32(): String {
        return Utils.ByteArraytoHex(this)
    }

    fun ByteArray.toFloat32(): String {
        return CmdUtils.hex2Float(Utils.ByteArraytoHex(this)).toString()
    }

    @Subscribe
    fun handle(msg: Message) {
        when (msg.what) {
            MsgWhat.CMD_TABLE1 -> {
                val array = msg.obj as ByteArray
                listColumn1.add(Utils.byteToASCII(array.sliceArray(3..22)))
                listColumn1.add(Utils.byteToASCII(array.sliceArray(23..42)))
                listColumn1.add(Utils.byteToASCII(array.sliceArray(43..62)))
                listColumn1.add(Utils.byteToASCII(array.sliceArray(63..82)))
                listColumn1.add(Utils.byteToASCII(array.sliceArray(83..102)))
                listColumn1.add(Utils.byteToASCII(array.sliceArray(103..122)))
                listColumn1.add(Utils.byteToASCII(array.sliceArray(123..142)))
                listColumn1.add(Utils.byteToASCII(array.sliceArray(143..162)))
                listColumn1.add(Utils.byteToASCII(array.sliceArray(163..182)))
                listColumn1.add(Utils.byteToASCII(array.sliceArray(183..202)))
                listColumn1.add(Utils.byteToASCII(array.sliceArray(203..222)))
                listColumn1.add(array.sliceArray(223..242).toGBK())
                listColumn1.add(array.sliceArray(243..262).toGBK())
                rv3.update()
            }
            MsgWhat.CMD_TABLE2 -> {
                val array = msg.obj as ByteArray
                (3..22).forEach {
                    listColumn1.add(array.sliceArray(it..it).toUTF8(false, ""))
                }
                listColumn1.add(array.sliceArray(23..26).toFloat32())
                listColumn1.add(array.sliceArray(27..28).toUTF16())
                listColumn1.add(array.sliceArray(29..30).toUTF16())
                listColumn1.add(array.sliceArray(31..32).toUTF16())
                listColumn1.add(array.sliceArray(33..36).toUTF32())
                listColumn1.add(array.sliceArray(37..40).toUTF32())
                rv3.update()
            }
            MsgWhat.CMD_TABLE3 -> {
                val array = msg.obj as ByteArray
                if (array.size == 100) {
                    listColumn3.clear()
                }
                listColumn3.addAll(array.toList())
                if (array.size != 100) {
                    val lastArray = listColumn3.toByteArray()
                    lastArray.size.logD(pre = "**************")
                    for (i in 3..47 step 4) {
                        listColumn1.add(lastArray.sliceArray(i..(i + 3)).toFloat32())

                    }
                    listColumn1.add(lastArray.sliceArray(51..51).toUTF8(true, "传感器类型："))
                    listColumn1.add(lastArray.sliceArray(52..52).toUTF8(true, "算法类型："))
                    listColumn1.add(lastArray.sliceArray(53..54).toUTF16(true, "传感器校准点："))
                    listColumn1.add(lastArray.sliceArray(55..56).toUTF16(true, "传感器档位："))
                    for (i in 57..173 step 4) {
                        listColumn1.add(lastArray.sliceArray(i..(i + 3)).toFloat32())
                    }
                    rv3.update()
                }

            }
        }
    }

    lateinit var pd:ProgressDialog

    override fun init(bundle: Bundle?) {

        tvSubTitle.txt("刷新").show().click {
            reqData()
        }

        pd = DialogUtils.createProgressDialog(this, "正在获取数据...")

        reqData()

        val listNo = mutableListOf("寄存器编号")
        listNo.addAll((0..12).map { "REG_INFO$it" })
        listNo.addAll((0..25).map { "REG_CONF$it" })
        listNo.addAll((0..45).map { "REG_DATA$it" })

        val listFunction = listOf(
            "寄存器功能",
            "主控板硬件信息",
            "主控板软件版本",
            "设备编号",
            "IMEI",
            "ICCID",
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
                h.tv(R.id.tv).txt(item).bgColor(
                    when (i) {
                        0 -> "#ffffff"
                        in 1..12 -> "#FFF2CB"
                        in 13..16 -> "#FBE4D5"
                        in 17..38 -> "#ECECEC"
                        else -> "#C0C4CB"
                    }
                )

            }, null, R.layout.item_text
        )

        rv2.wrap.generate(
            listFunction, { h, i, item ->
                h.tv(R.id.tv).txt(item)
                h.tv(R.id.tv).txt(item).bgColor(
                    when (i) {
                        0 -> "#ffffff"
                        in 1..12 -> "#FFF2CB"
                        in 13..16 -> "#FBE4D5"
                        in 17..38 -> "#ECECEC"
                        else -> "#C0C4CB"
                    }
                )
            }, null, R.layout.item_text
        )

        rv3.wrap.generate(
            listColumn1, { h, i, item ->
                h.tv(R.id.tv).txt(item)
            }, null, R.layout.item_text
        )
    }

    private fun reqData() {
        pd.show()
        listColumn1.clear()
        listColumn1.add("内容")
        // 获取表1，2，3的数据
        BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.TABLE1)
        doDelayTask(1000) {
            BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.TABLE2)
            doDelayTask(1000) {
                pd.dismiss()
                BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.TABLE3)
            }
        }
    }
}