package com.mindproapps.jira.integracaoponto.model.timesheets;

import com.mindproapps.jira.integracaoponto.model.base.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.Indexed;
import net.java.ao.schema.NotNull;

@Preload
public interface TSApprOriginTrace extends Entity {
    @Indexed
    @NotNull
    Integer getTimesheetApprovalId();

    void setTimesheetApprovalId();

    @Indexed
    @NotNull
    Integer getOrigin();

    void setOrigin(Integer origin);

}
