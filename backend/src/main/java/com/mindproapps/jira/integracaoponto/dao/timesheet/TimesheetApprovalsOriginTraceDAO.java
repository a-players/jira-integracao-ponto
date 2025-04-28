package com.mindproapps.jira.integracaoponto.dao.timesheet;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.mindproapps.jira.integracaoponto.dao.base.BaseDAO;
import com.mindproapps.jira.integracaoponto.model.timesheets.TimesheetActionOrigin;
import com.mindproapps.jira.integracaoponto.model.timesheets.TSApprOriginTrace;
import lombok.val;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Log4j
public class TimesheetApprovalsOriginTraceDAO extends BaseDAO {

    @ComponentImport
    private ActiveObjects ao;

    @Autowired
    public TimesheetApprovalsOriginTraceDAO(ActiveObjects activeObjects) {
        log.info("TimesheetApprovalsOriginTraceDAO: activeObjects = " + activeObjects);
        this.ao = activeObjects;
    }

    public void saveOriginTrace(Integer timesheetApprovalId, TimesheetActionOrigin origin) {
        log.info("saveOriginTrace: timesheetApprovalId = " + timesheetApprovalId + ", origin = " + origin);
        val map = new HashMap<String, Object>();
        map.put("TIMESHEET_APPROVAL_ID", timesheetApprovalId);
        map.put("ORIGIN", origin.getCode());
        TSApprOriginTrace tSApprOriginTrace = ao.create(
                TSApprOriginTrace.class, map);
        tSApprOriginTrace.save();
    }
}
