package com.yxd.lvjie.base

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yxd.baselib.base.BaseActivity
import com.yxd.baselib.utils.BusUtils
import com.yxd.lvjie.activity.DeviceConnectActivity
import com.yxd.lvjie.activity.HomeActivity
import com.yxd.lvjie.bean.BtDevice
import com.yxd.lvjie.bean.MService
import com.yxd.lvjie.bluetooth.Constants
import com.yxd.lvjie.bluetooth.GattAttributes
import com.yxd.lvjie.bluetooth.Utils
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.dialog.ProjectDialog
import com.yxd.lvjie.service.BluetoothLeService
import com.yxd.lvjie.utils.CmdUtils

abstract class BluetoothActivity : BaseActivity() {

    var isConnectedDevice = false
    lateinit var currentDevAddress: String
    lateinit var currentDevName: String
    lateinit var notifyCharacteristic: BluetoothGattCharacteristic
    lateinit var writeCharacteristic: BluetoothGattCharacteristic

    fun doConnectReceiverAndService() {
        // 注册广播接收者，接收消息
        registerReceiver(mGattUpdateReceiver, Utils.makeGattUpdateIntentFilter())
        // 开启服务
        startService(Intent(applicationContext, BluetoothLeService::class.java))
    }

    fun connectDevice(device: BluetoothDevice) {
        // 如果是连接状态，断开，重新连接
        if (BluetoothLeService.getConnectionState() != BluetoothLeService.STATE_DISCONNECTED)
            BluetoothLeService.disconnect()
        currentDevName = device.name
        currentDevAddress = device.address
        BluetoothLeService.connect(device.address, device.name, this)
    }

    fun stopBroadcastDataNotify(characteristic: BluetoothGattCharacteristic) {
        val charaProp = characteristic.properties
        if (charaProp or BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
            BluetoothLeService.setCharacteristicNotification(characteristic, false)
        }
    }

    fun prepareBroadcastDataNotify(characteristic: BluetoothGattCharacteristic) {
        val charaProp = characteristic.properties
        if (charaProp or BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
            BluetoothLeService.setCharacteristicNotification(characteristic, true)
        }
    }

    /**
     * 接收GATT通讯状态的广播接收器
     */
    val mGattUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // 连接到GATT服务器时收到的状态
            when (intent.action) {
                BluetoothLeService.ACTION_GATT_CONNECTED -> {
                    // 连接成功
                    isConnectedDevice = true
                    BusUtils.post(MsgWhat.HIDE_DIALOG)
                    BusUtils.post(
                        MsgWhat.CONNECT_SUCCESS,
                        BtDevice(currentDevName, currentDevAddress)
                    )
                    // 搜索服务
                    BluetoothLeService.discoverServices()
                }
                BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED -> {
                    // 搜索到GATT服务
                    prepareData(BluetoothLeService.getSupportedGattServices())
                }
                BluetoothLeService.ACTION_GATT_DISCONNECTED -> {
                    // 连接断开
                    isConnectedDevice = false
                    "设备断开了连接".logD("YXD_Cmd")
                    BusUtils.post(MsgWhat.DEVICE_DISCONNECT)
                    HomeActivity.listBonded.clear()
                    BusUtils.post(MsgWhat.CLEAR_BOUNDED_DEVICE)
                    if (actList.size == 1) {
                        ProjectDialog(this@BluetoothActivity).setInfo(
                            "设备已断开连接，请重新连接！", "确定",
                            false
                        ) {
                            it.dismiss()
                            goTo<DeviceConnectActivity>()
                        }.show()
                    }
                }
                BluetoothLeService.ACTION_DATA_AVAILABLE -> {
                    // 接收蓝牙设备返回的数据
                    val extras = intent.extras
                    if (extras!!.containsKey(Constants.EXTRA_BYTE_VALUE)) {
                        if (extras.containsKey(Constants.EXTRA_BYTE_UUID_VALUE)) {
                            val array = intent.getByteArrayExtra(Constants.EXTRA_BYTE_VALUE)
                            CmdUtils.formatMsgContent(array)?.logD("YXD_Cmd", "收到了反馈：")
                            val hex = Utils.ByteArraytoHex(array).replace(" ", "")
                            when (HomeActivity.cmdType) {
                                "强度和频率" -> {
                                    BusUtils.post(
                                        MsgWhat.CMD_STRENGTH_FREQ,
                                        CmdUtils.decodeStrengthAndFrequency(hex)
                                    )
                                }
                                "电量" -> {
                                    BusUtils.post(
                                        MsgWhat.CMD_EQ,
                                        CmdUtils.decodeElectricQuantity(hex).toInt()
                                    )
                                }
                                "设备编号" -> {
                                    BusUtils.post(MsgWhat.CMD_DEVICE_NO, Utils.byteToASCII(array))
                                }
                                "IMEI" -> {
                                    if (array.size > 23) {
                                        BusUtils.post(
                                            MsgWhat.CMD_IMEI,
                                            Utils.byteToASCII(CmdUtils.sliceByteArray(array, 3, 20))
                                        )
                                    }
                                }
                                "标准频率1", "标准频率2", "标准频率3", "标准频率4", "标准频率5" -> {
                                    BusUtils.post(
                                        MsgWhat.CMD_STARTED_FREQ,
                                        CmdUtils.hex2Float(hex.substring(6, 14))
                                    )
                                }
                                "标准值1", "标准值2", "标准值3", "标准值4", "标准值5" -> {
                                    BusUtils.post(
                                        MsgWhat.CMD_STARTED_VALUE,
                                        CmdUtils.hex2Float(hex.substring(6, 14))
                                    )
                                }
                                "标准测试值1", "标准测试值2", "标准测试值3", "标准测试值4", "标准测试值5" -> {
                                    BusUtils.post(
                                        MsgWhat.CMD_STARTED_TEST_VALUE,
                                        CmdUtils.hex2Float(hex.substring(6, 14))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun prepareData(gattServices: List<BluetoothGattService>?) {
        if (gattServices == null) return
        val list: MutableList<MService> = java.util.ArrayList()
        for (gattService in gattServices) {
            val uuid = gattService.uuid.toString()
            if (uuid == GattAttributes.GENERIC_ACCESS_SERVICE || uuid == GattAttributes.GENERIC_ATTRIBUTE_SERVICE)
                continue
            val name = GattAttributes.lookup(gattService.uuid.toString(), "UnknownService")
            val mService = MService(name, gattService)
            list.add(mService)
        }
        val myApp = (application as MyApplication)
        myApp.services = list
        myApp.characteristics = list[0].service.characteristics
        val characteristics = myApp.characteristics
        for (c in characteristics) {
            if (Utils.getPorperties(this, c) == "Notify") {
                notifyCharacteristic = c
                BluetoothLeService.requestMtu(512)
                continue
            }
            if (Utils.getPorperties(this, c) == "Write") {
                writeCharacteristic = c
                writeCharacteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE;
                continue
            }
        }
    }

    fun writeCharacteristic(characteristic: BluetoothGattCharacteristic, bytes: ByteArray) {
        // 将十六进制值写入特征符
        try {
            BluetoothLeService.writeCharacteristicGattDb(
                characteristic,
                bytes
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopBroadcastDataNotify(notifyCharacteristic)
        unregisterReceiver(mGattUpdateReceiver)
    }


}