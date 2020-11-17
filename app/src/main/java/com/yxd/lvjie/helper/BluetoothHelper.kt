package com.yxd.lvjie.helper

import android.app.Activity
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log


/**
 * 传统蓝牙开发帮助类
 * @author YeXuDong
 *
 * 需要申请的权限
 * <!-- 应用使用蓝牙的权限 -->
 * <uses-permission android:name="android.permission.BLUETOOTH"/>
 * <!--启动设备发现或操作蓝牙设置的权限-->
 * <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
 *
 *  因为android 6.0之后采用新的权限机制来保护用户的隐私，如果我们设置的targetSdkVersion大于或等于23，
 *  则需要另外添加ACCESS_COARSE_LOCATION和ACCESS_FINE_LOCATION权限，否则，可能会出现搜索不到蓝牙设备的问题
 *  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 *  <uses-permission-sdk-23 android:name="android.permission.ACCESS_COARSE_LOCATION"/>
 */
class BluetoothHelper private constructor() {

    companion object {

        private var helper: BluetoothHelper? = null

        const val REQUEST_CODE = 1

        fun getInstance(): BluetoothHelper {
            if (helper == null) {
                helper = BluetoothHelper()
            }
            return helper!!
        }
    }

    private lateinit var receiver: SearchDevicesReceiver
    private val adapter = BluetoothAdapter.getDefaultAdapter()

    /**
     * 获取本机的蓝牙名称
     */
    fun getName(): String? {
        return adapter.name
    }

    /**
     * 设置本机的蓝牙名称
     * @param name String
     */
    fun setName(name: String) {
        adapter.name = name
    }

    /**
     * 获取本机的蓝牙地址
     * @return String?
     */
    fun getAddress(): String? {
        return adapter.address
    }

    /**
     * 根据蓝牙地址获取远程的蓝牙设备
     * @param address String
     * @return BluetoothDevice?
     */
    fun getRemoteDevice(address: String): BluetoothDevice? {
        return adapter.getRemoteDevice(address)
    }

    /**
     * 获取本地蓝牙适配器的状态
     * @return Int
     * STATE_OFF 已关闭
     * STATE_TURNING_ON 正在开启
     * STATE_ON 已开启
     * STATE_TURNING_OFF 正在关闭
     */
    fun getState(): Int {
        return adapter.state
    }

    /**
     * 是否已开启蓝牙
     * @return Boolean
     */
    fun isBtEnabled(): Boolean {
        return adapter.isEnabled
    }

    /**
     * 开启蓝牙
     * @param context Activity
     * @param requestCode Int
     */
    fun enableBt(context: Activity, requestCode: Int = REQUEST_CODE) {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        context.startActivityForResult(enableBtIntent, requestCode)
    }

    /**
     * 关闭蓝牙
     */
    fun disableBt(): Boolean {
        return adapter.disable()
    }

    /**
     * 查询已配对的蓝牙设备
     */
    fun getBondedDevices(): List<BluetoothDevice> {
        return adapter.bondedDevices.toList()
    }

    /**
     * 搜索附近未配对的蓝牙设备
     * 搜索设备是在异步进程中，通常会有12秒的时间来进行查询扫描，之后对每台发现的设备进行页面扫描，以检索其蓝牙名称。
     * 需要新增如下权限
     * <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
     * 在发现设备后系统会进行ACTION_FOUND的广播，因此，我们需要一个广播接收者来接收广播
     * 执行设备发现对于蓝牙适配器而言是一个非常繁重的操作过程，并且会消耗大量资源。
     * 在找到要连接的设备后，确保始终使用cancelDiscovery() 停止发现，然后再尝试连接。不应该在处于连接状态时执行发现操作。
     */
    fun discoverDevices() {
        if (adapter.isDiscovering) {
            //判断蓝牙是否正在扫描，如果是调用取消扫描方法；如果不是，则开始扫描
            adapter.cancelDiscovery();
        } else {
            adapter.startDiscovery();
        }
    }

    /**
     * 在找到要连接的设备后，确保始终使用cancelDiscovery() 停止发现，然后再尝试连接。
     * 不应该在处于连接状态时执行发现操作。
     */
    fun cancelDiscoverDevices() {
        if(adapter.isDiscovering)
            adapter.cancelDiscovery()
    }

    /**
     * 注册接收搜索蓝牙设备结果的接收器
     * @param activity Activity
     * @param callbackFound 当搜索到设备时回调
     * @param callbackDiscoveryFinished 搜索结束回调
     * @param callbackDiscoveryStarted 搜索开始回调
     */
    fun registerSearchReceiver(
        activity: Activity,
        callbackFound: (BluetoothDevice) -> Unit,
        callbackDiscoveryFinished: (() -> Unit)? = null,
        callbackDiscoveryStarted: (() -> Unit)? = null,
        onBtTurningOn: (() -> Unit)? = null,
        onBtTurningOff: (() -> Unit)? = null,
        onBtOn: (() -> Unit)? = null,
        onBtOff: (() -> Unit)? = null,
    ) {
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        receiver = SearchDevicesReceiver(
            callbackFound,
            callbackDiscoveryFinished,
            callbackDiscoveryStarted,
            onBtTurningOn,
            onBtTurningOff,
            onBtOn,
            onBtOff,
        )
        activity.registerReceiver(receiver, filter)
    }

    /**
     * 解除注册搜索蓝牙设备结果的接收器
     * @param activity Activity
     */
    fun unRegisterSearchReceiver(activity: Activity) {
        activity.unregisterReceiver(receiver)
    }

    /**
     * 判断当前设备是否能被其它蓝牙设备检测到
     */
    fun isDiscoverable(): Boolean {
        return adapter.scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE
    }

    /**
     * 启用可检测性
     * @param context Context
     * @param discoverableTime Int 默认情况下，设备将变为可检测到并持续 120 秒钟。
     * 您可以通过添加EXTRA_DISCOVERABLE_DURATION Intent Extra 来定义不同的持续时间。
     * 应用可以设置的最大持续时间为 3600 秒，值为 0 则表示设备始终可检测到。
     * 任何小于 0 或大于 3600 的值都会自动设为 120 秒
     */
    fun setDiscoverable(context: Context, discoverableTime: Int = 120) {
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, discoverableTime)
        context.startActivity(discoverableIntent)
    }

    /*
     蓝牙连接相关
     */

    /**
     * 连接设备
     * @param mac String
     */
    fun connectByMac(context: Context, mac: String) {
        if (adapter.isDiscovering) {
            adapter.cancelDiscovery()
        }
        val device = adapter.getRemoteDevice(mac)
        device.connectGatt(context, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                Log.d("BtTag", "onConnectionStateChange")
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d("BtTag", "BluetoothProfile.STATE_CONNECTED")
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                Log.d("BtTag", "onServicesDiscovered")
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                Log.d("BtTag", "onCharacteristicRead")
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?
            ) {
                Log.d("BtTag", "onCharacteristicChanged")
            }
        })
    }

    /**
     * 搜索蓝牙设备广播接收器
     * @property callbackFound 发现设备回调
     * @property callbackDiscoveryFinished 搜索结束回调
     * @property callbackDiscoveryStarted 搜索开始回调
     * @constructor
     */
    private class SearchDevicesReceiver(
        private val callbackFound: (BluetoothDevice) -> Unit,
        private val callbackDiscoveryFinished: (() -> Unit)? = null,
        private val callbackDiscoveryStarted: (() -> Unit)? = null,
        private val onBtTurningOn: (() -> Unit)? = null,
        private val onBtTurningOff: (() -> Unit)? = null,
        private val onBtOn: (() -> Unit)? = null,
        private val onBtOff: (() -> Unit)? = null,

        ) : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent!!.action
            Log.d("Test", "action is" + action)
            // 发现设备
            when (action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)) {
                        BluetoothAdapter.STATE_TURNING_ON -> {
                            onBtTurningOn?.invoke()
                        }
                        BluetoothAdapter.STATE_ON -> {
                            onBtOn?.invoke()
                        }
                        BluetoothAdapter.STATE_TURNING_OFF -> {
                            onBtTurningOff?.invoke()
                        }
                        BluetoothAdapter.STATE_OFF -> {
                            onBtOff?.invoke()
                        }
                    }
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice =
                        intent!!.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    callbackFound.invoke(device)
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.d("Test", "搜索结束")
                    callbackDiscoveryFinished?.invoke()
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Log.d("Test", "搜索开始")
                    callbackDiscoveryStarted?.invoke()
                }
            }

        }

    }

}