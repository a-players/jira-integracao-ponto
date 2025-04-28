package com.mindproapps.jira.integracaoponto.util;

import com.mindproapps.jira.integracaoponto.model.dto.reports.UnsubmittedHoursDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class UnsubmittedHoursDTOComparator implements Comparator<UnsubmittedHoursDTO> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Override
    public int compare(UnsubmittedHoursDTO o1, UnsubmittedHoursDTO o2) {
            if(o1 != null) {
                if(o2 != null) {
                    if(o1.getPeriodStartDate() != null) {
                        LocalDate date1 = LocalDate.parse(o1.getPeriodStartDate(), formatter);
                        if(o2.getPeriodStartDate() != null) {
                            LocalDate date2 = LocalDate.parse(o2.getPeriodStartDate(), formatter);
                            return date1.isBefore(date2) ? -1 : date1.isAfter(date2) ? 1: 0;
                        } else {
                            return 1;
                        }
                    } else {
                        return -1;
                    }
                } else {
                    return 1;
                }
            } else if(o2 != null) {
                return -1;
            }
        return 0;
    }
}
