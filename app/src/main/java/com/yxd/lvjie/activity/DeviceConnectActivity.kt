package com.yxd.lvjie.activity

import android.app.ProgressDialog
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.os.Message
import com.yxd.baselib.annotation.Bus
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.utils.BusUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.activity.HomeActivity.Companion.listBonded
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.bean.BtDevice
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.helper.BluetoothHelper
import com.yxd.lvjie.utils.CmdUtils
import kotlinx.android.synthetic.main.activity_device_connect.*
import kotlinx.android.synthetic.main.header.*
import org.greenrobot.eventbus.Subscribe
import kotlin.collections.ArrayList


/**
 * 连接蓝牙设备
 */
@Bus
@LayoutId(R.layout.activity_device_connect)
class DeviceConnectActivity : ProjectBaseActivity() {

    private var listUnBonded = ArrayList<BluetoothDevice>()

    private lateinit var helper: BluetoothHelper

    private lateinit var pd:ProgressDialog

    @Subscribe
    fun handle(msg:Message){
        when(msg.what){
            MsgWhat.SHOW_DIALOG ->{
                pd.show()
            }
            MsgWhat.HIDE_DIALOG ->{
                pd.dismiss()
            }
            MsgWhat.CONNECT_OVERTIME ->{
                if(pd.isShowing){
                    pd.dismiss()
                    "连接超时".toast()
                }
            }
            MsgWhat.CLEAR_BOUNDED_DEVICE->{
                if(pd.isShowing){
                    pd.dismiss()
                    "连接失败，请重试".toast()
                }
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
            CmdUtils.sendCmdForMarkPoint(this, 0)
        }

        tvSubTitle.show().txt("重新搜索").click {
            if(!helper.isDiscovering()){
                listUnBonded.clear()
                rvDisconnectDevices.update()
                helper.discoverDevices()
            }
        }

        pd = ProgressDialog(this)
        pd.setMessage("正在连接...")
        pd.setCanceledOnTouchOutside(false)

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
        rvDisconnectDevices.wrap.generate(listUnBonded,
            {
                h, i, it ->
                h.tv(R.id.tvName).txt(it.name)
                h.tv(R.id.tvAddress).txt(it.address)
                h.v(R.id.btnConn).click { v->
                    helper.cancelDiscoverDevices()
                    BusUtils.post(MsgWhat.CONNECT_DEVICE, it)
                }
            }, null, R.layout.item_device1)
    }

    private fun doBondedDeviceList() {
        if (listBonded.isEmpty()) tvNoBondedDevice.show()

        rvConnectedDevices.wrap.generate(
            listBonded,
            { h, p, it ->
                h.tv(R.id.tvName).txt(listBonded[p].name)
                h.tv(R.id.tvAddress).txt(listBonded[p].address)
                h.itemClick {
//                    (application as MyApplication).characteristic = BluetoothGattCharacteristic(
//                        UUID.fromString(
//                        GattAttributes.USR_SERVICE),-1,-1)
//                    goTo<GattDetailActivity>()
//                    goTo<CharacteristicsActivity>("is_usr_service" to true)
                }
            }, null, R.layout.item_device
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if(helper.isDiscovering()){
            helper.cancelDiscoverDevices()
        }
        helper.unRegisterSearchReceiver(this)
        loading.stop()
    }

}