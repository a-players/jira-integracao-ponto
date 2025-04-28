package com.mindproapps.jira.integracaoponto.model.base;

import net.java.ao.RawEntity;
import net.java.ao.schema.AutoIncrement;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.PrimaryKey;

public interface Entity extends RawEntity<Integer> {

  @AutoIncrement
  @NotNull
  @PrimaryKey("ID")
  Long getID();

}
