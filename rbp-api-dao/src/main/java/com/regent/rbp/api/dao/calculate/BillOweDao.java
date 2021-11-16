package com.regent.rbp.api.dao.calculate;

import com.regent.rbp.api.dto.calculate.NoticeBillOweDetail;
import com.regent.rbp.api.dto.calculate.SalePlanBillOweDetail;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 计算欠数存储过程
 * @author: HaiFeng
 * @create: 2021-11-15 15:50
 */
public interface BillOweDao {

    void dropTemporaryTable(@Param("tableName") String tableName);

    void calculateSalePlanBillOwe(HashMap<String,Object> map);

    List<SalePlanBillOweDetail> getSalePlanBillOweDetail(@Param("tableName") String tableName);

    void calculateNoticeBillOwe(HashMap<String,Object> map);

    List<NoticeBillOweDetail> getNoticeBillOweDetail(@Param("tableName") String tableName);

}
