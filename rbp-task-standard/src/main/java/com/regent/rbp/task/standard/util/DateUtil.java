package com.regent.rbp.task.standard.util;

import com.regent.rbp.task.standard.module.WeekDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 日期工具类
 * @author: HaiFeng
 * @create: 2021-11-17 16:59
 */
public class DateUtil {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_START = "yyyy-MM-dd 00:00:00";
    public static final String YYYY_MM_DD_END = "yyyy-MM-dd 23:59:59";

    /**
     * 将时间按照星期分割
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<WeekDate> doDateType(Long startTime, Long endTime) {
        List<WeekDate> weekDateList = new ArrayList<>();
        // 开始时间段区间集合
        List<Long> beginDateList = new ArrayList<Long>();
        // 结束时间段区间集合
        List<Long> endDateList = new ArrayList<Long>();
        MyUtils.getIntervalTimeByWeek(startTime, endTime, beginDateList, endDateList);

        for (int i = 0; i < beginDateList.size(); i++) {
            Long beginStr = beginDateList.get(i);
            Long endStr = endDateList.get(i);
            weekDateList.add(new WeekDate(beginStr, endStr));
        }
        return weekDateList;
    }


}
