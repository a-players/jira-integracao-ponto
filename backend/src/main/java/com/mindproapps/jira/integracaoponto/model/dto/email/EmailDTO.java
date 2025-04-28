package com.mindproapps.jira.integracaoponto.model.dto.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {
    private String approverKey;
    private String comment;
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String userKey;
    private String hoursRequired;
    private String hoursSubmitted;
}
