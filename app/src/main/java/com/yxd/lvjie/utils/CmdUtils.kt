package com.yxd.lvjie.utils

import com.yp.baselib.utils.BusUtils
import com.yxd.lvjie.constant.Cmd
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.constant.Utils

/**
 * 蓝牙数据发送与处理的工具类
 */
object CmdUtils {

    fun formatMsgContent(data: ByteArray): String? {
        return "HEX:" + Utils.ByteArraytoHex(data) + "  ASSCII:" + Utils.byteToASCII(data)
    }

    private fun hex2Float(hex: String): Float {
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
     * 获取IMEI
     */
    fun getDeviceIMEI() {
        BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.IMEI)
    }

    /**
     * 获取设备编号
     */
    fun getDeviceNumber() {
        BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.DEVICE_NO)
    }

    /**
     * 获取电量
     */
    fun getElectricQuantity() {
        BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.EQ)
    }

    /**
     * 解析电量
     */
    fun decodeElectricQuantity(hex: String): Float {
        return hex2Float(hex.substring(6, 14))
    }

    /**
     * 获取强度和频率
     */
    fun getStrengthAndFrequency() {
        BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.STRENGTH_FREQ)
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

}