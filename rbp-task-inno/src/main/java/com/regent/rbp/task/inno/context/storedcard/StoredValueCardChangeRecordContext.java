package com.regent.rbp.task.inno.context.storedcard;

import com.regent.rbp.api.core.storedvaluecard.StoredValueCardChangeRecord;
import com.regent.rbp.task.inno.model.dto.StoredValueCardRecordDto;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/21 17:44
 */
@Data
public class StoredValueCardChangeRecordContext {
    private List<StoredValueCardRecordDto> resultList;

    public StoredValueCardChangeRecordContext(List<StoredValueCardChangeRecord> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            resultList = new ArrayList<>();
            for (StoredValueCardChangeRecord record : list) {
                StoredValueCardRecordDto dto = new StoredValueCardRecordDto();
                dto.setBillTypeName(record.getChangeTypeName());
                dto.setAmount(record.getChangePayAmount().add(record.getChangeCreditAmount()));
                dto.setCheckDate(record.getCreatedTime());
                dto.setCheckID(record.getBillNo());
                dto.setCustomer_ID(null);
                dto.setManual_ID(null);
                dto.setVip(record.getMemberCardNo());
                dto.setVipName(record.getMemberCardName());
                resultList.add(dto);
            }
        }
    }

}
