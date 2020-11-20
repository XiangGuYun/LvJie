package com.yxd.lvjie.constant

object MsgWhat {
    /**
     * 执行连接蓝牙设备
     */
    const val CONNECT_DEVICE = 0x001

    /**
     * 通知设备连接成功
     */
    const val CONNECT_SUCCESS = 0x002

    /**
     * 清空已连接的设备
     */
    const val CLEAR_BOUNDED_DEVICE = 0x003

    const val NOTIFY = 0x004

    const val STOP_NOTIFY = 0x005

    /**
     * 发送Hex指令
     */
    const val SEND_COMMAND = 0x006

    const val SHOW_DIALOG = 0x007

    const val HIDE_DIALOG = 0x008

    /**
     * 设备连接超时
     */
    const val CONNECT_OVERTIME = 0x009

    /**
     * 当设备断开连接
     */
    const val DEVICE_DISCONNECT = 0x010

    /**
     * 获取强度和频率
     */
    const val CMD_STRENGTH_FREQ = 0x100

    /**
     * 获取电量
     */
    const val CMD_EQ = 0x101

    /**
     * 获取设备编号
     */
    const val CMD_DEVICE_NO = 0x102

    /**
     * 获取IMEI
     */
    const val CMD_IMEI = 0x103
}