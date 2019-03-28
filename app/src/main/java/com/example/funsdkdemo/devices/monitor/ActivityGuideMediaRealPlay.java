package com.example.funsdkdemo.devices.monitor;

import com.example.funsdkdemo.ActivityGuide;
import com.example.funsdkdemo.ActivityGuideDeviceListAP;
import com.example.funsdkdemo.ActivityGuideDeviceListLan;
import com.example.funsdkdemo.ActivityGuideDeviceSNLogin;
import com.example.funsdkdemo.DemoModule;
import com.example.funsdkdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-11-24.
 */

public class ActivityGuideMediaRealPlay extends ActivityGuide{

    private static List<DemoModule> mMoudules = new ArrayList<DemoModule>();

    static {
        // 2.1 连接设备(通过序列号连接)
        mMoudules.add(new DemoModule(-1,
                R.string.guide_module_title_device_sn,
                -1,
                ActivityGuideDeviceSNLogin.class));

        // 2.2 连接设备(附近AP直连)
        mMoudules.add(new DemoModule(-1,
                R.string.guide_module_title_device_ap,
                -1,
                ActivityGuideDeviceListAP.class));

        // 2.3 连接设备(局域网内)
        mMoudules.add(new DemoModule(-1,
                R.string.guide_module_title_device_lan,
                -1,
                ActivityGuideDeviceListLan.class));
    }
    @Override
    protected List<DemoModule> getGuideModules() {
        return mMoudules;
    }
}
