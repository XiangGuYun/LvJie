package com.yxd.lvjie.activity

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.os.Bundle
import android.os.Message
import com.yp.baselib.annotation.Bus
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.utils.BusUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.activity.HomeActivity.Companion.listBonded
import com.yxd.lvjie.base.MyApplication
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.bean.BtDevice
import com.yxd.lvjie.constant.GattAttributes
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.helper.BluetoothHelper
import com.yxd.lvjie.test.GattDetailActivity
import kotlinx.android.synthetic.main.activity_device_connect.*
import kotlinx.android.synthetic.main.header.*
import org.greenrobot.eventbus.Subscribe
import java.util.*
import kotlin.collections.ArrayList


/**
 * 连接蓝牙设备
 */
@Bus
@LayoutId(R.layout.activity_device_connect)
class DeviceConnectActivity : ProjectBaseActivity() {

    private var listUnBonded = ArrayList<BluetoothDevice>()

    private lateinit var helper: BluetoothHelper

    @Subscribe
    fun handle(msg:Message){
        when(msg.what){
            MsgWhat.CLEAR_BOUNDED_DEVICE->{
                rvConnectedDevices.update()
                tvNoBondedDevice.show()
            }
            MsgWhat.CONNECT_SUCCESS->{
                val device = msg.obj as BtDevice
                listBonded.add(device)
                listUnBonded.removeAll {
                  it.address == device.address
                }
                rvConnectedDevices.update()
                rvDisconnectDevices.update()
                tvNoBondedDevice.gone()
            }
        }
    }

    override fun init(bundle: Bundle?) {
        tvTitle.txt("设备连接").click {
            BusUtils.post(MsgWhat.NOTIFY)
        }

        tvSubTitle.show().txt("测试").click {
            BusUtils.post(MsgWhat.SEND_COMMAND, "02 03 00 fa 00 04 64 0b")
        }

        helper = BluetoothHelper.getInstance()

        if(helper.isBtEnabled()){
            switchBT.bg(R.drawable.bg_switch_on)
            viewEnable.show()
        } else {
            switchBT.bg(R.drawable.bg_switch)
            viewDisable.show()
        }
        switchBT.click {
            if (helper.isBtEnabled()) {
                helper.disableBt()
            } else {
                helper.enableBt(this)
            }
        }

        // 处理已配对的设备列表
        doBondedDeviceList()

        // 处理未配对的设备列表
        doUnBondedDeviceList()

        // 搜索附近的蓝牙设备
        discoverDevices()
    }

    private fun discoverDevices() {

        helper.registerSearchReceiver(this, { device ->
            if (device.name != null && listUnBonded.find { it.address == device.address } == null) {
                if(listBonded.find { it.address == device.address } == null){
                    listUnBonded.add(device)
                    rvDisconnectDevices.update()
                }
            }
        }, {
            if(loading.isStart){
                loading.stop()
            }
        }, {
            if(loading.isStart){
                loading.stop()
            }
            loading.start()
        }, onBtOn = {
            switchBT.bg(R.drawable.bg_switch_on)
            viewEnable.show()
            viewDisable.gone()
            helper.discoverDevices()
        }, onBtOff = {
            helper.cancelDiscoverDevices()
            switchBT.bg(R.drawable.bg_switch)
            viewEnable.gone()
            viewDisable.show()
            listUnBonded.clear()
            listBonded.clear()
            rvConnectedDevices.update()
            rvDisconnectDevices.update()
        })

        helper.discoverDevices()
    }

    private fun doUnBondedDeviceList() {
        rvDisconnectDevices.wrap.rvAdapter(
            listUnBonded,
            { h, p ->
                h.tv(R.id.tvName).txt(listUnBonded[p].name)
                h.tv(R.id.tvAddress).txt(listUnBonded[p].address)
                h.v(R.id.btnConn).click {
                    "正在连接".toast()
                    BusUtils.post(MsgWhat.CONNECT_DEVICE, listUnBonded[p])
                }
            }, R.layout.item_device1
        )
    }

    private fun doBondedDeviceList() {
        if (listBonded.isEmpty()) tvNoBondedDevice.show()

        rvConnectedDevices.wrap.rvAdapter(
            listBonded,
            { h, p ->
                h.tv(R.id.tvName).txt(listBonded[p].name)
                h.tv(R.id.tvAddress).txt(listBonded[p].address)
                h.itemClick {
                    (application as MyApplication).characteristic = BluetoothGattCharacteristic(
                        UUID.fromString(
                        GattAttributes.USR_SERVICE),-1,-1)
                    goTo<GattDetailActivity>()
//                    goTo<CharacteristicsActivity>("is_usr_service" to true)
                }
            }, R.layout.item_device
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        helper.unRegisterSearchReceiver(this)
        loading.stop()
    }

}