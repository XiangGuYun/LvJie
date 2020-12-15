package com.yxd.maplib

import android.graphics.Color
import android.os.Bundle
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.LocationSource
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.maps2d.model.MyLocationStyle
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeResult
import com.yxd.baselib.base.BaseActivity

abstract class BaseMapActivity : BaseActivity(), LocationSource,
    AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener  {

    private lateinit var aMap: AMap

    abstract fun getMapView():MapView

    abstract fun getReGeocodeResult():((RegeocodeResult)->Unit)

    override fun beforeInit(savedInstanceState: Bundle?) {
        getMapView().onCreate(savedInstanceState)
        aMap = getMapView().map
//        geocodeSearch = GeocodeSearch(this)
//        geocodeSearch.setOnGeocodeSearchListener(this)

        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE)
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        val myLocationStyle = MyLocationStyle()
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000)
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.hecard))
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle)
        //aMap.getUiSettings().setMyLocationButtonEnabled(true)设置默认定位按钮是否显示，非必需设置。
        //设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(false)
        val latLng = LatLng(extraStr("latitude").toDouble(), extraStr("longitude").toDouble())
        aMap.moveCamera(CameraUpdateFactory.zoomTo(20f))
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng))//这个是关键
        //连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW)

        //获取详细地址信息
//        getAddressByLatlng(latLng.latitude, latLng.longitude)
    }

    fun doMark(){
        //创建一个导航对话框视图，将其添加到布局中并隐藏，以作为地图标记的Bitmap使用
//        val dialogView = inflate(R.layout.navi_view)
//        flParent.addView(dialogView, WC, WC)
//        dialogView.hide()
//        //门店名称
//        dialogView.tv(R.id.tvShopName).text = extraStr("name")
//        dialogView.tv(R.id.tvShopAddress).text = extraStr("address")
//
//        dialogView.loadBitmapFromView(Color.TRANSPARENT) {
//            aMap.addMarker(
//                MarkerOptions().position(latLng)
//                    .icon(BitmapDescriptorFactory.fromBitmap(it))
//                    .draggable(true).title("marker1")
//            )
//
//            aMap.setOnMarkerClickListener {
//                when (it.title) {
//                    "marker1" -> {
//                        if(mapAppList.isEmpty()){
//                            "暂未找到第三方地图软件\n请先下载哦".toast()
//                        }else{
//                            doMapSelectDialog(latLng.latitude, latLng.longitude)
//                        }
//                    }
//                }
//                true
//            }
//        }
    }

    override fun deactivate() {

    }

    override fun activate(p0: LocationSource.OnLocationChangedListener?) {

    }

    override fun onLocationChanged(p0: AMapLocation?) {

    }

    override fun onRegeocodeSearched(regeocodeResult: RegeocodeResult?, p1: Int) {
        regeocodeResult?.let {
            val regeocodeAddress = regeocodeResult.regeocodeAddress
            val formatAddress = regeocodeAddress.formatAddress
            getReGeocodeResult().invoke(it)
        }
    }

    override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        getMapView().onDestroy()
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        getMapView().onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        getMapView().onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        getMapView().onSaveInstanceState(outState)
    }

    fun getAddressByLatlng(latitude: Double, longitude: Double) {
//        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
//        val latLonPoint = LatLonPoint(latitude, longitude)
//        val query = RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP)
//        //异步查询
//        geocodeSearch.getFromLocationAsyn(query)
    }

}