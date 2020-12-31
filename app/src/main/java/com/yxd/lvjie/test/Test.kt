package com.yxd.lvjie.test

import com.yxd.lvjie.bluetooth.Utils
import com.yxd.lvjie.utils.CmdUtils
import com.yxd.lvjie.utils.Crc16Utils
import java.math.BigDecimal

object Test {

    @JvmStatic
    fun main(args: Array<String>){
//        20 12 31 03 18 53
        val ba =  byteArrayOf(
            0x02.toByte(), 0x10.toByte(), 0x00.toByte(), 0xcd.toByte(), 0x00.toByte(),
            0x03.toByte(), 0x06.toByte(), 0x20.toByte(), 0x12.toByte(),
            0x31.toByte(), 0x3.toByte(), 0x18.toByte(), 0x53.toByte()
        )

        val valueCrc16 = Crc16Utils.calcCrc16(ba).toString(16)
        println(Integer.parseInt("20", 16))
    }


}