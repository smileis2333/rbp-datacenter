package com.regent.rbp.api.core.receiveBill;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillMasterData;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 收货单主 对象 rbp_receive_bill
 * 
 * @author zyh
 * @date 2020-07-22
 */
@Data
@ApiModel(description="收货单 ")
@TableName(value = "rbp_receive_bill")
public class ReceiveBill extends BillMasterData {

//	@ApiModelProperty(notes = "业务类型")
    private Long businessTypeId;
//
//    @ApiModelProperty(notes = "业务类型名称")
//    @TableField(exist = false)
//    private String businessTypeName;
//
//    @ApiModelProperty(notes = "发货渠道编码")
    private Long channelId;
//
//    @ApiModelProperty(notes = "发货渠道名称")
//    @TableField(exist = false)
//    private String channelName;
//
//    @ApiModelProperty(notes = "发货渠道编号")
//    @TableField(exist = false)
//    private String channelCode;
//
//    @ApiModelProperty(notes = "收货渠道编号")
    private Long toChannelId;
//
    @TableField(exist = false)
    private String toChannelName;

    @TableField(exist = false)
    private String toChannelCode;
//
//    @ApiModelProperty(notes = "币种")
    private Long currencyTypeId;
//
//    @ApiModelProperty(notes = "币种名称")
//    @TableField(exist = false)
//    private String currencyTypeName;
//
//    @ApiModelProperty(notes = "发货单Id")
    private Long sendId;
//
//    @ApiModelProperty(notes = "发货单号")
    @TableField(exist = false)
    private String sendBillNo;
//
//    @ApiModelProperty(notes = "处理状态 (0.未执行;1.已配货;2.已发货;3.已完成;4.已取消;)")
    private Integer processStatus;
//
//    @ApiModelProperty(notes = "处理状态名称")
//    @TableField(exist = false)
//    private String  processStatusName;
//
//    @ApiModelProperty(notes = "箱列表")
//    @TableField(exist = false)
//    private List<Box> boxList;
//
//    @ApiModelProperty(notes = "原单货品明细")
//    @TableField(exist = false)
//    private List<ReceiveBillGoods> billGoodsList;
//
//    @ApiModelProperty(notes = "唯一码明细")
//    @TableField(exist = false)
//    private List<Label> labelList;
//
//    @ApiModelProperty(notes = "实收货品明细")
//    @TableField(exist = false)
//    private List<ReceiveBillRealGoods> billRealGoodsList;
//
//    @ApiModelProperty(notes = "差异数(SKU)(保存时，界面无需传此参数)")
//    @TableField(exist = false)
//    private List<ReceiveBillDifference> receiveBillDifferenceList;
//
//    @ApiModelProperty(notes = "失效人")
//    private Long cancelBy;
//
//    @ApiModelProperty(notes = "失效时间")
//    private Date cancelTime;
//
//    @ApiModelProperty(notes = "失效人名称")
//    @TableField(exist = false)
//    private String cancelByName;
//
//    @ApiModelProperty(notes = "反审核人")
//    private Long uncheckBy;
//
//    @ApiModelProperty(notes = "反审核时间")
//    private Date uncheckTime;
//
//    @ApiModelProperty(notes = "反审核人名称")
//    @TableField(exist = false)
//    private String uncheckByName;
//
//    @ApiModelProperty(notes = "完结状态")
//    private Boolean finishFlag;
//
//    @ApiModelProperty(notes = "原单货品明细总记录数")
//    @TableField(exist = false)
//    private long orginGoodsTotal;
//
//    @ApiModelProperty(notes = "科目")
//    private Long subjectId;
//
//    @ApiModelProperty(notes = "指令单id")
    private Long noticeId;
//
//    @ApiModelProperty(notes = "收货工作台单编码")
//    private Long receiveBenchId;
//
//    @ApiModelProperty(notes = "核算设置")
//    @TableField(exist = false)
//    private AccountingSettingDto accountingSetting;
//
//    @ApiModelProperty(notes = "价格类型")
//    @TableField(exist = false)
//    private Long priceTypeId;
//
//    public void setProcessStatus(Integer processStatus) {
//        this.processStatus = processStatus;
//        String key = "SalePlanBillProcessStatus0";
//        if (processStatus != null) {
//            //1
//            switch(processStatus){
//                case 1:
//                    key = "SalePlanBillProcessStatus1";
//                    break;
//                case 2:
//                    key = "SalePlanBillProcessStatus2";
//                    break;
//                case 3:
//                    key = "SalePlanBillProcessStatus3";
//                    break;
//                case 4:
//                    key = "SalePlanBillProcessStatus4";
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
}
