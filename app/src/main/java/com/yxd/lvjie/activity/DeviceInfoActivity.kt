package com.yxd.lvjie.activity

import android.app.AlertDialog
import android.os.Bundle
import android.os.Message
import android.text.InputType
import com.yxd.baselib.annotation.Bus
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.utils.BusUtils
import com.yxd.baselib.utils.DialogUtils
import com.yxd.baselib.utils.KeyboardUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.bluetooth.Utils
import com.yxd.lvjie.constant.Cmd
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.constant.MsgWhat.CMD_DEVICE_INFO_6
import com.yxd.lvjie.helper.SPHelper
import com.yxd.lvjie.net.Req
import com.yxd.lvjie.utils.CmdUtils
import kotlinx.android.synthetic.main.activity_device_info.*
import org.greenrobot.eventbus.Subscribe

/**
 * 设备信息
 */
@Bus
@LayoutId(R.layout.activity_device_info)
class DeviceInfoActivity : ProjectBaseActivity() {

    fun ByteArray.byteArrayToASCII(): String? {
        return Utils.byteToASCII(this)
    }

    @Subscribe
    fun handle(msg: Message) {
        when (msg.what) {
            CMD_DEVICE_INFO_6->{
                val byteArray = msg.obj as ByteArray
                tvZhuKongBanYingJianXinXi.txt(byteArray.sliceArray(3..22).byteArrayToASCII())
                tvZhuKongBanRuanJianBanBen.txt(byteArray.sliceArray(23..42).byteArrayToASCII())
                tvSheBeiBianHao.txt(byteArray.sliceArray(43..62).byteArrayToASCII())
                tvIMEI.txt(byteArray.sliceArray(63..82).byteArrayToASCII())
                tvICCID.txt(byteArray.sliceArray(83..102).byteArrayToASCII())
                tvMuBiaoIpDiZhi.txt(byteArray.sliceArray(103..122).byteArrayToASCII())
                tvWuXianDianDaiMa.txt(byteArray.sliceArray(123..142).byteArrayToASCII())
            }
            MsgWhat.CHANGE_DEVICE_NAME_DONE->{
                dialogProgress.dismiss()
                "修改成功".toast()
                SPHelper.putEquipName(currentDeviceName)
                tvSheBeiBianHao.txt(SPHelper.getEquipName())
            }
        }
    }

    private var currentDeviceName: String = ""
    lateinit var dialogProgress: AlertDialog

    override fun init(bundle: Bundle?) {
        BusUtils.post(MsgWhat.SEND_COMMAND, Cmd.DEVICE_INFO)
        dialogProgress = DialogUtils.createProgressDialog(this, "正在修改...")
        val dialog = DialogUtils.createInputDialog(this,
            "",
            "请输入新的设备编号",
            "修改",
            "取消",
            {
                    dialog, text ->
                currentDeviceName = text
                Req.modifyDeviceName(currentDeviceName){
                    CmdUtils.changeDeviceName(text)
                }
                dialogProgress.show()
            },
            {
                    dialog ->
            },
            {
                it.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                it.setMaxLength(20)
            }
        )

        btnModify.click {
            dialog.show()
            KeyboardUtils.openKeyboardDelay(this, 500)
        }

    }

}