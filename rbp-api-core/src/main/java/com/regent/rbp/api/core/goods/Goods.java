package com.regent.rbp.api.core.goods;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author xuxing
 */
@Data
@ApiModel(description = "货品档案 ")
@TableName(value = "rbp_goods")
public class Goods extends Model<Goods> {

    @TableId("id")
    @ApiModelProperty(notes = "ID")
    private Long id;

    @ApiModelProperty(notes = "货号")
    private String code;

    @ApiModelProperty(notes = "品名")
    private String name;

    @ApiModelProperty(notes = "货号编码规则")
    private Long buildRuleId;

    @ApiModelProperty(notes = "货品类型 1.普通物料2.单一物料")
    private Integer type;

    @ApiModelProperty(notes = "建档日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date buildDate;

    @ApiModelProperty(notes = "助记码")
    private String mnemonicCode;

    @ApiModelProperty(notes = "尺码类型 关联尺码档案rbp_size_class")
    private Long sizeClassId;

    @ApiModelProperty(notes = "号型 关联号型档案rbp_model_class")
    private Long modelClassId;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "品牌 关联品牌档案rbp_brand")
    private Long brandId;

    @ApiModelProperty(notes = "类别 关联类别档案rbp_category")
    private Long categoryId;

    @ApiModelProperty(notes = "系列 关联系列档案rbp_series")
    private Long seriesId;

    @ApiModelProperty(notes = "款型 关联款型档案rbp_pattern")
    private Long patternId;

    @ApiModelProperty(notes = "风格 关联风格档案rbp_style")
    private Long styleId;

    @ApiModelProperty(notes = "销售分类 关联销售分类rbp_sale_class")
    private Long saleClassId;

    @ApiModelProperty(notes = "年份 关联年份档案rbp_year")
    private Long yearId;

    @ApiModelProperty(notes = "季节 关联季节档案rbp_season")
    private Long seasonId;

    @ApiModelProperty(notes = "波段 关联波段档案rbp_band")
    private Long bandId;

    @ApiModelProperty(notes = "面料 关联面料档案rbp_material")
    private Long materialId;

    @ApiModelProperty(notes = "辅料")
    private String assistMaterial;

    @ApiModelProperty(notes = "性别 关联性别档案rbp_sex")
    private Long sexId;

    @ApiModelProperty(notes = "适用年龄 关联适用年龄rbp_age_range")
    private Long ageRangeId;

    @ApiModelProperty(notes = "换货类别 关联换货类别rbp_exchange_category")
    private Long exchangeCategoryId;

    @ApiModelProperty(notes = "二维码链接")
    private String qrcodeLink;

    @ApiModelProperty(notes = "单位")
    private Long unitId;;

    @ApiModelProperty(notes = "启用唯一码  0-未启用 1启用")
    private Boolean uniqueCodeFlag;

    @ApiModelProperty(notes = "状态 (0：未审核 1：已审核 2：反审核 3：作废)")
    private Integer status;

    @ApiModelProperty(notes = "折扣类别 关联折扣类别rbp_discount_category")
    private Long discountCategoryId;

    @ApiModelProperty(notes = "加工价")
    private BigDecimal machiningPrice;

    @ApiModelProperty(notes = "物料价")
    private BigDecimal materialPrice;

    @ApiModelProperty(notes = "计划成本价")
    private BigDecimal planCostPrice;

    @ApiModelProperty(notes = "进货价")
    private BigDecimal purchasePrice;

    @ApiModelProperty(notes = "供应商 关联供应商rbp_supplier")
    private Long supplierId;

    @ApiModelProperty(notes = "厂家编号")
    private String supplierGoodsNo;

    @ApiModelProperty(notes = "计量商品标记: 0否 1是")
    private Boolean metricFlag;

    @ApiModelProperty(notes = "一级品类")
    private Long category1;

    @ApiModelProperty(notes = "二级品类")
    private Long category2;

    @ApiModelProperty(notes = "三级品类")
    private Long category3;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

    @ApiModelProperty(notes = "审核人")
    private Long checkBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "审核时间")
    private Date checkTime;

    @ApiModelProperty(notes = "失效人")
    private Long cancelBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "失效时间")
    private Date cancelTime;

    @ApiModelProperty(notes = "反审核人")
    private Long uncheckBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "反审核时间")
    private Date uncheckTime;

    @ApiModelProperty(notes = "自定义字段")
    @TableField(exist = false)
    private Map<String, Object> customFieldMap;
}
