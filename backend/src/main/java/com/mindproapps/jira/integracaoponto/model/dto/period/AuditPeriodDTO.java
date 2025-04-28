package com.mindproapps.jira.integracaoponto.model.dto.period;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import com.mindproapps.jira.integracaoponto.model.dto.BaseDTO;
import com.mindproapps.jira.integracaoponto.model.period.AuditPeriod;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonAutoDetect
public class AuditPeriodDTO extends BaseDTO<AuditPeriod> {
    private Long id;
    private String updatedDate;
    private Integer periodId;
    private String periodKey;
    private String periodYearMonth;
    private Date periodStart;
    private Date periodEnd;
    private String previousState;
    private String currentState;

    @Override
    public void mapToDTO(AuditPeriod auditPeriod) {
        this.id = auditPeriod.getID();
        this.updatedDate = auditPeriod.getUpdatedDate();
        this.periodId = auditPeriod.getPeriodId();
        this.currentState = auditPeriod.getCurrentState();
        this.periodKey = auditPeriod.getPeriodKey();
        this.periodYearMonth = auditPeriod.getPeriodYearMonth();
        this.previousState = auditPeriod.getPreviousState();
    }

    public void setPeriodYearMonth(String periodYearMonth) {
        this.periodYearMonth = periodYearMonth;
        DateFormat formatter = new SimpleDateFormat("yyMM"); 
        try {
            this.periodStart = (Date)formatter.parse(periodYearMonth);
            Calendar cal = Calendar.getInstance();
            cal.setTime(this.periodStart);
            cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            this.periodEnd = cal.getTime();
        } catch (ParseException e) {
            this.periodStart = null;
            this.periodEnd = null;
        }
        
    }
}
