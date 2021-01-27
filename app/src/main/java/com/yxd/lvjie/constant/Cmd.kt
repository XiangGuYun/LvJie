package com.yxd.lvjie.constant

import com.yxd.lvjie.utils.CmdUtils

/**
 * 管理所有指令
 */
object Cmd {

    /**
     * 关机
     */
    const val POWER_OFF = "02 10 00 c9 00 01 02 01 00 A2 A9"

    /**
     * 寄存器表1
     */
    const val TABLE1 = "02 03 00 00 00 78 45 DB"

    /**
     * 寄存器表2
     */
    const val TABLE2 = "02 03 00 C8 00 13 85 CA"

    /**
     * 寄存器表3
     */
    const val TABLE3 = "02 03 00 fa 00 57 24 36"

    const val IMEI = "02 03 00 14 00 0a 85 FA"

    const val DEVICE_NO = "02 03 00 14 00 14 05 F2"

    /**
     * 电量
     */
    const val EQ = "02 03 00 d2 00 02 64 01"

    /**
     * 强度和频率
     */
    const val STRENGTH_FREQ = "02 03 00 fa 00 04 64 0b"

    /**
     * 原始强度
     */
    const val ORIGIN_STRENGTH = "02 03 01 00 00 02 C5 C4"

    /**
     * 自校准
     */
    const val AUTO_ADJUST = "02 10 00 c900 01 02 04 00 a1f9"

    /**
     * 获取历史数据
     */
    const val GET_HISTORY_DATA = "02 41 01 00 00 10 3D C6"

    /**
     * 设备唤醒时间
     */
    const val DEVICE_AWAKE_TIME = "02 03 00 d0 00 02 C5 C1"

    /**
     * 涉笔信息
     */
    const val DEVICE_INFO = "02 03 00 00 00 3c 45 E8"

    /**
     * 读取算法
     */
    const val READ_ARITHMETIC = "02 03 01 12 00 01 25 C0"

    /**
     * 写入算法1
     */
    val WRITE_ARITHMETIC1 =
        CmdUtils.mark(byteArrayOf(0x02, 0x10, 0x01, 0x12, 0x00, 0x01, 0x02, 0x01, 0x01))

    /**
     * 写入算法2
     */
    val WRITE_ARITHMETIC2 =
        CmdUtils.mark(byteArrayOf(0x02, 0x10, 0x01, 0x12, 0x00, 0x01, 0x02, 0x01, 0x02))

    /**
     * 写入算法3
     */
    val WRITE_ARITHMETIC3 =
        CmdUtils.mark(byteArrayOf(0x02, 0x10, 0x01, 0x12, 0x00, 0x01, 0x02, 0x01, 0x03))

    // 标定频率1
    val STARTED_FREQ1 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x15, 0x00, 0x02))

    // 标定值1
    val STARTED_VALUE1 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x17, 0x00, 0x02))

    // 标定测试值1 "02 03 01 19 00 02 14 03"
    val STARTED_TEST_VALUE1 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x19, 0x00, 0x02))

    // 标定频率2
    val STARTED_FREQ2 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x1b, 0x00, 0x02))
    // 标定值2
    val STARTED_VALUE2 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x1d, 0x00, 0x02))
    // 标定测试值2
    val STARTED_TEST_VALUE2 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x1f, 0x00, 0x02))

    // 标定频率3
    val STARTED_FREQ3 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x21, 0x00, 0x02))
    // 标定值3
    val STARTED_VALUE3 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x23, 0x00, 0x02))
    // 标定测试值3
    val STARTED_TEST_VALUE3 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x25, 0x00, 0x02))

    // 标定频率4
    val STARTED_FREQ4 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x27, 0x00, 0x02))
    // 标定值4
    val STARTED_VALUE4 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x29, 0x00, 0x02))
    // 标定测试值4
    val STARTED_TEST_VALUE4 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x2b, 0x00, 0x02))

    // 标定频率5
    val STARTED_FREQ5 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x2d, 0x00, 0x02))
    // 标定值5
    val STARTED_VALUE5 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x2f, 0x00, 0x02))
    // 标定测试值5
    val STARTED_TEST_VALUE5 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x31, 0x00, 0x02))

    // 标定频率6
    val STARTED_FREQ6 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x33, 0x00, 0x02))
    // 标定值6
    val STARTED_VALUE6= CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x35, 0x00, 0x02))
    // 标定测试值6
    val STARTED_TEST_VALUE6 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x37, 0x00, 0x02))

    // 标定频率7
    val STARTED_FREQ7 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x39, 0x00, 0x02))
    // 标定值7
    val STARTED_VALUE7 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x3b, 0x00, 0x02))
    // 标定测试值7
    val STARTED_TEST_VALUE7 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x3d, 0x00, 0x02))

    // 标定频率8
    val STARTED_FREQ8 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x3f, 0x00, 0x02))
    // 标定值3
    val STARTED_VALUE8 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x41, 0x00, 0x02))
    // 标定测试值3
    val STARTED_TEST_VALUE8 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x43, 0x00, 0x02))

    // 标定频率9
    val STARTED_FREQ9 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x45, 0x00, 0x02))
    // 标定值4
    val STARTED_VALUE9 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x47, 0x00, 0x02))
    // 标定测试值4
    val STARTED_TEST_VALUE9 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x49, 0x00, 0x02))

    // 标定频率10
    val STARTED_FREQ10 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x4b, 0x00, 0x02))
    // 标定值5
    val STARTED_VALUE10 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x4d, 0x00, 0x02))
    // 标定测试值5
    val STARTED_TEST_VALUE10 = CmdUtils.mark(byteArrayOf(0x02, 0x03, 0x01, 0x4f, 0x00, 0x02))
}