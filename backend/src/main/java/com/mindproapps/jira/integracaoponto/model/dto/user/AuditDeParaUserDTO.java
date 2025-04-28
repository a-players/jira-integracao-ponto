package com.mindproapps.jira.integracaoponto.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect
public class AuditDeParaUserDTO {
    private String userKey;
    private String actorName;
    private String informedEmail;
    private String previousInformedEmail;
    private String updatedDate;
    private String id;
}
