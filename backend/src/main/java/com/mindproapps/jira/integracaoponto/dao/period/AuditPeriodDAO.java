package com.mindproapps.jira.integracaoponto.dao.period;

import com.mindproapps.jira.integracaoponto.dao.base.BaseDAO;
import com.mindproapps.jira.integracaoponto.model.dto.period.AuditPeriodDTO;
import com.mindproapps.jira.integracaoponto.service.i18n.I18nService;
import com.mindproapps.jira.integracaoponto.util.ConversionUtils;
import com.mindproapps.jira.integracaoponto.util.LegacySQLProcessor;
import lombok.Cleanup;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Component
public class AuditPeriodDAO extends BaseDAO {

    private I18nService i18nService;

    public List<AuditPeriodDTO> getListAuditPeriod(String startDate, String endDate) {
        log.info("getListAuditPeriod: startDate = " + startDate + ", endDate =  " + endDate);
        List<AuditPeriodDTO> list = new ArrayList<>();
        try (LegacySQLProcessor sqlProcessor = this.createSQLProcessor()){
            String sql = "SELECT p.\"ID\", p.\"UPDATED_DATE\", p.\"PREVIOUS_STATE\", p.\"CURRENT_STATE\", " +
                    "p.\"PERIOD_KEY\", p.\"PERIOD_ID\", p.\"PERIOD_YEAR_MONTH\" " +
                    "FROM \"AO_441C88_AUDIT_PERIOD\" p " +
                    "WHERE p.\"UPDATED_DATE\" BETWEEN ? AND ? " +
                    "ORDER BY p.\"UPDATED_DATE\" DESC";
            sqlProcessor.prepareStatement(sql);
            sqlProcessor.setValue(startDate);
            sqlProcessor.setValue(endDate);
            ResultSet result = sqlProcessor.executeQuery();

            while (result.next()) {
                AuditPeriodDTO auditPeriodDTO = new AuditPeriodDTO();
                auditPeriodDTO.setId(result.getLong("ID"));
                auditPeriodDTO.setCurrentState(result.getString("CURRENT_STATE"));
                auditPeriodDTO.setPeriodId(result.getInt("PERIOD_ID"));
                auditPeriodDTO.setPeriodKey(result.getString("PERIOD_KEY"));
                auditPeriodDTO.setPeriodYearMonth(result.getString("PERIOD_YEAR_MONTH"));
                auditPeriodDTO.setPreviousState(result.getString("PREVIOUS_STATE"));
                auditPeriodDTO.setUpdatedDate(ConversionUtils.formatISODateTimeAsRegularDateTime(
                        result.getString("UPDATED_DATE").replace(" ", "T"))
                );
                list.add(auditPeriodDTO);
            }
        } catch (Exception e) {
            log.error("DAO: getListAuditPeriod error: " + e.getMessage(), e);
        }
        return list;
    }
}
