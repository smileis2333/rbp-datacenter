package com.regent.rbp.api.core.constants;

/**
 * @Description 业务类型
 * @Author shaoqidong
 * @Date 2020/10/16
 **/
public class BusinessTypeConstants {
    //期货
    public static final Long FUTURES = 1100000000000001L;
    //补货
    public static final Long REPLENISH = 1100000000000002L;
    //铺货
    public static final Long DISTRIBUTION = 1100000000000003L;
    //买断
    public static final Long BUYOUT = 1100000000000004L;

    //配货
    public static final Long ALLOCATE_CARGO = 1100000000000011L;
    //调拨
    public static final Long TRANSFERRING = 1100000000000012L;
    //退货
    public static final Long RETURN_GOODS = 1100000000000013L;
    //发货
    public static final Long SEND_GOODS = 1100000000000014L;

    //收货
    public static final Long RECEIVE_GOODS = 1100000000000021L;
    //调拨收货
    public static final Long TRANSFERRING_RECEIVE_GOODS = 1100000000000022L;
    //收退货
    public static final Long RECEIVE_RETURN_GOODS = 1100000000000023L;

    //盘点调整(基础业务类型id)
    public static final Long INVENTORY_ADJUST = 1100000000000031L;
    //盘点调整(业务类型Id)
    public static final Long SUB_INVENTORY_ADJUST = 1120000000000041L;
    //库存调整
    public static final Long STOCK_ADJUST = 1100000000000032L;
    //收发差异调整
    public static final Long RECEIVE_DIFFERENCE_ADJUST = 1100000000000033L;
    // 子收发差异调整
    public static final Long SUB_RECEIVE_DIFFERENCE_ADJUST = 1120000000000043L;
    //串色串码调整
    public static final Long CROSS_COLOR_SIZE_ADJUST = 1100000000000034L;

}
