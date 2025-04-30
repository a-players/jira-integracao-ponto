package com.mindproapps.jira.integracaoponto.listener;

import com.atlassian.event.api.EventPublisher;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import lombok.extern.log4j.Log4j;
import com.atlassian.plugin.event.PluginEventListener;
import com.atlassian.plugin.event.events.PluginEnabledEvent;
import com.mindproapps.jira.integracaoponto.dao.base.TriggerCreatorDAO;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@Log4j
public class PluginEventsListener extends GenericListener {

    private final EventPublisher eventPublisher;
    private Object instance;
    private final TriggerCreatorDAO triggerCreatorDAO;

    @Inject
    public PluginEventsListener(@ComponentImport EventPublisher eventPublisher, TriggerCreatorDAO triggerCreatorDAO) {
        log.info("PluginEventsListener: eventPublisher = " + eventPublisher);
        this.eventPublisher = eventPublisher;
        this.triggerCreatorDAO = triggerCreatorDAO;
        this.instance = this;
    }

    @PluginEventListener
    public void onPluginEnabledEvent(PluginEnabledEvent enabledEvent) {
        log.info("onPluginEnabledEvent: enabledEvent = " + enabledEvent);
        String key = enabledEvent.getPlugin().getKey();
        if ("com.mindproapps.jira.integracaoponto".equalsIgnoreCase(key)) {
            new Thread(() -> {
                try {
                    triggerCreatorDAO.createDatabaseTrigger();
                    log.info("Trigger criada com sucesso.");
                } catch (Exception e) {
                    log.error("Erro ao criar trigger", e);
                }
            }).start();
        }
    }

    @Override
    public EventPublisher getEventPublisher() {
        return this.eventPublisher;
    }

    @Override
    public Object getInstance() {
        return this.instance;
    }

    @Override
    public void setInstance(Object instance) {
        this.instance = instance;
    }

    @Override
    public void destroy() {
        this.eventPublisher.unregister(this);
        log.trace("plugin uninstalled");
        try {
            triggerCreatorDAO.removeDatabaseTrigger();
        } catch (Exception e) {
            log.error("Erro ao remover trigger", e);
        }
    }
}