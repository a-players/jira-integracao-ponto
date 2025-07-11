package com.mindproapps.jira.integracaoponto.dao.user.depara;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.mindproapps.jira.integracaoponto.dao.base.BaseDAO;
import com.mindproapps.jira.integracaoponto.model.dto.user.AuditDeParaUserDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.DeParaUserDTO;
import com.mindproapps.jira.integracaoponto.model.user.AuditDeParaUser;
import com.mindproapps.jira.integracaoponto.model.user.DeParaUser;
import com.mindproapps.jira.integracaoponto.util.ConversionUtils;
import com.mindproapps.jira.integracaoponto.util.LegacySQLProcessor;
import net.java.ao.Query;
import lombok.Cleanup;
import lombok.extern.log4j.Log4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.ResultSet;

@Component
@Log4j
public class DeParaUserDAO extends BaseDAO {

    private ActiveObjects ao;

    public static final String SQL_AUDIT =
            "SELECT DISTINCT adp.\"ID\", adp.\"UPDATED_DATE\", adp.\"USER_KEY\" , " +
                    "adp.\"PREVIOUS_INFORMED_EMAIL\" , " +
                    "adp.\"INFORMED_EMAIL\", aaui.\"DISPLAY_NAME\" " +
                    "FROM \"AO_441C88_AUDIT_DE_PARA_USER\" adp " +
                    "INNER JOIN \"AO_AEFED0_USER_INDEX\" aaui " +
                    "ON aaui .\"USER_KEY\" = adp.\"ACTOR_KEY\" " +
                    "WHERE adp.\"USER_KEY\" IN(%%USER_KEYS%%) " +
                    "ORDER BY adp.\"UPDATED_DATE\" DESC";

    public DeParaUserDAO(ActiveObjects activeObjects) {
        log.info("DeParaUserDAO: activeObjects" + activeObjects);
        this.ao = activeObjects;
    }

    public void insertNewDeParaUser(DeParaUserDTO deParaUserDTO, String actorKey) {
        log.info("insertNewDeParaUser: deParaUserDTO = " + deParaUserDTO + ", actorKey = " + actorKey);
        if (deParaUserDTO.getEmail() != null &&
                !StringUtils.isEmpty(deParaUserDTO.getEmail()) &&
                deParaUserDTO.getUserKey() != null &&
                !StringUtils.isEmpty(deParaUserDTO.getUserKey())) {

            val map = new HashMap<String, Object>();
            map.put("JIRA_EMAIL", deParaUserDTO.getEmail());
            map.put("USER_KEY", deParaUserDTO.getUserKey());
            map.put("PONTO_EMAIL", deParaUserDTO.getPontoEmail());
            map.put("INFORMED_EMAIL", deParaUserDTO.getInformedEmail());
            DeParaUser user = ao.create(DeParaUser.class, map);
            user.save();
            if (deParaUserDTO.getInformedEmail() != null &&
                    !StringUtils.isEmpty(deParaUserDTO.getInformedEmail())) {
                registerDeParaUserChange(deParaUserDTO.getUserKey(),
                        null,
                        deParaUserDTO.getInformedEmail(),
                        actorKey);
            }
        }
    }

    public void updateDeParaUser(DeParaUserDTO deParaUserDTO, String actorKey) {
        log.info("updateDeParaUser: deParaUserDTO = " + deParaUserDTO + ", actorKey = " + actorKey);
        DeParaUser[] deParaUsers = ao.find(DeParaUser.class, Query.select().where("\"USER_KEY\" = ?", deParaUserDTO.getUserKey()));
        if (deParaUsers != null && deParaUsers.length == 1) {
            String previousInformedEmail = deParaUsers[0].getInformedEmail();
            deParaUsers[0].setInformedEmail(deParaUserDTO.getInformedEmail());
            deParaUsers[0].setPontoEmail(deParaUserDTO.getPontoEmail());
            deParaUsers[0].save();
            if (
                    (previousInformedEmail != null &&
                            !StringUtils.isEmpty(previousInformedEmail) &&
                            !previousInformedEmail.equalsIgnoreCase(deParaUserDTO.getInformedEmail())
                    ) ||
                            (deParaUserDTO.getInformedEmail() != null &&
                                    !StringUtils.isEmpty(deParaUserDTO.getInformedEmail()) &&
                                    !deParaUserDTO.getInformedEmail().equalsIgnoreCase(previousInformedEmail)
                            )
            ) {
                registerDeParaUserChange(deParaUserDTO.getUserKey(),
                        previousInformedEmail,
                        deParaUserDTO.getInformedEmail(),
                        actorKey);
            }
        }
    }

    private void registerDeParaUserChange(String userKey, String previousInformedEmail, String informedEmail, String actorKey) {
        log.info("registerDeParaUserChange: userKey = " + userKey + ", previousInformedEmail = " + previousInformedEmail + ", informedEmail =  " + informedEmail + ", actorKey = " + actorKey);
        val map = new HashMap<String, Object>();
        map.put("ACTOR_KEY", actorKey);
        map.put("USER_KEY", userKey);
        map.put("UPDATED_DATE", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        map.put("PREVIOUS_INFORMED_EMAIL", previousInformedEmail);
        map.put("INFORMED_EMAIL", informedEmail);
        AuditDeParaUser auditDeParaUser = ao.create(AuditDeParaUser.class, map);
    }

    public List<AuditDeParaUserDTO> getAuditHistory(String[] userKeys) {
        log.info("getAuditHistory: userKeys = " + userKeys);
        List<AuditDeParaUserDTO> list = new ArrayList<>();
        String userKeysString = "'" + String.join("','", userKeys) + "'";

        try (LegacySQLProcessor sqlProcessor = this.createSQLProcessor()){
            sqlProcessor.prepareStatement(SQL_AUDIT.replaceAll("%%USER_KEYS%%", userKeysString));
            ResultSet result = sqlProcessor.executeQuery();
            while (result.next()) {
                AuditDeParaUserDTO dto = AuditDeParaUserDTO.builder()
                        .updatedDate(ConversionUtils.formatISODateTimeAsRegularDateTime(
                                result.getString("UPDATED_DATE").replace(" ", "T")))
                        .actorName(result.getString("DISPLAY_NAME"))
                        .informedEmail(result.getString("INFORMED_EMAIL"))
                        .previousInformedEmail(result.getString("PREVIOUS_INFORMED_EMAIL"))
                        .userKey(result.getString("USER_KEY"))
                        .id(result.getString("ID"))
                        .build();
                list.add(dto);
            }

        } catch (Exception e) {
            log.error("DAO: getAuditHistory error: " + e.getMessage(), e);
        }

        return list;
    }
}
