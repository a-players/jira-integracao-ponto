package com.mindproapps.jira.integracaoponto.model.dto.user;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@Getter
@Setter
@JsonAutoDetect
public class DeParaUserListDTO {
    List<DeParaUserDTO> list;
    Integer totalCount;
    Integer DeParaTypeOnRequest;
    List<Integer[]> pagesMap;
}
