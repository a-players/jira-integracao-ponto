package com.mindproapps.jira.integracaoponto.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConversionUtils {
    private ConversionUtils() {
        super();
    }

    public static LocalDate convertISODateStringToLocalDate(String isoDate) {
        if(!StringUtils.isEmpty(isoDate)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(isoDate, formatter);
        }
        return LocalDate.now();
    }

    public static String convertLocalDateToISODateString(LocalDate date) {
        if(date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return formatter.format(date);
        }
        return null;
    }

    public static String formatISODateTimeAsRegularDateTime(String isoDateTime) {
        if(isoDateTime.lastIndexOf("-") > 7) {
            isoDateTime = isoDateTime.substring(0, isoDateTime.lastIndexOf("-"));
        }
        if(isoDateTime.lastIndexOf("+") > 7) {
            isoDateTime = isoDateTime.substring(0, isoDateTime.lastIndexOf("+"));
        }
        LocalDateTime localDateTime = LocalDateTime.parse(isoDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return formatter.format(localDateTime);
    }

    public static String formatISODateAsRegularDate(String isoDate) {
        LocalDate localDate = LocalDate.parse(isoDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return formatter.format(localDate);
    }
}
