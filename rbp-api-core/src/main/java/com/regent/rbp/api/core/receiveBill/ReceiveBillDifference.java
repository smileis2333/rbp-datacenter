package com.regent.rbp.api.core.receiveBill;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收货差异明细对象 rbp_receive_bill_difference
 * 
 * @author zhongyh
 * @date 2020-07-28
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(description="收货差异明细对象")
@TableName(value = "rbp_receive_bill_difference")
public class ReceiveBillDifference extends Model<ReceiveBillDifference>{
	
    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "单据编码")
    private Long billId;
    
    @ApiModelProperty(notes = "收货单号")
    @TableField(exist = false)
    private String billNo;
    
    @ApiModelProperty(notes = "发货单号")
    @TableField(exist = false)
    private String sendBillNo;
    
    @ApiModelProperty(notes = "指令单号")
    @TableField(exist = false)
    private String noticeBillNo;
    
    @ApiModelProperty(notes = "货号")
    private Long goodsId;

    @ApiModelProperty(notes = "货品编号")
    @TableField(exist = false)
    private String goodsCode;

    @ApiModelProperty(notes = "货品名称")
    @TableField(exist = false)
    private String goodsName;

    @ApiModelProperty(notes = "颜色")
    private Long colorId;

    @ApiModelProperty(notes = "颜色编号")
    @TableField(exist = false)
    private String colorNo;

    @ApiModelProperty(notes = "颜色名称")
    @TableField(exist = false)
    private String colorName;

    @ApiModelProperty(notes = "内长")
    private Long longId;

    @ApiModelProperty(notes = "内长名称")
    @TableField(exist = false)
    private String longName;

    @ApiModelProperty(notes = "尺码id, 就是size_detail_id")
    private Long sizeId;
    
    @ApiModelProperty(notes = "尺码名称")
    @TableField(exist = false)
    private String sizeName;
    
    @ApiModelProperty(notes = "条码")
    @TableField(exist = false)
    private String barcode;

    @ApiModelProperty(notes = "差异数")
    private BigDecimal quantity;
	
    @ApiModelProperty(notes = "处理状态")
    private Integer processStatus;
    
    @TableField(exist = false)
    @ApiModelProperty(notes = "处理状态名称")
    private String processStatusName;
    
    @ApiModelProperty(notes = "处理方式")
    private Integer processMode;
    
    @TableField(exist = false)
    @ApiModelProperty(notes = "处理方式名称")
    private String processModeName;
    
    @ApiModelProperty(notes = "处理人")
    private Long processBy;

    @ApiModelProperty(notes = "处理人名称")
    @TableField(exist = false)
    private String processByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "处理时间")
    private Date processTime;
    
    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建人名称")
    @TableField(exist = false)
    private String createdByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;
    
    @ApiModelProperty(notes = "货品id集合(差异处理工作台按单据处理中的货品列id)")
    @TableField(exist = false)
    private String goodsIds;
    
    public ReceiveBillDifference(Long id, Long billId, Long goodsId, Long colorId, Long longId, Long sizeId, BigDecimal quantity) {
        setId(id);
        setBillId(billId);
        setGoodsId(goodsId);
        setColorId(colorId);
        setLongId(longId);
        setSizeId(sizeId);
        setQuantity(quantity);
    }
    
	public String getUniqueKey() {
		String uniqueKey = getGoodsId()+"-"+getColorId()+"-"+getLongId()+"-"+getSizeId();
		return uniqueKey;
	}
	
	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
		String key = "";
		if (processStatus != null) {
			// 1
			switch (processStatus) {
			case 1:
				key = "ReceiveBillProcessStatus1"; // 未处理
				break;
			case 2:
				key = "ReceiveBillProcessStatus2";// 已处理
				break;
			default:
				break;
			}
			this.processStatusName = LanguageUtil.getMessage(key);
		}
	}

	public Integer getProcessStatus() {
		return processStatus;
	}
	
	public void setProcessMode(Integer processMode) {
		this.processMode = processMode;
		String key = "";
		if (processMode != null) {
			// 1
			switch (processMode) {
			case 1:
				key = "ReceiveBillProcessMode1"; // 发方责任
				break;
			case 2:
				key = "ReceiveBillProcessMode2";// 收方责任
				break;
			case 3:
				key = "ReceiveBillProcessMode3";// 线下处理
				break;
			default:
				break;
			}
			this.processModeName = LanguageUtil.getMessage(key);
		}
	}

	public Integer getProcessMode() {
		return processMode;
	}
	
}
