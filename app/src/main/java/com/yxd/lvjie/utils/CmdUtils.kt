package com.yxd.lvjie.utils

import com.yxd.baselib.base.BaseActivity
import com.yxd.baselib.utils.BusUtils
import com.yxd.baselib.utils.LogUtils
import com.yxd.lvjie.bluetooth.Utils
import com.yxd.lvjie.constant.Cmd
import com.yxd.lvjie.constant.MsgWhat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 蓝牙数据发送与处理的工具类
 */
object CmdUtils {

    fun add0(num: Int): String {
        return if (num < 10)
            "0$num"
        else
            num.toString()
    }

    /**
     * 写入当前时间并校准
     */
    fun writeCurrentTime(activity: BaseActivity) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR).toString().slice(2..3).toInt()
        val month = SimpleDateFormat("MM").format(System.currentTimeMillis()).toInt()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        val ba = byteArrayOf(
            0x02.toByte(), 0x10.toByte(), 0x00.toByte(),
            0xcd.toByte(), 0x00.toByte(),
            0x03.toByte(), 0x06.toByte(),
            Integer.parseInt(year.toString(), 16).toByte(),
            Integer.parseInt(month.toString(), 16).toByte(),
            Integer.parseInt(day.toString(), 16).toByte(),
            Integer.parseInt(hour.toString(), 16).toByte(),
            Integer.parseInt(minute.toString(), 16).toByte(),
            Integer.parseInt(second.toString(), 16).toByte()
        )

        val valueCrc16 = Crc16Utils.calcCrc16(ba).toString(16)
        val checkCode = Utils.ByteArraytoHex(Utils.hexStringToByteArray(valueCrc16).reversedArray())
            .replace(" ", "")

        val command =
            "02 10 00 cd 00 03 06 $year ${add0(month)} ${add0(day)} ${add0(hour)} ${add0(minute)} ${
                add0(second)
            } $checkCode"
        BusUtils.post(MsgWhat.SEND_COMMAND, command)
        activity.doDelayTask(1000) {
            BusUtils.post(MsgWhat.SEND_COMMAND, "02 10 00 c9 00 01 02 03 00 A3 C9")
        }
    }

    fun writeTime(hour: Int, minute: Int, second: Int) {
        val hour1 = if (hour < 10) "0$hour" else hour
        val minute1 = if (minute < 10) "0$minute" else minute
        val second1 = if (second < 10) "0$second" else second

        val cmd = "021000d0000204${hour1}${minute1}${second1}00"

        // 生成校验码
        val valueCrc16 = Crc16Utils.calcCrc16(Utils.hexStringToByteArray(cmd)).toString(16)
        val checkCode = Utils.ByteArraytoHex(Utils.hexStringToByteArray(valueCrc16).reversedArray())
            .replace(" ", "")

        val command = "$cmd$checkCode"

        LogUtils.d("YXD_", command)

        BusUtils.post(MsgWhat.SEND_COMMAND, command)
    }

    fun mark(originCmd: ByteArray): String {
        // 生成校验码
        val valueCrc16 = Crc16Utils.calcCrc16(originCmd).toString(16)
        val checkCode = Utils.ByteArraytoHex(Utils.hexStringToByteArray(valueCrc16).reversedArray())
            .replace(" ", "")

        val command = "${Utils.ByteArraytoHex(originCmd)}$checkCode"
        LogUtils.d("YXD_CMD", command)
        return command
    }

    /**
     * 写入数据
     * @param writeValue Float
     * @param h47 Int
     * @param g47 String
     */
    fun write(writeValue: Float, h47: Int, g47: String) {
        val first = "0210"
        // 00fe
        var second = h47.toBigDecimal().divide(2.toBigDecimal()).toInt().toString(16)
        while (second.length < 4) {
            second = "0$second"
        }
        // 0002
        var third =
            maxOf(1f, g47.toBigDecimal().divide(2.toBigDecimal()).toFloat()).toInt().toString()
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

        val valueCrc16 =
            Crc16Utils.calcCrc16(Utils.hexStringToByteArray("$first$second$third$fourth$fifth"))
                .toString(16)
        val newValue = Utils.ByteArraytoHex(Utils.hexStringToByteArray(valueCrc16).reversedArray())
            .replace(" ", "")
        val command = "$first$second$third$fourth$fifth$newValue"
        BusUtils.post(MsgWhat.SEND_COMMAND, command)
    }

    fun formatMsgContent(data: ByteArray): String? {
        return "HEX:" + Utils.ByteArraytoHex(data) + "  ASSCII:" + Utils.byteToASCII(data)
    }

    fun hex2Float(hex: String): Float {
        var returnValue = 0f
        returnValue = try {
            java.lang.Float.intBitsToFloat(Integer.valueOf(hex.replace(" ", ""), 16))
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
        Cmd.STARTED_FREQ5,
        Cmd.STARTED_FREQ6,
        Cmd.STARTED_FREQ7,
        Cmd.STARTED_FREQ8,
        Cmd.STARTED_FREQ9,
        Cmd.STARTED_FREQ10
    )

    private val markPointValueList = listOf(
        Cmd.STARTED_VALUE1,
        Cmd.STARTED_VALUE2,
        Cmd.STARTED_VALUE3,
        Cmd.STARTED_VALUE4,
        Cmd.STARTED_VALUE5,
        Cmd.STARTED_VALUE6,
        Cmd.STARTED_VALUE7,
        Cmd.STARTED_VALUE8,
        Cmd.STARTED_VALUE9,
        Cmd.STARTED_VALUE10,
    )

    private val markPointTestValue = listOf(
        Cmd.STARTED_TEST_VALUE1,
        Cmd.STARTED_TEST_VALUE2,
        Cmd.STARTED_TEST_VALUE3,
        Cmd.STARTED_TEST_VALUE4,
        Cmd.STARTED_TEST_VALUE5,
        Cmd.STARTED_TEST_VALUE6,
        Cmd.STARTED_TEST_VALUE7,
        Cmd.STARTED_TEST_VALUE8,
        Cmd.STARTED_TEST_VALUE9,
        Cmd.STARTED_TEST_VALUE10,
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

}