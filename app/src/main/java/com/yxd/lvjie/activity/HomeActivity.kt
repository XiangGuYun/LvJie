package com.yxd.lvjie.activity

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.text.TextUtils
import com.yp.baselib.annotation.Bus
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseActivity
import com.yp.baselib.utils.BusUtils
import com.yp.baselib.utils.ToastUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.base.MyApplication
import com.yxd.lvjie.bean.BtDevice
import com.yxd.lvjie.bean.MService
import com.yxd.lvjie.constant.*
import com.yxd.lvjie.constant.MsgWhat.DEVICE_DISCONNECT
import com.yxd.lvjie.dialog.ProjectDialog
import com.yxd.lvjie.service.BluetoothLeService
import com.yxd.lvjie.utils.CmdUtils
import com.yxd.lvjie.utils.CmdUtils.formatMsgContent
import com.yxd.lvjie.utils.CmdUtils.sliceByteArray
import kotlinx.android.synthetic.main.activity_device_manager.*
import org.greenrobot.eventbus.Subscribe


/**
 * 主页
 */
@Bus
@LayoutId(R.layout.activity_device_manager)
class HomeActivity : BaseActivity() {

    private var isConnectedDevice: Boolean = false
    private lateinit var currentDevAddress: String
    private lateinit var currentDevName: String

    private lateinit var notifyCharacteristic: BluetoothGattCharacteristic
    private lateinit var writeCharacteristic: BluetoothGattCharacteristic

    companion object {
        var listBonded = ArrayList<BtDevice>()
    }

    override fun init(bundle: Bundle?) {
        initView()
        // 处理连接蓝牙的服务和广播接收器
        doConnectReceiverAndService()
    }

    var cmdType = ""

    @Subscribe
    fun handle(msg: Message) {
        when (msg.what) {
            MsgWhat.CONNECT_DEVICE -> {
                BusUtils.post(MsgWhat.SHOW_DIALOG)
                doDelayTask(10000) {
                    BusUtils.post(MsgWhat.CONNECT_OVERTIME)
                }
                val device = msg.obj as BluetoothDevice
                connectDevice(device)
            }
            MsgWhat.NOTIFY -> {
                prepareBroadcastDataNotify(notifyCharacteristic)
            }
            MsgWhat.STOP_NOTIFY -> {
                stopBroadcastDataNotify(notifyCharacteristic)
            }
            MsgWhat.SEND_COMMAND -> {
                val text = msg.obj.toString()
                if (TextUtils.isEmpty(text)) {
                    return
                }
                val command = text.replace(" ", "")
                if (!Utils.isRightHexStr(command)) {
                    return
                }
                when (text) {
                    Cmd.STRENGTH_FREQ -> {
                        cmdType = "强度和频率"
                    }
                    Cmd.EQ -> {
                        cmdType = "电量"
                    }
                    Cmd.DEVICE_NO -> {
                        cmdType = "设备编号"
                    }
                    Cmd.IMEI -> {
                        cmdType = "IMEI"
                    }
                }
                val array = Utils.hexStringToByteArray(command)
                writeCharacteristic(writeCharacteristic, array)
            }
        }
    }

    private fun stopBroadcastDataNotify(characteristic: BluetoothGattCharacteristic) {
        val charaProp = characteristic.properties
        if (charaProp or BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
            BluetoothLeService.setCharacteristicNotification(characteristic, false)
        }
    }

    private fun prepareBroadcastDataNotify(characteristic: BluetoothGattCharacteristic) {
        val charaProp = characteristic.properties
        if (charaProp or BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
            BluetoothLeService.setCharacteristicNotification(characteristic, true)
        }
    }

    private fun initView() {
        val listDeviceManager = listOf(
            R.mipmap.shebeilianjie to "设备连接",
            R.mipmap.shebeiliebiao to "设备列表",
            R.mipmap.shishishuju to "实时数据",
            R.mipmap.lishishuju to "历史数据",
            R.mipmap.yinpincaiji to "音频采集",
            R.mipmap.chaizhuangjilu to "拆装记录",
            R.mipmap.weihujilu to "维护记录",
            R.mipmap.xunjianjilu to "巡检记录",
            R.mipmap.shebeishzhi to "设备设置"
        )

        rvDeviceManager.wrap.gridManager(4).rvMultiAdapter(
            listDeviceManager,
            { h, p ->
                h.iv(R.id.ivIcon).sIR(listDeviceManager[p].first)
                h.tv(R.id.tvName).txt(listDeviceManager[p].second)
                h.v(R.id.item).click {
                    when (p) {
                        0 -> goTo<DeviceConnectActivity>()
                        1 -> {
                        }
                        2 -> {
                            goTo<RealtimeDataActivity>()
                        }
                        8 -> {
                            goTo<SettingActivity>()
                        }
                    }
                }
            },
            {
                0
            }, R.layout.item_device_manager
        )

        val listStatAnalysis = listOf(
            R.mipmap.ditufenbu to "地图分布",
            R.mipmap.xiangguanjisuan to "相关计算",
            R.mipmap.gongshitongji to "工时统计",
            R.mipmap.loudiantongji to "漏电统计"
        )

        rvStatAnalysis.wrap.gridManager(4).rvMultiAdapter(
            listStatAnalysis,
            { h, p ->
                h.iv(R.id.ivIcon).sIR(listStatAnalysis[p].first)
                h.tv(R.id.tvName).txt(listStatAnalysis[p].second)
            },
            {
                0
            }, R.layout.item_device_manager
        )

        val listBusinessHandle = listOf(
            R.mipmap.shenloubaojing to "渗漏报警",
            R.mipmap.shenlougongdan to "渗漏工单",
            R.mipmap.budianrenwu to "布点任务"
        )

        rvBusinessHandle.wrap.gridManager(4).rvMultiAdapter(
            listBusinessHandle,
            { h, p ->
                h.iv(R.id.ivIcon).sIR(listBusinessHandle[p].first)
                h.tv(R.id.tvName).txt(listBusinessHandle[p].second)
            },
            {
                0
            }, R.layout.item_device_manager
        )

    }

    private fun doConnectReceiverAndService() {
        // 注册广播接收者，接收消息
        registerReceiver(mGattUpdateReceiver, Utils.makeGattUpdateIntentFilter())
        // 开启服务
        startService(Intent(applicationContext, BluetoothLeService::class.java))
    }

    private fun connectDevice(device: BluetoothDevice) {
        // 如果是连接状态，断开，重新连接
        if (BluetoothLeService.getConnectionState() !== BluetoothLeService.STATE_DISCONNECTED)
            BluetoothLeService.disconnect()
        currentDevName = device.name
        currentDevAddress = device.address
        BluetoothLeService.connect(device.address, device.name, this)
    }

    /**
     * BroadcastReceiver for receiving the GATT communication status
     */
    private val mGattUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Status received when connected to GATT Server
            intent.action?.logD("Cmd", "action is ")
            // 连接成功
            when (intent.action) {
                BluetoothLeService.ACTION_GATT_CONNECTED -> {
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
                    prepareData(BluetoothLeService.getSupportedGattServices())
                }
                BluetoothLeService.ACTION_GATT_DISCONNECTED -> {
                    // connect break (连接断开)
                    isConnectedDevice = false
                    "设备断开了连接".logD("CmdTag")
                    BusUtils.post(DEVICE_DISCONNECT)
                    listBonded.clear()
                    BusUtils.post(MsgWhat.CLEAR_BOUNDED_DEVICE)
                    if (actList.size == 1) {
                        ProjectDialog(this@HomeActivity).setInfo(
                            "设备已断开连接，请重新连接！", "确定",
                            false
                        ) {
                            it.dismiss()
                            goTo<DeviceConnectActivity>()
                        }.show()
                    }
                }
                BluetoothLeService.ACTION_DATA_AVAILABLE -> {
                    val extras = intent.extras
                    if (extras!!.containsKey(Constants.EXTRA_BYTE_VALUE)) {
                        if (extras.containsKey(Constants.EXTRA_BYTE_UUID_VALUE)) {
                            val array = intent.getByteArrayExtra(Constants.EXTRA_BYTE_VALUE)
                            formatMsgContent(array)?.logD("CmdTag", "收到了反馈：")
                            val hex = Utils.ByteArraytoHex(array).replace(" ", "")
                            when (cmdType) {
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
                                            Utils.byteToASCII(sliceByteArray(array, 3, 20))
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun prepareData(gattServices: List<BluetoothGattService>?) {
        if (gattServices == null) return
        val list: MutableList<MService> = java.util.ArrayList<MService>()
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

    private fun writeCharacteristic(characteristic: BluetoothGattCharacteristic, bytes: ByteArray) {
        // Writing the hexValue to the characteristics
        try {
            BluetoothLeService.writeCharacteristicGattDb(
                characteristic,
                bytes
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.toast(e.localizedMessage)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mGattUpdateReceiver)
        listBonded.clear()
        stopBroadcastDataNotify(notifyCharacteristic)
    }

    override fun onBackPressedSupport() {
        doExitVerify()
    }

}