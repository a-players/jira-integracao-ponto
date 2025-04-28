package com.mindproapps.jira.integracaoponto.model.period;

import com.mindproapps.jira.integracaoponto.model.base.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.Indexed;
import net.java.ao.schema.NotNull;

@Preload
public interface AuditPeriod extends Entity {
    @Indexed
    @NotNull
    String getUpdatedDate();

    public void setUpdatedDate(String updatedDate);

    @Indexed
    @NotNull
    Integer getPeriodId();

    public void setPeriodId(Integer periodId);

    @Indexed
    @NotNull
    String getPeriodKey();

    public void setPeriodKey(String periodKey);

    @Indexed
    @NotNull
    String getPeriodYearMonth();

    public void setPeriodYearMonth(String periodYearMonth);

    @NotNull
    String getPreviousState();

    public void setPreviousState(String previousState);

    @NotNull
    String getCurrentState();

    void setCurrentState(String currentState);

}
