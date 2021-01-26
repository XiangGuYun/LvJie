package com.yxd.lvjie

import com.yxd.lvjie.bluetooth.Utils
import com.yxd.lvjie.constant.Cmd
import com.yxd.lvjie.utils.CmdUtils
import com.yxd.lvjie.utils.Crc16Utils
import java.math.BigDecimal
import java.util.*

class Test34 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
//            println(Utils.ByteArraytoHex("lvjie".toByteArray()))
//            println(Utils.hexStringToString("4C 56 4A 49 45".replace(" ", "")))
            println(BigDecimal(6.1234).setScale(1, BigDecimal.ROUND_HALF_UP))
        }
    }

}