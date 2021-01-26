package com.yxd.lvjie.activity

import android.graphics.Color
import android.os.Bundle
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.LocationSource
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.maps2d.model.MyLocationStyle
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeResult
import com.yxd.baselib.annotation.LayoutId
import com.yxd.baselib.base.BaseActivity
import com.yxd.baselib.utils.BusUtils
import com.yxd.lvjie.R
import com.yxd.lvjie.constant.MsgWhat
import com.yxd.lvjie.dialog.LonLatDialog
import com.yxd.lvjie.net.Req
import com.yxd.maplib.MapUtils
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.header.*

/**
 * 地图
 * @author YeXuDong
 */
@LayoutId(R.layout.activity_map)
class MapActivity : BaseActivity(), LocationSource, AMapLocationListener,
    GeocodeSearch.OnGeocodeSearchListener {

    private lateinit var geocodeSearch: GeocodeSearch

    lateinit var aMap: AMap

    override fun init(bundle: Bundle?) {
        ivBack.click { finish() }
        tvTitle.setLimitText(if (!extraBool("isDetail", false)) "地图" else "设备详情")

        mapView.onCreate(bundle)
        aMap = mapView.map
        geocodeSearch = GeocodeSearch(this)
        geocodeSearch.setOnGeocodeSearchListener(this)

        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE)
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        val myLocationStyle = MyLocationStyle()
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000)
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round))
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle)
        //aMap.getUiSettings().setMyLocationButtonEnabled(true)设置默认定位按钮是否显示，非必需设置。
        //设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.isMyLocationEnabled = false

        MapUtils().initLocation(this) {
            val latLng = if (!extraBool("isDetail", false))
                LatLng(it.latitude, it.longitude)
            else
                LatLng(extraStr("lat").toDouble(), extraStr("lon").toDouble())
            aMap.moveCamera(CameraUpdateFactory.zoomTo(20f))
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng))//这个是关键
            //连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW)

            if (extraBool("isDetail", false)) {
                val dialogLonLat = LonLatDialog(this)
//                aMap.setOnMapClickListener {
//                    dialogLonLat.init(it.longitude.toString(), it.latitude.toString()) { lon, lat ->
//                        BusUtils.post(MsgWhat.UPDATE_LON_LAT, "$lon,$lat")
//                        finish()
//                    }.show()
//                }
                ivMarker.show()
                btnSelect.show()
                //创建一个导航对话框视图，将其添加到布局中并隐藏，以作为地图标记的Bitmap使用
                val dialogView = inflate(R.layout.navi_view)
                dialogView.tv(R.id.tvLoc).txt("${latLng.longitude},${latLng.latitude}")
                dialogView.loadBitmapFromView(Color.TRANSPARENT) {
                    aMap.addMarker(
                        MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromBitmap(it))
                            .draggable(true)
                    )
                }
                flParent.addView(dialogView, WC, WC)
                dialogView.hide()
                btnSelect.click {
                    val longitude = aMap.cameraPosition.target.longitude
                    val latitude = aMap.cameraPosition.target.latitude
                    BusUtils.post(MsgWhat.UPDATE_LON_LAT, "$longitude,$latitude")
                    finish()
                }
            } else {
                Req.getAllDevices {
                    it.data?.forEach {
                        if (it != null) {
                            val view = inflate(R.layout.navi_view)
                            view.tv(R.id.tvLoc).txt(it.equipNo) // "${it.longitude},${it.latitude}"
                            aMap.moveCamera(
                                CameraUpdateFactory.changeLatLng(
                                    LatLng(
                                        it.latitude!!,
                                        it.longitude!!
                                    )
                                )
                            )//这个是关键
                            view.loadBitmapFromView(Color.TRANSPARENT) { bmp ->
                                aMap.addMarker(
                                    MarkerOptions().position(LatLng(it.latitude!!, it.longitude!!))
                                        .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                                        .draggable(true)
                                        .title(it.id.toString())
                                )

                                aMap.setOnMarkerClickListener { marker ->
                                    goTo<DeviceDetailActivity>("id" to marker.title)
                                    true
                                }
                            }
                            flParent.addView(view, WC, WC)
                            view.hide()
                        }
                    }
                }
            }
        }.startLocation()

    }

    override fun deactivate() {

    }

    override fun activate(p0: LocationSource.OnLocationChangedListener?) {

    }

    override fun onLocationChanged(p0: AMapLocation?) {

    }

    override fun onRegeocodeSearched(regeocodeResult: RegeocodeResult?, p1: Int) {

    }

    override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState)
    }
}