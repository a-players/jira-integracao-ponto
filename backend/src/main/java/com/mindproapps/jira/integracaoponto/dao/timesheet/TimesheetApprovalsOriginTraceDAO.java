package com.mindproapps.jira.integracaoponto.dao.timesheet;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.mindproapps.jira.integracaoponto.dao.base.BaseDAO;
import com.mindproapps.jira.integracaoponto.model.timesheets.TimesheetActionOrigin;
import com.mindproapps.jira.integracaoponto.model.timesheets.TSApprOriginTrace;
import lombok.extern.log4j.Log4j;
import lombok.val;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;

@Named
@Log4j
public class TimesheetApprovalsOriginTraceDAO extends BaseDAO {

    private final ActiveObjects ao;

    @Inject
    public TimesheetApprovalsOriginTraceDAO(@ComponentImport ActiveObjects ao) {
        log.info("TimesheetApprovalsOriginTraceDAO: activeObjects = " + ao);
        this.ao = ao;
    }

    public void saveOriginTrace(Integer timesheetApprovalId, TimesheetActionOrigin origin) {
        log.info("saveOriginTrace: timesheetApprovalId = " + timesheetApprovalId + ", origin = " + origin);
        val map = new HashMap<String, Object>();
        map.put("TIMESHEET_APPROVAL_ID", timesheetApprovalId);
        map.put("ORIGIN", origin.getCode());
        TSApprOriginTrace trace = ao.create(TSApprOriginTrace.class, map);
        trace.save();
    }
}
