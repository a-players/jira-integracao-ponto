package com.mindproapps.jira.integracaoponto.service.period.impl;

import com.mindproapps.jira.integracaoponto.dao.period.AuditPeriodDAO;
import com.mindproapps.jira.integracaoponto.model.dto.period.AuditPeriodDTO;
import com.mindproapps.jira.integracaoponto.service.i18n.I18nService;
import com.mindproapps.jira.integracaoponto.service.period.AuditPeriodService;
import com.mindproapps.jira.integracaoponto.util.ConversionUtils;
import com.mindproapps.jira.integracaoponto.validator.PeriodValidator;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Log4j
public class AuditPeriodServiceImpl implements AuditPeriodService {
    private AuditPeriodDAO auditPeriodDAO;
    private PeriodValidator periodValidator;
    private I18nService i18nService;

    @Autowired
    public AuditPeriodServiceImpl(AuditPeriodDAO auditPeriodDAO,
                                  PeriodValidator periodValidator,
                                  I18nService i18nService) {
        this.auditPeriodDAO = auditPeriodDAO;
        this.periodValidator = periodValidator;
        this.i18nService = i18nService;
    }

    @Override
    public List<AuditPeriodDTO> getListByInterval(String startDate, String endDate) {
        log.info("getListByInterval: startDate = " + startDate + ", endDate = " + endDate);
        LocalDate start = ConversionUtils.convertISODateStringToLocalDate(startDate);
        LocalDate end = ConversionUtils.convertISODateStringToLocalDate(endDate);
        periodValidator.validateDateInterval(start, end);
        end = end.plusDays(1);
        Map<String, String > i18nEstados = i18nService.getTexts("logs.status");
        List<AuditPeriodDTO> list = auditPeriodDAO.getListAuditPeriod(ConversionUtils.convertLocalDateToISODateString(start),
                                                ConversionUtils.convertLocalDateToISODateString(end));
        list.forEach(auditPeriodDTO -> {
            auditPeriodDTO.setCurrentState(i18nEstados.get(auditPeriodDTO.getCurrentState()));
            auditPeriodDTO.setPreviousState(i18nEstados.get(auditPeriodDTO.getPreviousState()));
        });
        return list;
    }
}
