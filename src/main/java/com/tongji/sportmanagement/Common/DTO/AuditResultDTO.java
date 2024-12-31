package com.tongji.sportmanagement.Common.DTO;

import lombok.Data;

@Data
public class AuditResultDTO {
    private boolean result;
    private Integer reviewerId;
    private Integer auditObjectId;
}
