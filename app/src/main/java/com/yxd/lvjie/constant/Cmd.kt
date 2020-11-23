package com.yxd.lvjie.constant

/**
 * 管理所有指令
 */
object Cmd {

    const val IMEI = "02 03 00 14 00 0a 85 FA"

    const val DEVICE_NO = "02 03 00 14 00 14 05 F2"

    const val EQ = "02 03 00 d2 00 02 64 01"

    const val STRENGTH_FREQ = "02 03 00 fa 00 04 64 0b"

    /**
     * 原始强度
     */
    const val ORIGIN_STRENGTH = "02 03 01 00 00 02 C5 C4"

    /**
     * 自校准
     */
    const val AUTO_ADJUST = "02 10 00 c900 01 02 04 00 a1f9"


    // 标定频率1
    const val STARTED_FREQ1 = "02 03 01 08 00 02 44 06"
    // 标定值1
    const val STARTED_VALUE1= "02 03 01 0a 00 02 E5 C6"
    // 标定测试值1
    const val STARTED_TEST_VALUE1 = "02 03 01 0c 00 02 05 C7"
    // 标定频率2
    const val STARTED_FREQ2 = "02 03 01 0e 00 02 A4 07"
    // 标定值2
    const val STARTED_VALUE2 = "02 03 01 10 00 02 C4 01"
    // 标定测试值2
    const val STARTED_TEST_VALUE2 = "02 03 01 12 00 02  65 C1"
    // 标定频率3
    const val STARTED_FREQ3 = "02 03 01 14 00 02  85 C0"
    // 标定值3
    const val STARTED_VALUE3 = "02 03 01 16 00 02 24 00"
    // 标定测试值3
    const val STARTED_TEST_VALUE3 = "02 03 01 18 00 02 45 C3"
    // 标定频率4
    const val STARTED_FREQ4 = "02 03 01 1a 00 02 E4 03"
    // 标定值4
    const val STARTED_VALUE4 = "02 03 01 1c 00 02 04 02"
    // 标定测试值4
    const val STARTED_TEST_VALUE4 = "02 03 01 1e 00 02 A5 C2"
    // 标定频率5
    const val STARTED_FREQ5 = "02 03 01 20 00 02 C4 0E"
    // 标定值5
    const val STARTED_VALUE5 = "02 03 01 22 00 02 65 CE"
    // 标定测试值5
    const val STARTED_TEST_VALUE5 = "02 03 01 24 00 02 85 CF"
}