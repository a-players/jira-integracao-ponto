package com.mindproapps.jira.integracaoponto.model.user;

import com.mindproapps.jira.integracaoponto.model.base.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.Indexed;
import net.java.ao.schema.NotNull;

@Preload
public interface DeParaUser extends Entity {

    @Indexed
    @NotNull
    String getJiraEmail();

    void setJiraEmail(String jiraEmail);

    @Indexed
    @NotNull
    String getUserKey();

    void setUserKey(String userKey);

    @Indexed
    String getPontoEmail();

    void setPontoEmail(String pontoEmail);

    @Indexed
    String getInformedEmail();

    void setInformedEmail(String informedEmail);

}
