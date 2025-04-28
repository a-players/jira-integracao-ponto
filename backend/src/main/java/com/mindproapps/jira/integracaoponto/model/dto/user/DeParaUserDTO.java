package com.mindproapps.jira.integracaoponto.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect
public class DeParaUserDTO {
    private String userKey;
    private String username;
    private String email;
    @JsonIgnore
    private String jiraEmail;
    private String pontoEmail;
    private String informedEmail;
    private Integer rn;
    private List<AuditDeParaUserDTO> history;
}
