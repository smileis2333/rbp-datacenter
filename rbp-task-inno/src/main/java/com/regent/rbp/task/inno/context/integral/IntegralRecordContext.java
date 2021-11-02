package com.regent.rbp.task.inno.context.integral;

import com.regent.rbp.api.core.integral.MemberIntegralChangeRecord;
import com.regent.rbp.task.inno.model.dto.IntegralResultDto;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/22 10:34
 */
@Data
public class IntegralRecordContext {
    private List<IntegralResultDto> resultList;

    public IntegralRecordContext(List<MemberIntegralChangeRecord> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            resultList = new ArrayList<>();
            for (MemberIntegralChangeRecord record : list) {
                IntegralResultDto dto = new IntegralResultDto();
                dto.setBillTypeName(record.getChangeTypeName());
                dto.setCheckDate(record.getCreatedTime());
                dto.setCheckID(record.getBillNo());
                dto.setCustomer_ID(null);
                dto.setRemark(record.getNotes());
                dto.setVip(record.getMemberCardNo());
                dto.setVipName(record.getMemberCardName());
                dto.setIntegralAmount(record.getChangeIntegral());
                resultList.add(dto);
            }
        }
    }
}
