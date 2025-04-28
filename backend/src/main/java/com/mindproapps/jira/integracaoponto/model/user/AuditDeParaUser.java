package com.mindproapps.jira.integracaoponto.model.user;

import com.mindproapps.jira.integracaoponto.model.base.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.Indexed;
import net.java.ao.schema.NotNull;

@Preload
public interface AuditDeParaUser extends Entity {

    @Indexed
    @NotNull
    String getUserKey();

    void setUserKey(String userKey);

    @Indexed
    String getActorKey();

    void setActorKey(String actorKey);

    @Indexed
    String getInformedEmail();

    void setInformedEmail(String informedEmail);

    @Indexed
    String getPreviousInformedEmail();

    void setPreviousInformedEmail(String previousInformedEmail);

    @Indexed
    @NotNull
    String getUpdatedDate();

    public void setUpdatedDate(String updatedDate);
}
