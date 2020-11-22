package com.yxd.lvjie.utils

import com.yp.baselib.base.BaseActivity
import com.yp.baselib.utils.BusUtils
import com.yxd.lvjie.bluetooth.Utils
import com.yxd.lvjie.constant.Cmd
import com.yxd.lvjie.constant.MsgWhat

/**
 * 蓝牙数据发送与处理的工具类
 */
object CmdUtils {

    /**
     * 写入数据
     * @param writeValue Float
     * @param h47 Int
     * @param g47 String
     * @return String
     */
    fun write(writeValue: Float, h47: Int, g47: String): String {
        val first = "0210"
        // 00fe
        var second = h47.toBigDecimal().divide(2.toBigDecimal()).toInt().toString(16)
        while (second.length < 4) {
            second = "0$second"
        }
        // 0002
        var third = g47.toBigDecimal().divide(2.toBigDecimal()).toString()
        while (third.length < 4) {
            third = "0$third"
        }
        // 04
        var fourth = g47
        while (fourth.length < 2) {
            fourth = "0$fourth"
        }
        // 42c80000
        val fifth = float2Hex(writeValue)
        // A5E7
        val valueCrc16 =
            Crc16Utils.calcCrc16(Utils.hexStringToByteArray("$first$second$third$fourth$fifth"))
                .toString(16)
        val newValue = Utils.ByteArraytoHex(Utils.hexStringToByteArray(valueCrc16).reversedArray())
            .replace(" ", "")
        return "$first$second$third$fourth$fifth$newValue"
    }

    fun formatMsgContent(data: ByteArray): String? {
        return "HEX:" + Utils.ByteArraytoHex(data) + "  ASSCII:" + Utils.byteToASCII(data)
    }

    fun hex2Float(hex: String): Float {
        return java.lang.Float.intBitsToFloat(Integer.valueOf(hex.trim { it <= ' ' }, 16))
    }

    fun float2Hex(f: Float): String {
        return Integer.toHexString(java.lang.Float.floatToIntBits(f))
    }

    /**
     * 截取字节数组
     * @param bytes ByteArray
     * @param start Int
     * @param length Int
     * @return ByteArray
     */
    fun sliceByteArray(bytes: ByteArray, start: Int, length: Int): ByteArray {
        val newArray = bytes.toMutableList()
            .subList(start, length)
        newArray.removeAll {
            it.toInt() == 0
        }
        return newArray.toByteArray()
    }

    /**
     *  发送命令来获取IMEI
     */
    fun sendCmdForIMEI() {
        BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.IMEI)
    }

    /**
     * 发送命令来获取设备编号
     */
    fun sendCmdForDeviceNumber() {
        BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.DEVICE_NO)
    }

    /**
     * 发送命令来获取电量
     */
    fun sendCmdForElectricQuantity() {
        BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.EQ)
    }

    /**
     *  发送命令来获取强度和频率
     */
    fun sendCmdForStrengthAndFrequency() {
        BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.STRENGTH_FREQ)
    }

    private val markPointFreqList = listOf(
        Cmd.STARTED_FREQ1,
        Cmd.STARTED_FREQ2,
        Cmd.STARTED_FREQ3,
        Cmd.STARTED_FREQ4,
        Cmd.STARTED_FREQ5
    )

    private val markPointValueList = listOf(
        Cmd.STARTED_VALUE1,
        Cmd.STARTED_VALUE2,
        Cmd.STARTED_VALUE3,
        Cmd.STARTED_VALUE4,
        Cmd.STARTED_VALUE5
    )

    private val markPointTestValue = listOf(
        Cmd.STARTED_TEST_VALUE1,
        Cmd.STARTED_TEST_VALUE2,
        Cmd.STARTED_TEST_VALUE3,
        Cmd.STARTED_TEST_VALUE4,
        Cmd.STARTED_TEST_VALUE5
    )

    /**
     * 发送命令来获取标定点的数据
     * @param activity BaseActivity
     * @param index Int 标定点的索引位，标定点1对应0，标定点2对应1，以此类推
     */
    fun sendCmdForMarkPoint(activity: BaseActivity, index: Int) {
        BusUtils.post(MsgWhat.SEND_COMMAND, markPointFreqList[index])
        activity.doDelayTask(350) {
            BusUtils.post(MsgWhat.SEND_COMMAND, markPointValueList[index])
            activity.doDelayTask(350) {
                BusUtils.post(MsgWhat.SEND_COMMAND, markPointTestValue[index])
            }
        }
    }

    /**
     * 解析强度和频率
     * @param hex String
     * @return Pair<Float, Float>
     */
    fun decodeStrengthAndFrequency(hex: String): Pair<Float, Float> {
        val newHex = hex.replace(" ", "")
        val hexStrength = newHex.substring(6, 14)
        val hexFrequency = newHex.substring(14, 22)
        return hex2Float(hexStrength) to hex2Float(hexFrequency)
    }

    /**
     * 解析电量
     */
    fun decodeElectricQuantity(hex: String): Float {
        return hex2Float(hex.substring(6, 14))
    }
}