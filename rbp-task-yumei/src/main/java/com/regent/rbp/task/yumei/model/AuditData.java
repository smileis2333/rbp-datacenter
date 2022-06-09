package com.regent.rbp.task.yumei.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuditData {
    private Long createdBy;
    private Long checkBy;
    private Long updateBy;
}
