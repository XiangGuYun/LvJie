package com.yxd.lvjie.base;

import android.bluetooth.BluetoothGattCharacteristic;

import com.yp.baselib.base.BaseApplication;
import com.yp.baselib.helper.ExceptionHelper;
import com.yxd.lvjie.bean.MService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USR_LJQ on 2015-11-17.
 */
public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ExceptionHelper.getInstance().init();
    }

    private  boolean clearflag;

    public boolean isClearflag() {
        return clearflag;
    }

    public void setClearflag(boolean clearflag) {
        this.clearflag = clearflag;
    }

    public enum SERVICE_TYPE{
        TYPE_USR_DEBUG,TYPE_NUMBER,TYPE_STR,TYPE_OTHER;
    }

    private final List<MService> services = new ArrayList<>();
    private final List<BluetoothGattCharacteristic> characteristics = new ArrayList<>();

    private BluetoothGattCharacteristic characteristic;

    public List<MService> getServices() {
        return services;
    }

    public static SERVICE_TYPE serviceType ;

    public void setServices(List<MService> services) {
        this.services.clear();
        this.services.addAll(services);
    }


    public List<BluetoothGattCharacteristic> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(List<BluetoothGattCharacteristic> characteristics) {
        this.characteristics.clear();
        this.characteristics.addAll(characteristics);
    }


    public void setCharacteristic(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return characteristic;
    }
}
