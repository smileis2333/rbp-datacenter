package com.regent.rbp.api.dao.supplier;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.supplier.Supplier;
import com.regent.rbp.api.core.supplier.SupplierGrade;
import com.regent.rbp.api.core.supplier.SupplierNature;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author xuxing
 */
public interface SupplierDao extends BaseMapper<Supplier> {

    @Select("select * from rbp_supplier_nature rsn where name = #{natureName}")
    SupplierNature selectNatureByName(@Param("natureName") String natureName);

    @Select("select * from rbp_supplier_grade rsn where name = #{gradeName}")
    SupplierGrade selectGradeByName(@Param("gradeName") String gradeName);

}
