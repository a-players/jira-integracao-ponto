package com.mindproapps.jira.integracaoponto.service.user;

import com.mindproapps.jira.integracaoponto.model.dto.user.DeParaUserDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.DeParaUserListDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.DeParaUserListRequestDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.GroupNameDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.UsernameUserKeyDTO;

import java.util.List;

public interface UserService {
    DeParaUserListDTO getUsersList(DeParaUserListRequestDTO requestDTO);
    void updateDeParaUsers(List<DeParaUserDTO> deParaUserDTOList);
    DeParaUserListDTO getListByKey(String userKey);
    List<UsernameUserKeyDTO> getAllUsernameUserKeyList();
    List<GroupNameDTO> getAllGroups();
}
