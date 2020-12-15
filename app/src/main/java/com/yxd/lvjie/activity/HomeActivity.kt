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
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.yxd.baselib.annotation.Bus
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.base.BaseActivity
import com.yxd.baselib.utils.BusUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.base.MyApplication
import com.yxd.lvjie.bean.BtDevice
import com.yxd.lvjie.bean.MService
import com.yxd.lvjie.bluetooth.Constants
import com.yxd.lvjie.bluetooth.GattAttributes
import com.yxd.lvjie.bluetooth.Utils
import com.yxd.lvjie.constant.*
import com.yxd.lvjie.constant.MsgWhat.DEVICE_DISCONNECT
import com.yxd.lvjie.dialog.ProjectDialog
import com.yxd.lvjie.service.BluetoothLeService
import com.yxd.lvjie.utils.CmdUtils
import com.yxd.lvjie.utils.CmdUtils.formatMsgContent
import com.yxd.lvjie.utils.CmdUtils.sliceByteArray
import kotlinx.android.synthetic.main.activity_device_manager.*
import org.greenrobot.eventbus.Subscribe
import java.math.RoundingMode


/**
 * 主页
 */
@Bus
@LayoutId(R.layout.activity_device_manager)
class HomeActivity : BaseActivity() {

    private var isConnectedDevice = false
    private lateinit var currentDevAddress: String
    private lateinit var currentDevName: String
    private lateinit var notifyCharacteristic: BluetoothGattCharacteristic
    private lateinit var writeCharacteristic: BluetoothGattCharacteristic

    private val listDeviceManager = listOf(
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

    private val listStatAnalysis = listOf(
        R.mipmap.ditufenbu to "地图分布",
        R.mipmap.xiangguanjisuan to "相关计算",
        R.mipmap.gongshitongji to "工时统计",
        R.mipmap.loudiantongji to "漏点统计"
    )

    private val listBusinessHandle = listOf(
        R.mipmap.shenloubaojing to "渗漏报警",
        R.mipmap.shenlougongdan to "渗漏工单",
        R.mipmap.budianrenwu to "布点任务"
    )

    companion object {

        /**
         * 已绑定的设备
         */
        var listBonded = ArrayList<BtDevice>()

        /**
         * 收到的命令类型
         */
        var cmdType = ""
    }

    override fun init( bundle: Bundle?) {
        initView()
        // 处理连接蓝牙的服务和广播接收器
        doConnectReceiverAndService()
    }

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
                cmdType = when (text) {
                    Cmd.DEVICE_AWAKE_TIME -> "设备唤醒时间"
                    Cmd.STRENGTH_FREQ -> "强度和频率"
                    Cmd.EQ -> "电量"
                    Cmd.DEVICE_NO -> "设备编号"
                    Cmd.IMEI -> "IMEI"
                    Cmd.ORIGIN_STRENGTH -> "原始强度"
                    Cmd.STARTED_FREQ1 -> "标准频率1"
                    Cmd.STARTED_FREQ2 -> "标准频率2"
                    Cmd.STARTED_FREQ3 -> "标准频率3"
                    Cmd.STARTED_FREQ4 -> "标准频率4"
                    Cmd.STARTED_FREQ5 -> "标准频率5"
                    Cmd.STARTED_VALUE1 -> "标准值1"
                    Cmd.STARTED_VALUE2 -> "标准值2"
                    Cmd.STARTED_VALUE3 -> "标准值3"
                    Cmd.STARTED_VALUE4 -> "标准值4"
                    Cmd.STARTED_VALUE5 -> "标准值5"
                    Cmd.STARTED_TEST_VALUE1 -> "标准测试值1"
                    Cmd.STARTED_TEST_VALUE2 -> "标准测试值2"
                    Cmd.STARTED_TEST_VALUE3 -> "标准测试值3"
                    Cmd.STARTED_TEST_VALUE4 -> "标准测试值4"
                    Cmd.STARTED_TEST_VALUE5 -> "标准测试值5"
                    else -> ""
                }
                val array = Utils.hexStringToByteArray(command)
                writeCharacteristic(writeCharacteristic, array)
            }
        }
    }

    private fun initView() {
        rvDeviceManager.wrap.gridManager(4).generate(
            listDeviceManager,
            { h, p, item ->
                h.iv(R.id.ivIcon).sIR(item.first)
                h.tv(R.id.tvName).txt(item.second)
                h.v(R.id.item).click {
                    when (item.second) {
                        "设备连接" -> goTo<DeviceConnectActivity>()
                        "设备列表" -> goTo<DeviceListActivity>()
                        "实时数据" -> goTo<RealtimeDataActivity>()
                        "设备设置" -> goTo<SettingActivity>()
                        "历史数据" -> goTo<HistoryDataActivity>()
                    }
                }
            }, null, R.layout.item_device_manager
        )

        rvStatAnalysis.wrap.gridManager(4).generate(
            listStatAnalysis,
            { h, p, item ->
                h.iv(R.id.ivIcon).sIR(item.first)
                h.tv(R.id.tvName).txt(item.second)
                h.itemClick {
                    when(item.second){
                        "地图分布"->goTo<MapActivity>()
                    }
                }
            }, null, R.layout.item_device_manager
        )

        rvBusinessHandle.wrap.gridManager(4).generate(
            listBusinessHandle,
            { h, p, item ->
                h.iv(R.id.ivIcon).sIR(item.first)
                h.tv(R.id.tvName).txt(item.second)
            }, null, R.layout.item_device_manager
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
        if (BluetoothLeService.getConnectionState() != BluetoothLeService.STATE_DISCONNECTED)
            BluetoothLeService.disconnect()
        currentDevName = device.name
        currentDevAddress = device.address
        BluetoothLeService.connect(device.address, device.name, this)
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

    /**
     * 接收GATT通讯状态的广播接收器
     */
    private val mGattUpdateReceiver = object : BroadcastReceiver() {
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
                    // 接收蓝牙设备返回的数据
                    val extras = intent.extras
                    if (extras!!.containsKey(Constants.EXTRA_BYTE_VALUE)) {
                        if (extras.containsKey(Constants.EXTRA_BYTE_UUID_VALUE)) {
                            val array = intent.getByteArrayExtra(Constants.EXTRA_BYTE_VALUE)
                            formatMsgContent(array)?.logD("YXD_Cmd", "收到了反馈：")
                            val hex = Utils.ByteArraytoHex(array).replace(" ", "")
                            when (cmdType) {
                                "设备唤醒时间"->{
                                    BusUtils.post(
                                        MsgWhat.CMD_DEVICE_AWAKE_TIME,
                                        ""
                                    )
                                }
                                "强度和频率" -> {
                                    BusUtils.post(
                                        MsgWhat.CMD_STRENGTH_FREQ,
                                        CmdUtils.decodeStrengthAndFrequency(hex)
                                    )
                                }
                                "原始强度"->{
                                    BusUtils.post(
                                        MsgWhat.ORIGIN_STRENGTH,
                                        CmdUtils.hex2Float(hex.substring(6, 14)).toBigDecimal().divide(
                                            1.toBigDecimal(), 2, RoundingMode.HALF_UP
                                        )
                                    )
                                }
                                "电量" -> {
                                    BusUtils.post(
                                        MsgWhat.CMD_EQ,
                                        CmdUtils.decodeElectricQuantity(hex).toInt()
                                    )
                                }
                                "设备编号" -> {
                                    BusUtils.post(MsgWhat.CMD_DEVICE_NO, String(array.sliceArray(23..37)))
                                }
                                "IMEI" -> {
                                    if (array.size > 23) {
                                        BusUtils.post(
                                            MsgWhat.CMD_IMEI,
                                            Utils.byteToASCII(sliceByteArray(array, 3, 20))
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

    private fun prepareData(gattServices: List<BluetoothGattService>?) {
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

    private fun writeCharacteristic(characteristic: BluetoothGattCharacteristic, bytes: ByteArray) {
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
        listBonded.clear()
//        stopBroadcastDataNotify(notifyCharacteristic)
        unregisterReceiver(mGattUpdateReceiver)
    }

    override fun onBackPressedSupport() {
        doExitVerify()
    }

}