package com.yxd.lvjie.activity

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.yp.baselib.annotation.LayoutId
import com.yxd.lvjie.R
import com.yxd.lvjie.base.ProjectBaseActivity
import com.yxd.lvjie.bean.DeviceDetailBean
import com.yxd.lvjie.bean.DeviceEditBean
import com.yxd.lvjie.net.Req
import kotlinx.android.synthetic.main.activity_device_detail.*
import kotlinx.android.synthetic.main.header.*

/**
 * 设备详情
 */
@LayoutId(R.layout.activity_device_detail)
class DeviceDetailActivity : ProjectBaseActivity() {

    private lateinit var list: ArrayList<Pair<String, String>>
    private var isEditable = false

    private lateinit var currentData: DeviceDetailBean.Data

    private lateinit var etCompany: EditText
    private lateinit var etLonLat: EditText
    private lateinit var etInstallPerson: EditText
    private lateinit var tvInstallTime: TextView
    private lateinit var etPipeCaliber: EditText
    private lateinit var etPipeMaterial: EditText
    private lateinit var etValveLocation: EditText
    private lateinit var etValveNo: EditText

    override fun init(bundle: Bundle?) {

        val id = extraStr("id")

        tvSubTitle.show().txt("编辑").click {
            if (tvSubTitle.str == "编辑") {
                tvSubTitle.txt("保存")
                isEditable = true
                rvDeviceDetail.update()
            } else {
//                Req.editDevice(
//                    DeviceEditBean(
//                        company = etCompany.str,
//                        equipModel = etEquipModel.str,
//                        equipNo = etEquipNo.str,
//                        id = id.toInt(),
//                        installMode = currentInstallMode,
//                        installPattern = currentInstallPattern,
//                        installPerson = etInstallPerson.str,
//                        installTime = etInstallTime.str.reverseFmtDate("yyyy-MM-dd"),
//                        latitude = etLatitude.str.toInt(),
//                        longitude = etLongitude.str.toInt(),
//                        pipeCaliber = etPipeCaliber.str,
//                        pipeMaterial = etPipeMaterial.str,
//                        valveLocation = etValveLocation.str,
//                        valveNo = etValveNo.str,
//                    )
//                ) {
//                    if (it.code == 0) {
//                        "保存成功".toast()
//                        isEditable = false
//                        rvDeviceDetail.update()
//                        tvSubTitle.txt("编辑")
//                    } else {
//                        "保存失败,${it.message}".toast()
//                    }
//                }
                currentData.apply {
                    longitude = etLonLat.str.split(",")[0].trim()
                    latitude = etLonLat.str.split(",")[1].trim()
                    valveNo = etValveNo.str.trim()
                    valveLocation = etValveLocation.str.trim()
                    pipeMaterial = etPipeMaterial.str.trim()
                    pipeCaliber = etPipeCaliber.str.trim()
                    installPerson = etInstallPerson.str.trim()
                    company = etCompany.str.trim()
                    installTime = tvInstallTime.str.reverseFmtDate("yyyy-MM-dd")
                }
                list.clear()
                list.addAll(
                    listOf(
                        "设备位置：" to "设备位置：${currentData.longitude}, ${currentData.latitude}",
                        "阀门编号：" to "阀门编号：${currentData.valveNo}",
                        "阀门位置：" to "阀门位置：${currentData.valveLocation}",
                        "管道材质：" to "管道材质：${currentData.pipeMaterial}",
                        "管道口径：" to "管道口径：${currentData.pipeCaliber}",
                        "安装人员：" to "安装人员：${currentData.installPerson}",
                        "公        司：" to "公        司：${currentData.company}",
                        "安装时间：" to "安装时间：${currentData.installTime?.fmtDate("yyyy-MM-dd")}",
                        "安装模式：" to "安装模式：${getPattern(currentData.installPattern)}",
                        "安装方式：" to "安装方式：${getModel(currentData.installMode)}"
                    )
                )
                tvSubTitle.txt("编辑")
                isEditable = false
                rvDeviceDetail.update()
            }
        }

        Req.getDeviceDetail(id) {
            currentData = it.data

            tvXingHao.txt("型号：${currentData.equipModel}")

            tvDianLiang.txt("电量：${currentData.power}")

            tvSheBeiBianHao.txt("设备编号：${currentData.equipNo} ")

            tvShuJuGengXinShiJian.txt("数据更新时间：${currentData.dataUpdateTime?.fmtDate("yyyy-MM-dd HH:mm:ss")}")
            list = arrayListOf(
                "设备位置：" to "设备位置：${currentData.longitude}, ${currentData.latitude}",
                "阀门编号：" to "阀门编号：${currentData.valveNo}",
                "阀门位置：" to "阀门位置：${currentData.valveLocation}",
                "管道材质：" to "管道材质：${currentData.pipeMaterial}",
                "管道口径：" to "管道口径：${currentData.pipeCaliber}",
                "安装人员：" to "安装人员：${currentData.installPerson}",
                "公        司：" to "公        司：${currentData.company}",
                "安装时间：" to "安装时间：${currentData.installTime?.fmtDate("yyyy-MM-dd")}",
                "安装模式：" to "安装模式：${getPattern(currentData.installPattern)}",
                "安装方式：" to "安装方式：${getModel(currentData.installMode)}"
            )
            
            rvDeviceDetail.wrap.generate(
                list,
                { h, p, item ->
                    h.tv(R.id.tv1).txt(if (isEditable) item.first else item.second)
                    if (h.vNull(R.id.tv2) != null && h.v(R.id.tv2) is EditText) {
                        val et = h.et(R.id.tv2)
                        when(h.tv(R.id.tv1).str){
                            "设备位置：" -> {
                                etLonLat = et
                                etLonLat.txt("${currentData.longitude},${currentData.latitude}")
                            }
                            "阀门编号：" -> {
                                etValveNo = et
                                etValveNo.txt(currentData.valveNo)
                            }
                            "阀门位置：" -> {
                                etValveLocation = et
                                etValveLocation.txt(currentData.valveLocation)
                            }
                            "管道材质：" -> {
                                etPipeMaterial = et
                                etPipeMaterial.txt(currentData.pipeMaterial)
                            }
                            "管道口径：" -> {
                                etPipeCaliber = et
                                etPipeCaliber.txt(currentData.pipeCaliber)
                            }
                            "安装人员：" -> {
                                etInstallPerson = et
                                etInstallPerson.txt(currentData.installPerson)
                            }
                            "公        司：" -> {
                                etCompany = et
                                etCompany.txt(currentData.company)
                            }
                        }
                    }
                    if(h.tvNull(R.id.tv3) != null){
                        val tv = h.tv(R.id.tv3)
                        when(h.tv(R.id.tv1).str){
                            "安装时间：" -> {
                                tvInstallTime = tv
                                tvInstallTime.txt(currentData.installTime?.fmtDate("yyyy-MM-dd"))
                                h.v(R.id.flTV3).click {
                                    "选择时间".toast()
                                }
                            }
                            "安装模式：" -> {
                                tv.txt(getPattern(currentData.installPattern))
                            }
                            "安装方式：" -> {
                                tv.txt(getModel(currentData.installMode))
                            }
                        }
                       
                    }
                },
                {
                    val i = when {
                        // 只读文本框
                        !isEditable -> 0
                        // 可编辑文本框
                        it !in 7..9 -> 1
                        // 选项文本框
                        else -> 2
                    }
                    i
                },
                R.layout.item_deivie_detail1,
                R.layout.item_deivie_detail2,
                R.layout.item_deivie_detail3
            )
        }


    }

    /**
     * 0-固定点 1-流动点 2-观察点
     * @param installPattern Int?
     * @return String
     */
    private fun getPattern(installPattern: Int?): String {
        return when (installPattern) {
            0 -> "固定点"
            1 -> "流动点"
            else -> "观察点"
        }
    }

    /**
     * 0-直接吸附阀门 1-直接吸附管道 2-抱箍固定安装管道 3-其他
     * @param model Int?
     * @return String
     */
    private fun getModel(model: Int?): String {
        return when (model) {
            0 -> "直接吸附阀门"
            1 -> "直接吸附管道"
            2 -> "抱箍固定安装管道"
            else -> "其他"
        }
    }

}