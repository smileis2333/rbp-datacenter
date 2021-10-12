package com.regent.rbp.task.inno.model.param;

import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021-09-26
 */
@Data
public class UploadRetailSendBillParam {

    private String ErpDeliveryOrderSn;//	#ERP发货单号（ERP发货单主键）

    private String ErpOrderSn;//	ERP订单号

    private String OrderSn;//	订单号

    private String AddTime;//	发货时间

    private String ShippingName;//	快递名称

    private String Shipping_Code;//	快递编码

    private String Consignee;//	收货人

    private String Address;//	地址

    private String Country;//	国家(中文)

    private String Province;//	省份(中文)

    private String City;//	城市(中文)

    private String District;//	地区(中文)

    private String Email;//	邮件

    private String Zipcode;//	邮编

    private String Tel;//	电话

    private String Mobile;//	手机

    private String InsureFee;//	保险费

    private String ShippingFee;//	快递费用

    private String InvoiceNo;//	物流单号

    private String ChannalCode;//	渠道编码

    private String delivery_store_code;//	发货单位代码

    private Integer delivery_type;//	发货类型，0门店，1仓库

    private List<UploadRetailSendBillGoodsParam> DeliveryGoodsList; // 发货明细

}
