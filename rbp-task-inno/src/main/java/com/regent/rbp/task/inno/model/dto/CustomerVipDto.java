package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CustomerVipDto {

    @ApiModelProperty(notes = "Vip卡号")
    private String VIP;

    @ApiModelProperty(notes = "发卡日期")
    private String BegainDate;

    @ApiModelProperty(notes = "失效日期")
    private String ExpireDate;

    @ApiModelProperty(notes = "姓名")
    private String Name;

    @ApiModelProperty(notes = "性别，男、女")
    private String Sex;

    @ApiModelProperty(notes = "出生日期，例‘1995-05-13’")
    private String BirthDate;

    @ApiModelProperty(notes = "电话")
    private String Tel;

    @ApiModelProperty(notes = "电子邮件")
    private String Email;

    @ApiModelProperty(notes = "地址")
    private String Address;

    @ApiModelProperty(notes = "折扣")
    public BigDecimal Discount;

    @ApiModelProperty(notes = "备注")
    private String Remark;

    @ApiModelProperty(notes = "职业")
    private String Work;

    @ApiModelProperty(notes = "手机")
    private String MobileTel;

    @ApiModelProperty(notes = "类别")
    private String LeiBie;

    @ApiModelProperty(notes = "店铺编号")
    private String Customer_ID;

    @ApiModelProperty(notes = "组")
    private String Group;

    @ApiModelProperty(notes = "等级，可设置开卡等级")
    private String VipGrade;

    @ApiModelProperty(notes = "使用状态")
    private String UseStatus;

    @ApiModelProperty(notes = "历史积分")
    public BigDecimal HisIntegral;

    @ApiModelProperty(notes = "省")
    private String City;

    @ApiModelProperty(notes = "市")
    private String InCity;

    @ApiModelProperty(notes = "原渠道")
    private String OriginalCustomer;

    @ApiModelProperty(notes = "状态，正常或者挂失")
    private String Status;

    @ApiModelProperty(notes = "审核，审核了线下不能修改资料")
    public Integer Posted;

    @ApiModelProperty(notes = "密码")
    private String Passwords;

    @ApiModelProperty(notes = "积分")
    public BigDecimal Integral;

    @ApiModelProperty(notes = "营业员")
    private String BusinessManID;

    @ApiModelProperty(notes = "旧卡号")
    private String OldVip;

    @ApiModelProperty(notes = "年龄")
    private String Age;

    @ApiModelProperty(notes = "卡号类型")
    public Integer CardType;


    /*public Integer AddValueTimes;
    private String AgeBound;
    private String AgeType;
    private String BackCause;
    private String BrandID;
    public Integer CheckCycle;
    private String Constellation;
    public Integer CreateType;
    public List<CustomerVipChildrenDto> CustomerVIPChildren;
    public List<CustomerVipGoodsConditionDto> CustomerVipGoodsCondition;
    private String DevBuisnessManID;
    private String DispatchDate;
    private String DispatchStatus;
    private String DispatchUser;
    private String District;
    private String DownSize;
    private String Earning;
    private String EncryptVIP;
    private String EngName;
    private String Favorite;
    private String FavoriteBrand;
    private String FeedBackMode;
    public Integer Flag;
    private String FrockSize;
    private String Height;
    private String Hobby;
    private String IDCard;
    public BigDecimal Imprest;
    private String Information;
    private String Input_Date;
    public Integer isExchange;
    public Integer IsOverlay;
    private String ItemType;
    public Integer ItemTypeID;
    private String JBUser;
    private String LossReportTime;
    private String MaiBuisnessManID;
    private String MarketCodeType;
    private String MobileTel2;
    private String MobileTel3;
    private String MSN;
    private String Nationality;
    private String NativePlace;
    private String Occasion;
    private String ParentVip;
    private String PayCardUser;
    private String Picture;
    private String PostalCode;
    private String PostCode;
    private String PostedDate;
    private String PostUser;
    private String Province;
    private String QQ;
    private String RecomCardNo;
    private String RelationVip;
    private String RepostDate;
    public Integer Reposted;
    private String RepostRemark;
    private String RepostUser;
    private String ReturnReason;
    private String RGUser;
    private String ShoeSize;
    private String Spouse;
    private String unionid;
    private String UpdateTime;
    public Integer UpdateTimeStamp;
    public BigDecimal VipIntegral;
    public BigDecimal VipValue;
    private String WaistLine;
    private String WangWang;
    private String WeiBo;
    private String Weight;
    private String weixinOpenId;*/
}
