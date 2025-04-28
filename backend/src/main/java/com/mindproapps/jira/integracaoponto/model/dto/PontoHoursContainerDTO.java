package com.mindproapps.jira.integracaoponto.model.dto;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@JsonAutoDetect
public class PontoHoursContainerDTO {
    protected String email;
    protected String emailPonto;
    protected String emailInformado;
    protected String jiraEmail;
    protected String horasPonto;
    protected String period;
    protected String periodStartDate;
    protected String periodEndDate;
    protected String periodStartDateISO;
    protected String periodEndDateISO;
    protected String workerKey;

    public void updateEmailPonto() {
        String emailPonto = this.getEmailPonto();
        String emailInformado = this.getEmailInformado();
        if (emailInformado != null) {
            this.setEmailPonto(emailInformado);
        } else if (emailPonto != null) {
            this.setEmailPonto(emailPonto);
        }
    }
}
