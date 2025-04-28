package com.mindproapps.jira.integracaoponto.service.period;

import com.mindproapps.jira.integracaoponto.model.dto.period.AuditPeriodDTO;

import java.util.List;

public interface AuditPeriodService {
    List<AuditPeriodDTO> getListByInterval(String startDate, String endDate);
}
