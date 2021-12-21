package com.regent.rbp.api.service.constants;

/**
 * @author czw
 * @date 2020/5/9 14:35
 */
public class BillConstants {

    public interface BillGoodsColumnSettingConstants {
        //全局模块
         String GLOBAL_MODULE = "global";
         String GLOBAL_MODULE_NAME = "全局";
    }


    public interface HanderTypeConstants {
        //保存
        Integer SAVE = 1;
        //审核
        Integer CHECK = 2;
    }
    
    public interface ReceiveDifferenceProcessStatusConstants {
        //未处理
        Integer UNPROCESS = 1;
        //已处理
        Integer PROCESSED = 2;
    }
    
    public interface ReceiveDifferenceProcessModeConstants {
        //发方责任
        Integer PROCESSMODE1 = 1;
        //收方责任
        Integer PROCESSMODE2 = 2;
        //线下处理
        Integer PROCESSMODE3 = 3;
    }

}
