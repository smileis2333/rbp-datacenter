package com.regent.rbp.task.inno.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CustomerVipDto {
    private String VIP;
    /**
     * 姓名
     */
    private String Name;
    private String BegainDate;
    private String ExpireDate;
    /**
     * 性别
     */
    private String Sex;
    /**
     * 生日
     */
    private String BirthDate;
    private String Tel;
    private String Email;
    private String Address;
    public BigDecimal Discount;
    private String Remark;
    private String Work;
    private String MobileTel;
    private String LeiBie;
    private String Customer_ID;
    private String Group;
    private String VipGrade;
    private String UseStatus;
    public BigDecimal HisIntegral;
    private String City;
    private String InCity;
    private String OriginalCustomer;
    private String Status;
    public Integer Posted;
    private String Passwords;
    public BigDecimal Integral;
    private String BusinessManID;
    private String OldVip;
    private String Age;
    public Integer CardType;

    public Integer AddValueTimes;
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
    /**
     * 身高
     */
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
    /**
     * 体重
     */
    private String Weight;
    private String weixinOpenId;
}
