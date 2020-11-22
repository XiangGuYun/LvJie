package com.yxd.lvjie.test

import com.yxd.lvjie.bluetooth.Utils

object Test {

    @JvmStatic
    fun main(args: Array<String>){
//        val result = Crc16Utils.calcCrc16(Utils.hexStringToByteArray("021000fe00020442C80000"))
//        println(result.toString(16))

//        println(CmdUtils.hex2Float("3F800000C4CF"))

        println(Utils.ByteArraytoHex(Utils.hexStringToByteArray("A5E7").reversedArray()).replace(" ", ""))
    }


}