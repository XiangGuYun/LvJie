package com.yxd.lvjie.utils

import com.yxd.baselib.base.BaseActivity
import com.yxd.baselib.utils.BusUtils
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
    fun write(writeValue: Float, h47: Int, g47: String) {
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
        BusUtils.post(MsgWhat.SEND_COMMAND, "$first$second$third$fourth$fifth$newValue")
    }

    fun formatMsgContent(data: ByteArray): String? {
        return "HEX:" + Utils.ByteArraytoHex(data) + "  ASSCII:" + Utils.byteToASCII(data)
    }

    fun hex2Float(hex: String): Float {
        var returnValue = 0f
        returnValue = try {
            java.lang.Float.intBitsToFloat(Integer.valueOf(hex.trim { it <= ' ' }, 16))
        } catch (e: NumberFormatException) {
            1f
        }
        return returnValue
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
        BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.ORIGIN_STRENGTH)
        activity.doDelayTask(1000) {
            BusUtils.post(MsgWhat.SEND_COMMAND, markPointValueList[index])
            activity.doDelayTask(1000) {
                BusUtils.post(MsgWhat.SEND_COMMAND, markPointFreqList[index])
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

    /**
     * 自校正
     */
    fun autoAdjust() {
        BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.AUTO_ADJUST)
    }

    /**
     * 02 41 01 00 00 10 44 20 11 06 00 22 40 00 00 00 00 44 87 80 00 01 44 20 11 06 00 22 41 00 00 00 00 43 88 00 00 01 44 20 11 06 00 22 42 42 55 51 78 43 DE 00 00 01 44 20 11 06 00 22 43 42 55 51 78 44 AD 00 00 01 44 20 11 06 00 22 44 42 52 E8 A9 44 3F 00 00 01 44 20 11 06 00 22 45 42 52 E8 A9 44 BC 80
     */
    fun decodeHistoryData(hex:List<String>): HistoryData {
        return HistoryData(
            hex2Float(hex[1]),
            hex2Float(hex[2]),
            hex2Float(hex[3]),
            hex2Float(hex[4]),
            hex2Float(hex[5]),
            hex2Float(hex[6]),
            hex2Float(hex[7]).toString(),
            hex2Float(hex[8]).toString(),
            hex2Float(hex[9]).toString(),
            hex2Float(hex[10]).toString(),
            hex2Float(hex[11]).toString(),
            hex2Float(hex[12]).toString(),
            hex2Float(hex[13]).toString(),
            hex2Float(hex[14]).toString(),
        )
    }

    data class HistoryData(
        val year: Float = 0f, // 字节2
        val month: Float = 0f, // 字节3
        val day: Float = 0f, // 字节4
        val hour: Float = 0f, // 字节5
        val minute: Float = 0f, // 字节6
        val second: Float = 0f, // 字节7
        val strength1: String = "", // 字节8
        val strength2: String = "", // 字节9
        val strength3: String = "", // 字节10
        val strength4: String = "", // 字节11
        val freq1: String = "", // 字节12
        val freq2: String = "", // 字节13
        val freq3: String = "", // 字节14
        val freq4: String = "", // 字节15
    )

}