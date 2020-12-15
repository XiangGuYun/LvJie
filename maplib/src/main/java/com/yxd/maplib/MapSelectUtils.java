package com.yxd.maplib;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MapSelectUtils {

    /**
     * 检索地图软件
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible(Context context, String packageName){
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 指定地图
     * 百度地图包名：com.baidu.BaiduMap
     * 高德地图包名：com.autonavi.minimap
     * 腾讯地图包名：com.tencent.map
     * 谷歌地图 com.google.android.apps.maps
     */
    public static List<String> mapsList (){
        List<String> maps = new ArrayList<>();
        maps.add("com.baidu.BaiduMap");
        maps.add("com.autonavi.minimap");
        maps.add("com.tencent.map");
        return maps;
    }

    /**
     * 检索筛选后返回
     * @param context
     * @return
     */
    public static List<String> hasMap (Context context){
        List<String> mapsList = mapsList();
        List<String> backList = new ArrayList<>();
        for (int i = 0; i < mapsList.size(); i++) {
            boolean avilible = isAvilible(context, mapsList.get(i));
            if (avilible){
                backList.add(mapsList.get(i));
            }
        }
        return backList;
    }

//    public class MapToast {
//        public void showChooseMap(Context context, View rootView, HisLocationBean bean){
//            CommonPopupWindow popupWindow=new CommonPopupWindow.Builder(context)
//                    .setView(R.layout.map_toast)
//                    .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT)
//                    .setBackGroundLevel(0.5f)
//                    .setViewOnclickListener((view, layoutResId, popupWindow1) -> {
//                        View map_toast_cancelbt = view.findViewById(R.id.map_toast_cancelbt);
//                        View map_toast_gaodebt = view.findViewById(R.id.map_toast_gaodebt);
//                        View map_toast_baidubt = view.findViewById(R.id.map_toast_baidubt);
//                        View map_toast_tencentbt = view.findViewById(R.id.map_toast_tencentbt);
//                        View map_toast_hinttv = view.findViewById(R.id.map_toast_hinttv);
//
//                        List<String> hasMap = new ThridMapUtil().hasMap(context);
//                        for (int i = 0; i < hasMap.size(); i++) {
//                            if (hasMap.get(i).contains("com.autonavi.minimap")){
//                                map_toast_gaodebt.setVisibility(View.VISIBLE);
//                            }else if (hasMap.get(i).contains("com.baidu.BaiduMap")){
//                                map_toast_baidubt.setVisibility(View.VISIBLE);
//                            }else if (hasMap.get(i).contains("com.tencent.map")){
//                                map_toast_tencentbt.setVisibility(View.VISIBLE);
//                            }
//                        }
//                        if (hasMap.size() == 0){
//                            map_toast_hinttv.setVisibility(View.VISIBLE);
//                        }
//                        map_toast_cancelbt.setOnClickListener(view1 -> {
//                            popupWindow1.dismiss();
//                        });
//                        map_toast_gaodebt.setOnClickListener(view1 -> {
//
//                            toGaodeNavi(context,bean);
//                            popupWindow1.dismiss();
//                        });
//                        map_toast_baidubt.setOnClickListener(view1 -> {
//                            toBaidu(context,bean);
//                            popupWindow1.dismiss();
//
//                        });
//                        map_toast_tencentbt.setOnClickListener(view1 -> {
//
//                            toTencent(context,bean);
//                            popupWindow1.dismiss();
//                        });
//
//                    }).setOutsideTouchable(true)
//                    .create();
//            popupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
//        }


        public static class LocationBean{
            private double lat;
            private double lon;

            public LocationBean(double lat, double lon) {
                this.lat = lat;
                this.lon = lon;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLon() {
                return lon;
            }

            public void setLon(double lon) {
                this.lon = lon;
            }
        }

        // 百度地图
        public static void toBaidu(Context context, LocationBean bean){
            Intent intent = null;
            try {
                intent = Intent.getIntent("intent://map/direction?" +
                        "destination=latlng:" + bean.getLat() + "," + bean.getLon() + "|name:我的目的地" +    //终点
                        "&mode=driving&" +
                        "&src=appname#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            //Intent intent= new Intent("android.intent.action.VIEW", android.net.Uri.parse("baidumap://map/geocoder?location=" + bean.getLat() + "," + bean.getLon()));
            context.startActivity(intent);
        }

        // 高德地图
        public static void toGaodeNavi(Context context,LocationBean bean){
            Intent intent= new Intent("android.intent.action.VIEW", android.net.Uri.parse("androidamap://route?sourceApplication=appName&slat=&slon=&sname=我的位置&dlat="+ bean.getLat() +"&dlon="+ bean.getLon()+"&dname=目的地&dev=0&t=2"));
            context.startActivity(intent);
        }

        // 腾讯地图
        public static void toTencent(Context context,LocationBean bean){
            Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("qqmap://map/routeplan?type=drive&from=&fromcoord=&to=目的地&tocoord=" + bean.getLat() + "," + bean.getLon() + "&policy=0&referer=appName"));
            context.startActivity(intent);
        }


}
