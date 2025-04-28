package com.mindproapps.jira.integracaoponto.service.i18n;

import java.util.Locale;
import java.util.Map;

public interface I18nService {
    Map<String, String> getTextsForPage(String page);
    Map<String, String> getTexts(String prefix);
    Map<String, String> getTextsForLocale(String prefix, Locale locale);
}
