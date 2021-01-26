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

    const val SHOW_DIALOG1 = 0x0071

    const val HIDE_DIALOG1 = 0x0081

    /**
     * 更新经纬度
     */
    const val UPDATE_LON_LAT = 0x009

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

    /**
     * 标准频率
     */
    const val CMD_STARTED_FREQ = 0x104

    /**
     * 标准值
     */
    const val CMD_STARTED_VALUE = 0x105

    /**
     * 标准测试值
     */
    const val CMD_STARTED_TEST_VALUE = 0x106

    /**
     * 原始强度
     */
    const val ORIGIN_STRENGTH = 0x107

    /**
     * 设备唤醒时间
     */
    const val CMD_DEVICE_AWAKE_TIME = 0x108

    /**
     * 获取设备信息
     */
    const val CMD_DEVICE_INFO_6 = 0x114

    /**
     * 同步历史数据
     */
    const val CMD_HISTORY_DATA = 0x115

    /**
     * 设备名
     */
    const val CMD_DEVICE_NAME = 0x116

    /**
     * 读取算法
     */
    const val CMD_READ_ARTH = 0x117

    /**
     * 寄存器表1
     */
    const val CMD_TABLE1 = 0x118

    /**
     * 寄存器表2
     */
    const val CMD_TABLE2 = 0x119

    /**
     * 寄存器表3
     */
    const val CMD_TABLE3= 0x120

    /**
     * 更新设备列表
     */
    const val UPDATE_DEVICE_LIST = 0x121

    /**
     * 写入时间完成
     */
    const val WRITE_TIME_DONE = 0x122

    /**
     * 更新设备模式
     */
    const val CHANGE_DEVICE_PATTERN = 0x123

    /**
     * 完成修改设备名
     */
    const val CHANGE_DEVICE_NAME_DONE = 0x124
}