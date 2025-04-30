package com.mindproapps.jira.integracaoponto.model.timesheets;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import com.mindproapps.jira.integracaoponto.model.timesheets.TimesheetActionOrigin;


@Getter
public enum TimesheetActionOrigin {
    INTEGRACAO_PONTO_APP(1),
    TEMPO_TIMESHEETS(2);

    private final Integer code;

    private static final Map<Integer, TimesheetActionOrigin> map = new HashMap<>();

    static {
        for(TimesheetActionOrigin origin : values()) {
            map.put(origin.getCode(), origin);
        }
    }

    private TimesheetActionOrigin(Integer code) {
        this.code = code;
    }

    public TimesheetActionOrigin getByCode(Integer code) {
        return map.get(code);
    }

}
