package com.mindproapps.jira.integracaoponto.dao.base;

import lombok.extern.log4j.Log4j;
import org.ofbiz.core.entity.GenericEntityException;
import org.springframework.stereotype.Component;
import com.mindproapps.jira.integracaoponto.util.LegacySQLProcessor;

import java.sql.SQLException;

@Log4j
@Component
public class TriggerCreatorDAO extends BaseDAO{
    private static final String SQL_CREATE_FUNCTION = "CREATE OR REPLACE FUNCTION audit_period_log_fn() RETURNS TRIGGER AS $audit_period_trg$\n" +
            "    DECLARE v_previous_state TEXT;\n" +
            "    DECLARE v_current_state TEXT;\n" +
            "    BEGIN\n" +
            "        IF TG_OP != 'UPDATE' THEN\n" +
            "            v_previous_state := 'logs.status.openforall';\n" +
            "        ELSE\n" +
            "\t\t\tIF OLD.\"OPEN_FOR_TEAM_MEMBER\" = FALSE THEN\n" +
            "                IF OLD.\"OPEN_FOR_APPROVER\" = FALSE THEN\n" +
            "                    v_previous_state := 'logs.status.closedforall';\n" +
            "                ELSE\n" +
            "                    v_previous_state := 'logs.status.openforapprovers';\n" +
            "                END IF;\n" +
            "            ELSIF OLD.\"OPEN_FOR_APPROVER\" = TRUE THEN\n" +
            "                    v_previous_state := 'logs.status.openforall';\n" +
            "            END IF;\n" +
            "        END IF;\n" +
            "        IF NEW.\"OPEN_FOR_TEAM_MEMBER\" = FALSE THEN\n" +
            "        \tIF NEW.\"OPEN_FOR_APPROVER\" = FALSE THEN\n" +
            "            \tv_current_state := 'logs.status.closedforall';\n" +
            "            ELSE\n" +
            "                v_current_state := 'logs.status.openforapprovers';\n" +
            "            END if;\n" +
            "        ELSIF NEW.\"OPEN_FOR_APPROVER\" = TRUE THEN\n" +
            "        \tv_current_state := 'logs.status.openforall';\n" +
            "        END if;\n" +
            "        INSERT INTO public.\"AO_441C88_AUDIT_PERIOD\"(\"UPDATED_DATE\", \"PREVIOUS_STATE\", \"CURRENT_STATE\", \"PERIOD_YEAR_MONTH\", \"PERIOD_KEY\", \"PERIOD_ID\")\n" +
            "        VALUES(NOW(), v_previous_state, v_current_state, CONCAT(SUBSTRING(NEW.\"KEY\" FROM 3 FOR 2),SUBSTRING(NEW.\"KEY\" FROM 1 FOR 2)), NEW.\"KEY\", NEW.\"ID\");\n" +
            "        RETURN NEW;\n" +
            "    END\n" +
            "$audit_period_trg$ LANGUAGE plpgsql;";

    private static final String SQL_DELETE_TRIGGER = "DROP TRIGGER IF EXISTS audit_period_trg ON public.\"AO_86ED1B_PERIOD\";";
    private static final String SQL_CREATE_TRIGGER = "CREATE TRIGGER audit_period_trg\n" +
            "    AFTER INSERT OR UPDATE\n" +
            "    ON public.\"AO_86ED1B_PERIOD\"\n" +
            "    FOR EACH ROW EXECUTE PROCEDURE audit_period_log_fn();";

    private static final String SQL_DELETE_FUNCTION = "DROP FUNCTION IF EXISTS audit_period_log_fn()";

    public void createDatabaseTrigger() {
        log.trace("entering create trigger");

        try (LegacySQLProcessor processor = this.createSQLProcessor()) {
            processor.prepareStatement(SQL_CREATE_FUNCTION);
            processor.executeUpdate();

            processor.prepareStatement(SQL_DELETE_TRIGGER);
            processor.executeUpdate();

            processor.prepareStatement(SQL_CREATE_TRIGGER);
            processor.executeUpdate();
        } catch (Exception e) {
            log.error("Erro ao criar trigger no banco de dados", e);
        }
    }


    public void removeDatabaseTrigger() {
        log.trace("entering remove trigger");

        try (LegacySQLProcessor processor = this.createSQLProcessor()) {
            processor.prepareStatement(SQL_DELETE_TRIGGER);
            processor.executeUpdate();

            processor.prepareStatement(SQL_DELETE_FUNCTION);
            processor.executeUpdate();
        } catch (Exception e) {
            log.error("Erro ao remover trigger no banco de dados", e);
        }
    }
}
