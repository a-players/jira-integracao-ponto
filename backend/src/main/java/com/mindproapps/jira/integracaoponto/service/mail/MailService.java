package com.mindproapps.jira.integracaoponto.service.mail;

import com.mindproapps.jira.integracaoponto.model.dto.email.EmailDTO;

public interface MailService {
    void sendApproval(EmailDTO emailDTO);
    void sendReopen(EmailDTO emailDTO);
    void sendRejection(EmailDTO emailDTO);
    void sendSubmitted(EmailDTO emailDTO);
}
