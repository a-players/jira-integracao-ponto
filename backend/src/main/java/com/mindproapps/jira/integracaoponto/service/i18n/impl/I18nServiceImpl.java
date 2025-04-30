package com.mindproapps.jira.integracaoponto.service.i18n.impl;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.util.I18nHelper;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.message.I18nResolver;
import com.mindproapps.jira.integracaoponto.service.i18n.I18nService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Named
@Singleton
public class I18nServiceImpl implements I18nService {


    private final I18nResolver i18n;

    @Inject
    public I18nServiceImpl(I18nResolver i18n) {
        this.i18n = i18n;
    }

    @Override
    public Map<String, String> getTextsForPage(String page) {
        return i18n.getAllTranslationsForPrefix(page + ".page.");
    }

    @Override
    public Map<String, String> getTexts(String prefix) {
        return i18n.getAllTranslationsForPrefix(prefix);
    }

    @Override
    public Map<String, String> getTextsForLocale(String prefix, Locale locale) {
        I18nHelper helper = ComponentAccessor.getI18nHelperFactory().getInstance(locale);
        Set<String> keys = helper.getKeysForPrefix(prefix);
        if (keys != null && !keys.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            keys.forEach(key -> map.put(key, helper.getText(key)));
            return map;
        }
        return null;
    }
}
