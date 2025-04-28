package com.mindproapps.jira.integracaoponto.listener;

import com.atlassian.event.api.EventPublisher;
import com.atlassian.plugin.event.PluginEventListener;
import com.atlassian.plugin.event.events.PluginEnabledEvent;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.mindproapps.jira.integracaoponto.dao.base.TriggerCreatorDAO;
import lombok.extern.log4j.Log4j;
import org.ofbiz.core.entity.GenericEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Log4j
@Component
public class PluginEventsListener extends GenericListener {

    @JiraImport
    protected EventPublisher eventPublisher;
    protected Object instance;

    @Autowired
    private TriggerCreatorDAO triggerCreatorDAO;

    @Autowired
    public PluginEventsListener(EventPublisher eventPublisher) {
        log.info("PluginEventsListener: eventPublisher = " + eventPublisher);
        this.eventPublisher = eventPublisher;
        this.instance = this;
    }

    @PluginEventListener
    public void onPluginEnabledEvent(PluginEnabledEvent enabledEvent) {
        log.info("onPluginEnabledEvent: enabledEvent = " + enabledEvent);
        String key = enabledEvent.getPlugin().getKey();
        if ("com.mindproapps.jira.integracaoponto".equalsIgnoreCase(key)) {
            triggerCreatorDAO.createDatabaseTrigger();
        }
    }


    @Override
    public EventPublisher getEventPublisher() {
        return this.eventPublisher;
    }

    @Override
    public Object getInstance() {
        return instance;
    }
    @Override
    public void setInstance(Object instance) {
        this.instance = instance;
    }

    @Override
    public void destroy() {
        this.eventPublisher.unregister(this);
        log.trace("plugin uninstalled");
        triggerCreatorDAO.removeDatabaseTrigger();
    }


}
