package com.mindproapps.jira.integracaoponto.model.dto;

import com.mindproapps.jira.integracaoponto.model.base.Entity;

public abstract class BaseDTO<E extends Entity> {
    public abstract void mapToDTO(E entity);
}
