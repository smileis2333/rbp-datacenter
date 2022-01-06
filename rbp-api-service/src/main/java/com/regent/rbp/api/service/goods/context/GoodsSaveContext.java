package com.regent.rbp.api.service.goods.context;

import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.goods.*;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.goods.GoodsPriceDto;
import com.regent.rbp.api.dto.goods.GoodsSaveParam;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import lombok.Data;

import java.util.List;

/**
 * 货品保存上下文对象
 *
 * @author xuxing
 */
@Data
public class GoodsSaveContext {

    private Goods goods;
    private List<GoodsColor> goodsColorList;
    private List<GoodsLong> goodsLongList;
    private List<Barcode> barcodeList;
    private List<SizeDisable> sizeDisableList;
    private List<GoodsTagPrice> goodsTagPriceList;
    private List<CustomizeDataDto> customizeData;

    public GoodsSaveContext() {
        this(null);
    }

    public GoodsSaveContext(GoodsSaveParam param) {
        this.goods = new Goods();
        Long userId = ThreadLocalGroup.getUserId();
        this.goods.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        this.goods.setCreatedBy(userId);
        this.goods.setUpdatedBy(userId);

        if (param != null) {
            this.readProperties(param);
        }
    }

    /**
     * 利用GoodsSaveParam属性值更新当前货品对象
     *
     * @param param
     */
    public void readProperties(GoodsSaveParam param) {
        if (this.goods == null) {
            return;
        }
        this.goods.setCode(param.getGoodsCode());
        this.goods.setName(param.getGoodsName());
        this.goods.setType(param.getType());
        this.goods.setBuildDate(param.getBuildDate());
        this.goods.setAssistMaterial(param.getAssistMaterial());
        this.goods.setNotes(param.getNotes());
        this.goods.setMnemonicCode(param.getMnemonicCode());
        this.goods.setQrcodeLink(param.getQrcodeLink());
        this.goods.setUniqueCodeFlag(param.isUniqueCodeFlag());
        this.goods.setStatus(param.getStatus());
        this.goods.setSupplierGoodsNo(param.getSupplierGoodsNo());
        this.goods.setMetricFlag(param.isMetricFlag());
        GoodsPriceDto priceData = param.getPriceData();
        if (priceData != null) {
            this.goods.setMachiningPrice(priceData.getMachiningPrice());
            this.goods.setPlanCostPrice(priceData.getPlanCostPrice());
            this.goods.setMaterialPrice(priceData.getMaterialPrice());
            this.goods.setPurchasePrice(priceData.getPurchasePrice());
        }
    }
}
