package com.mindproapps.jira.integracaoponto.dao.user.jira;

import com.mindproapps.jira.integracaoponto.dao.base.BaseDAO;
import com.mindproapps.jira.integracaoponto.model.dto.user.DeParaUserDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.GroupNameDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.UsernameUserKeyDTO;
import com.mindproapps.jira.integracaoponto.util.LegacySQLProcessor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Component
public class JiraUserDAO extends BaseDAO {

    public List<DeParaUserDTO> listJiraUsers(Integer startRecord, Integer numberOfRecords, Integer DeParaType, String groups) {
        log.info("listJiraUsers: startRecord = " + startRecord + ", numberOfRecords = " + numberOfRecords + ", DeParaType = " + DeParaType + ", groups = " + groups);
        List<DeParaUserDTO> list = new ArrayList<>();
        try {
            LegacySQLProcessor sqlProcessor = this.createSQLProcessor();
            StringBuilder sql = new StringBuilder("SELECT DISTINCT ui.\"USER_KEY\", ui.\"USER_NAME\" , ui.\"EMAIL\", \n" +
                    "dp.\"JIRA_EMAIL\", dp.\"PONTO_EMAIL\", dp.\"INFORMED_EMAIL\"\n" +
                    "FROM \"AO_AEFED0_USER_INDEX\" ui \n" +
                    "LEFT JOIN \"AO_441C88_DE_PARA_USER\" dp ON dp.\"USER_KEY\" = ui.\"USER_KEY\" \n" +
                    "JOIN cwd_user cu ON cu.\"user_name\" = ui.\"USER_NAME\" \n" +
                    "JOIN cwd_directory cd ON cu.directory_id = cd.id \n");

            if (groups != null && !StringUtils.isEmpty(groups)) {
                sql.append("WHERE EXISTS (SELECT 1 FROM cwd_membership cm WHERE cm.child_name = ui.\"USER_NAME\" AND cm.lower_parent_name IN(")
                        .append(groups)
                        .append(")) ");
            } else {
                sql.append("WHERE EXISTS (SELECT 1 FROM cwd_membership cm WHERE cm.child_name = ui.\"USER_NAME\" AND cm.lower_parent_name LIKE 'jira%') ");
            }

            if (DeParaType == 1) {
                sql.append("AND dp.\"PONTO_EMAIL\" IS NULL AND dp.\"INFORMED_EMAIL\" IS NULL ");
            } else if (DeParaType == 2) {
                sql.append("AND dp.\"INFORMED_EMAIL\" IS NOT NULL ");
            }

            sql.append("AND cd.lower_directory_name = 'linx ad v.01' \n" +
                    "AND ui.\"EMAIL\" NOT LIKE '%@terceiroslinx.com.br' \n" +
                    "AND ui.\"EMAIL\" NOT LIKE '%@franqueadolinx.com.br' \n" +
                    "AND ui.\"EMAIL\" NOT LIKE '%@parceiroslinx.com.br' \n" +
                    "ORDER BY ui.\"USER_NAME\" ");

            if (startRecord != null && numberOfRecords != null) {
                sql.append("OFFSET ? LIMIT ?");
            }

            sqlProcessor.prepareStatement(sql.toString());

            if (startRecord != null && numberOfRecords != null) {
                sqlProcessor.setValue(startRecord - 1);
                sqlProcessor.setValue(numberOfRecords);
            }

            ResultSet result = sqlProcessor.executeQuery();
            int rn = startRecord != null ? startRecord : 1;
            while (result.next()) {
                list.add(DeParaUserDTO.builder()
                        .informedEmail(result.getString("INFORMED_EMAIL"))
                        .email(result.getString("EMAIL"))
                        .jiraEmail(result.getString("JIRA_EMAIL"))
                        .pontoEmail(result.getString("PONTO_EMAIL"))
                        .userKey(result.getString("USER_KEY"))
                        .username(result.getString("USER_NAME"))
                        .rn(rn++)
                        .build());
            }
        } catch (Exception e) {
            log.error("DAO: listJiraUsers error: " + e.getMessage(), e);
        }
        return list;
    }

    public List<DeParaUserDTO> listJiraUsersByKey(String userKey) {
        log.info("listJiraUsersByKey: userKey = " + userKey);
        List<DeParaUserDTO> list = new ArrayList<>();
        try {
            LegacySQLProcessor sqlProcessor = this.createSQLProcessor();
            String sql = "SELECT DISTINCT ui.\"USER_KEY\", ui.\"USER_NAME\" , ui.\"EMAIL\", " +
                    "dp.\"JIRA_EMAIL\", dp.\"PONTO_EMAIL\", dp.\"INFORMED_EMAIL\" " +
                    "FROM \"AO_AEFED0_USER_INDEX\" ui " +
                    "LEFT JOIN \"AO_441C88_DE_PARA_USER\" dp ON dp.\"USER_KEY\" = ui.\"USER_KEY\" " +
                    "JOIN cwd_user cu ON cu.\"user_name\" = ui.\"USER_NAME\" " +
                    "JOIN cwd_directory cd ON cu.directory_id = cd.id " +
                    "WHERE ui.\"USER_KEY\" = ? " +
                    "AND cd.lower_directory_name = 'linx ad v.01' " +
                    "AND ui.\"EMAIL\" NOT LIKE '%@terceiroslinx.com.br' " +
                    "AND ui.\"EMAIL\" NOT LIKE '%@franqueadolinx.com.br' " +
                    "AND ui.\"EMAIL\" NOT LIKE '%@parceiroslinx.com.br' " +
                    "ORDER BY ui.\"USER_NAME\"";
            sqlProcessor.prepareStatement(sql);
            sqlProcessor.setValue(userKey);
            ResultSet result = sqlProcessor.executeQuery();
            int rn = 1;
            while (result.next()) {
                list.add(DeParaUserDTO.builder()
                        .informedEmail(result.getString("INFORMED_EMAIL"))
                        .email(result.getString("EMAIL"))
                        .jiraEmail(result.getString("JIRA_EMAIL"))
                        .pontoEmail(result.getString("PONTO_EMAIL"))
                        .userKey(result.getString("USER_KEY"))
                        .username(result.getString("USER_NAME"))
                        .rn(rn++)
                        .build());
            }
        } catch (Exception e) {
            log.error("DAO: listJiraUsersByKey error: " + e.getMessage(), e);
        }
        return list;
    }

    public String getEmailByKey(String userKey) {
        log.info("getEmailByKey: userKey = " + userKey);
        try {
            LegacySQLProcessor sqlProcessor = this.createSQLProcessor();
            String sql = "SELECT DISTINCT ui.\"EMAIL\" " +
                    "FROM \"AO_AEFED0_USER_INDEX\" ui " +
                    "JOIN cwd_user cu ON cu.\"user_name\" = ui.\"USER_NAME\" " +
                    "JOIN cwd_directory cd ON cu.directory_id = cd.id " +
                    "WHERE ui.\"USER_KEY\" = ? " +
                    "AND cd.lower_directory_name = 'linx ad v.01' " +
                    "AND ui.\"EMAIL\" NOT LIKE '%@terceiroslinx.com.br' " +
                    "AND ui.\"EMAIL\" NOT LIKE '%@franqueadolinx.com.br' " +
                    "AND ui.\"EMAIL\" NOT LIKE '%@parceiroslinx.com.br'";
            sqlProcessor.prepareStatement(sql);
            sqlProcessor.setValue(userKey);
            ResultSet result = sqlProcessor.executeQuery();
            if (result.next()) {
                return result.getString("EMAIL");
            }
        } catch (Exception e) {
            log.error("DAO: getEmailByKey error: " + e.getMessage(), e);
        }
        return null;
    }

    public String getNameByKey(String userKey) {
        log.info("getNameByKey: userKey = " + userKey);
        try {
            LegacySQLProcessor sqlProcessor = this.createSQLProcessor();
            String sql = "SELECT DISTINCT ui.\"DISPLAY_NAME\" " +
                    "FROM \"AO_AEFED0_USER_INDEX\" ui " +
                    "JOIN cwd_user cu ON cu.\"user_name\" = ui.\"USER_NAME\" " +
                    "JOIN cwd_directory cd ON cu.directory_id = cd.id " +
                    "WHERE ui.\"USER_KEY\" = ? " +
                    "AND cd.lower_directory_name = 'linx ad v.01' " +
                    "AND ui.\"EMAIL\" NOT LIKE '%@terceiroslinx.com.br' " +
                    "AND ui.\"EMAIL\" NOT LIKE '%@franqueadolinx.com.br' " +
                    "AND ui.\"EMAIL\" NOT LIKE '%@parceiroslinx.com.br'";
            sqlProcessor.prepareStatement(sql);
            sqlProcessor.setValue(userKey);
            ResultSet result = sqlProcessor.executeQuery();
            if (result.next()) {
                return result.getString("DISPLAY_NAME");
            }
        } catch (Exception e) {
            log.error("DAO: getNameByKey error: " + e.getMessage(), e);
        }
        return null;
    }

    public Integer getTotalJiraUsers(Integer DeParaType, String groups) {
        log.info("getTotalJiraUsers: DeParaType = " + DeParaType + ", groups = " + groups);
        try {
            LegacySQLProcessor sqlProcessor = this.createSQLProcessor();
            StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT ui.\"USER_KEY\") AS COUNT " +
                    "FROM \"AO_AEFED0_USER_INDEX\" ui " +
                    "LEFT JOIN \"AO_441C88_DE_PARA_USER\" dp ON dp.\"USER_KEY\" = ui.\"USER_KEY\" " +
                    "JOIN cwd_user cu ON cu.\"user_name\" = ui.\"USER_NAME\" " +
                    "JOIN cwd_directory cd ON cu.directory_id = cd.id ");

            if (groups != null && !StringUtils.isEmpty(groups)) {
                sql.append("WHERE EXISTS (SELECT 1 FROM cwd_membership cm WHERE cm.child_name = ui.\"USER_NAME\" AND cm.lower_parent_name IN(")
                        .append(groups)
                        .append(")) ");
            } else {
                sql.append("WHERE EXISTS (SELECT 1 FROM cwd_membership cm WHERE cm.child_name = ui.\"USER_NAME\" AND cm.lower_parent_name LIKE 'jira%') ");
            }

            if (DeParaType == 1) {
                sql.append("AND dp.\"PONTO_EMAIL\" IS NULL AND dp.\"INFORMED_EMAIL\" IS NULL ");
            } else if (DeParaType == 2) {
                sql.append("AND dp.\"INFORMED_EMAIL\" IS NOT NULL ");
            }

            sql.append("AND cd.lower_directory_name = 'linx ad v.01' " +
                    "AND ui.\"EMAIL\" NOT LIKE '%@terceiroslinx.com.br' " +
                    "AND ui.\"EMAIL\" NOT LIKE '%@franqueadolinx.com.br' " +
                    "AND ui.\"EMAIL\" NOT LIKE '%@parceiroslinx.com.br' ");

            sqlProcessor.prepareStatement(sql.toString());
            ResultSet result = sqlProcessor.executeQuery();
            if (result.next()) {
                return result.getInt("COUNT");
            }
        } catch (Exception e) {
            log.error("DAO: getTotalJiraUsers error: " + e.getMessage(), e);
        }
        return -1;
    }

    public List<UsernameUserKeyDTO> listAllJiraUsernames() {
        List<UsernameUserKeyDTO> list = new ArrayList<>();
        try {
            LegacySQLProcessor sqlProcessor = this.createSQLProcessor();
            String sql = "SELECT DISTINCT ui.\"USER_KEY\", ui.\"USER_NAME\" " +
                    "FROM \"AO_AEFED0_USER_INDEX\" ui " +
                    "JOIN cwd_user cu ON cu.\"user_name\" = ui.\"USER_NAME\" " +
                    "JOIN cwd_directory cd ON cu.directory_id = cd.id " +
                    "WHERE cd.lower_directory_name = 'linx ad v.01' " +
                    "AND ui.\"EMAIL\" NOT LIKE '%@terceiroslinx.com.br' " +
                    "AND ui.\"EMAIL\" NOT LIKE '%@franqueadolinx.com.br' " +
                    "AND ui.\"EMAIL\" NOT LIKE '%@parceiroslinx.com.br' " +
                    "ORDER BY ui.\"USER_NAME\"";
            sqlProcessor.prepareStatement(sql);
            ResultSet result = sqlProcessor.executeQuery();
            while (result.next()) {
                list.add(UsernameUserKeyDTO.builder()
                        .id(result.getString("USER_KEY"))
                        .label(result.getString("USER_NAME"))
                        .build());
            }
        } catch (Exception e) {
            log.error("DAO: listAllJiraUsernames error: " + e.getMessage(), e);
        }
        return list;
    }

    public List<GroupNameDTO> listAllJiraAccessGroups() {
        List<GroupNameDTO> list = new ArrayList<>();
        try {
            LegacySQLProcessor sqlProcessor = this.createSQLProcessor();
            String sql = "SELECT DISTINCT cm.parent_name , cm.lower_parent_name " +
                    "FROM cwd_membership cm " +
                    "WHERE lower_parent_name LIKE 'jira%' " +
                    "ORDER BY parent_name";
            sqlProcessor.prepareStatement(sql);
            ResultSet result = sqlProcessor.executeQuery();
            while (result.next()) {
                list.add(GroupNameDTO.builder()
                        .label(result.getString("parent_name"))
                        .value(result.getString("lower_parent_name"))
                        .build());
            }
        } catch (Exception e) {
            log.error("DAO: listAllJiraAccessGroups error: " + e.getMessage(), e);
        }
        return list;
    }
}
