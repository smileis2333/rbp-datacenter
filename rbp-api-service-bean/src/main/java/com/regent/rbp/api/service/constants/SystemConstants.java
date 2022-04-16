package com.regent.rbp.api.service.constants;

/**
 * @author chenchungui
 * @date 2021/9/9
 * @description 系统常量
 */
public class SystemConstants {

    public static final Long ADMIN_CODE = 6877239110796543L;

    public static final String SUCCESS_CODE = "1";

    public static final String FAIL_CODE = "-1";

    public static final Integer PAGE_NO = 1;

    public static final Integer PAGE_SIZE = 100;

    public static final Integer BATCH_SIZE = 500;

    public static final Long DEFAULT_RETAIL_ORDER_BASE_BUSINESS_TYPE_ID = 1100000000000052L;

    public static final String NATION = "中国";

    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final Long DEFAULT_TEN_MINUTES = 10 * 60 * 1000L;

    public static final String DOWNLOAD_ONLINE_GOODS_LIST_JOB = "inno.downloadOnlineGoodsListJobHandler";

    public static final String DOWNLOAD_ONLINE_ORDER_LIST_JOB = "inno.downloadOnlineOrderListJobHandler";

    public static final String DOWNLOAD_ONLINE_ORDER_STATUS_LIST_JOB = "inno.downloadOnlineOrderStatusListJobHandler";

    public static final String ONLINE_SYNC_GOODS_STOCK_FULL_JOB = "inno.onlineSyncGoodsStockFullJobHandler";

    public static final String ONLINE_SYNC_GOODS_STOCK_JOB = "inno.onlineSyncGoodsStockJobHandler";

    public static final String POST_ERP_STORE = "inno.PostErpStore";

    public static final String POST_ERP_WAREHOUSE = "inno.PostErpWarehouse";

    public static final String POST_ERP_CLOUD_WAREHOUSE = "inno.PostErpCloudWarehouse";

    public static final String POST_ERP_EMPLOYEE = "inno.PostErpEmployee";

    public static final String POST_ERP_USERS = "inno.PostErpUsers";

    public static final String GET_APP_RETURN_ORDER = "inno.GetAppReturnOrder";

    public static final String POST_RETURN_ORDER_STATUS = "inno.PostUpdateReturnOrderStatus";

    public static final String GET_USER_LIST = "inno.GetUserList";

    public static final String GET_COUPON_POLICY = "inno.GetCouponPolicy";

    public static final String SALE_PLAN_BILL_AUTO_COMPLETE = "salePlanBillAutoComplete";

    public static final String NOTICE_BILL_AUTO_COMPLETE = "noticeBillAutoComplete";

    public static final String PURCHASE_BILL_AUTO_COMPLETE = "purchaseBillAutoComplete";

    public static final String AUTO_DELETE_TEMP_TABLE = "autoDeleteTempTable";

    public static final String UPDATE_BATCH_MANAGEMENT_STATUS = "updateBatchManagementStatus";

    public static final String AUTO_MEMBER_GROUPING = "autoMemberGrouping";

    public static final String TASK_CARE_MEMBER_HANDLER = "taskCareMemberHandler";

    public static final String TASK_CARE_CHANNEL_HANDLER = "taskCareChannelHandler";

    public static final String TASK_CHANNEL_NEW_GOODS_GUARD_JOB = "taskChannelNewGoodsGuardJob";

    public static final String ORDER_PUSH_RETRY_JOB = "yumei.orderPushRetry";

    public static final String ORDER_RECEIPT_PUSH_RETRY_JOB = "yumei.orderReceiptPushRetry";

    public static final String RETAIL_RETURN_NOTICE_PUSH_ORDER_REFUND_JOB = "yumei.retailReturnNoticePushOrderRefund";


}
