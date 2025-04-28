package com.mindproapps.jira.integracaoponto.listener;

import com.atlassian.event.api.EventPublisher;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

@Log4j
public abstract class GenericListener implements InitializingBean, DisposableBean {

  public abstract EventPublisher getEventPublisher();

  public abstract Object getInstance();

  public abstract void setInstance(Object instance);

  public String getName() {
    return getInstance().getClass().getSimpleName();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    log.info("Enabling " + this.getName());
    this.getEventPublisher().register(this.getInstance());
    log.trace("listener registrado" + this.getEventPublisher().getClass().getName());

  }

  /**
   * Called when the plugin is being disabled or removed.
   * @throws Exception
   */
  @Override
  public void destroy() throws Exception {
    log.info("Disabling " + this.getName());
    this.getEventPublisher().unregister(this.getInstance());
  }

}
