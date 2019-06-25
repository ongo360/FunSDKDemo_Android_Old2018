package com.example.funsdkdemo.devices.tour.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccy on 2017-09-28.
 * 获取巡航路线json
 */

public class PTZTourBean {

    public static final String JSON_NAME = "Uart.PTZTour";

    public String Name; //巡航路线名称  (目前无用，为"")
    public int Id; //巡航路线id
    public List<TourBean> Tour = new ArrayList<>(); //巡航点集合
}
