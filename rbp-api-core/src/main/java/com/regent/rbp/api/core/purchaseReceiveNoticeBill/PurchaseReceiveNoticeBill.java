package com.regent.rbp.api.core.purchaseReceiveNoticeBill;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillMasterData;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author huangjie
 * @date : 2021/12/22
 * 采购到货通知单
 * @description
 */
@Data
@TableName(value = "rbp_purchase_receive_notice_bill")
public class PurchaseReceiveNoticeBill extends BillMasterData {
//
//    @ApiModelProperty(notes = "原单号")
//    @TableField(exist = false)
//    private String purchaseNo;
//
//    @ApiModelProperty(notes = "原单号Id")
    private Long purchaseId;
//
//    @ApiModelProperty(notes = "税率")
    private BigDecimal taxRate ;
//
//    @ApiModelProperty(notes = "币种")
    private Long currencyTypeId;
//
//    @ApiModelProperty(notes = "业务类型")
    private Long businessTypeId;
//
//    @ApiModelProperty(notes = "业务类型名称")
//    @TableField(exist = false)
//    private String businessTypeName;
//
//    @ApiModelProperty(notes = "供应商编码")
    private Long supplierId;
//
//    @ApiModelProperty(notes = "供应商名称")
//    @TableField(exist = false)
//    private String supplierName;
//
//    @ApiModelProperty(notes = "供应商编号")
//    @TableField(exist = false)
//    private String supplierCode;
//
//    @ApiModelProperty(notes = "收货渠道编号")
    private Long toChannelId;
//
//    @ApiModelProperty(notes = "收货渠道名称")
//    @TableField(exist = false)
//    private String toChannelName;
//
//    @ApiModelProperty(notes = "收货渠道编号")
//    @TableField(exist = false)
//    private String toChannelCode;
//
////    @ApiModelProperty(notes = "货品明细")
////    @TableField(exist = false)
////    private List<PurchaseReceiveNoticeBillGoods> billGoodsList;
//
//    @ApiModelProperty(notes = "唯一码明细")
//    @TableField(exist = false)
//    private List<Label> labelList;
//
//    @ApiModelProperty(notes = "箱列表")
//    @TableField(exist = false)
//    private List<Box> boxList;
//
//    @ApiModelProperty(notes = "处理状态 (0.未执行;1.已配货;2.已发货;3.已完成;4.已取消;)")
//    private Integer processStatus;
//
//    @ApiModelProperty(notes = "处理状态名称")
//    @TableField(exist = false)
//    private String processStatusName;
//
//    @ApiModelProperty(notes = "失效人")
//    @TableField(exist = false)
//    private Long cancelBy;
//
//    @ApiModelProperty(notes = "失效人名称")
//    @TableField(exist = false)
//    private String cancelByName;
//
//    @ApiModelProperty(notes = "失效时间")
//    @TableField(exist = false)
//    private Date cancelTime;
//
//    @ApiModelProperty(notes = "反审核人")
//    @TableField(exist = false)
//    private Long uncheckBy;
//
//    @ApiModelProperty(notes = "反审核人名称")
//    @TableField(exist = false)
//    private String uncheckByName;
//
//    @ApiModelProperty(notes = "反审核时间")
//    @TableField(exist = false)
//    private Date uncheckTime;
//
//    @ApiModelProperty(notes = "核算设置")
//    @TableField(exist = false)
//    private AccountingSettingDto accountingSetting;
//
//    public void setProcessStatus(Integer processStatus) {
//        this.processStatus = processStatus;
//        String key = "PurchaseReceiveNoticeBillProcess0";
//        if (processStatus != null) {
//            //1
//            switch (processStatus) {
//                case 1:
//                    key = "PurchaseReceiveNoticeBillProcess0";
//                    break;
//                case 2:
//                    key = "PurchaseReceiveNoticeBillProcess1";
//                    break;
//                default:
//                    break;
//            }
//        }
//        this.processStatusName = LanguageUtil.getMessage(key);
//    }
//
//    public Integer getProcessStatus() {
//        return processStatus;
//    }
//
//    @ApiModelProperty(notes = "完结状态")
//    private Integer finishFlag;
//
//    @ApiModelProperty(notes = "完结状态名称")
//    @TableField(exist = false)
//    private String finishFlagName;
//
//    public void setFinishFlag(Integer finishFlag) {
//        this.finishFlag = finishFlag;
//        String key = "FinishFlag0";
//        if (finishFlag != null) {
//            switch(finishFlag){
//                case 0:
//                    key = "FinishFlag0";
//                    break;
//                case 1:
//                    key = "FinishFlag1";
//                    break;
//                default:
//                    break;
//            }
//        }
//        this.finishFlagName = LanguageUtil.getMessage(key);
//    }
//
//    public Integer getFinishFlag() {
//        return finishFlag;
//    }
}
