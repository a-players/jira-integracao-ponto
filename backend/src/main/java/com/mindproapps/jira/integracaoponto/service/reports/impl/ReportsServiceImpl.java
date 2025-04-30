package com.mindproapps.jira.integracaoponto.service.reports.impl;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.ApplicationUser;
import com.mindproapps.jira.integracaoponto.dao.reports.ReportsDAO;
import com.mindproapps.jira.integracaoponto.exception.NoLoggedUserException;
import com.mindproapps.jira.integracaoponto.model.dto.period.TimesheetsPeriodDTO;
import com.mindproapps.jira.integracaoponto.model.dto.ponto.PontoDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.*;
import com.mindproapps.jira.integracaoponto.service.ponto.PontoService;
import com.mindproapps.jira.integracaoponto.service.reports.ReportsService;
import com.mindproapps.jira.integracaoponto.util.ConversionUtils;
import com.mindproapps.jira.integracaoponto.validator.PeriodValidator;
import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Named
@Singleton
@Log4j
public class ReportsServiceImpl implements ReportsService {

    @Inject
    private ReportsDAO reportsDAO;

    @Inject
    private PeriodValidator periodValidator;

    @Inject
    private PontoService pontoService;

    @Override
    public TimesheetReportsResponseDTO getTimesheetsApproved(String startDate, String endDate, Integer teamId) {
        log.info("getTimesheetsApproved: startDate = " + startDate + ", endDate = " + endDate + ", teamId = " + teamId);
        LocalDate start = ConversionUtils.convertISODateStringToLocalDate(startDate);
        LocalDate end = ConversionUtils.convertISODateStringToLocalDate(endDate);
        periodValidator.validateDateInterval(start, end);

        TimesheetReportsResponseDTO responseDTO = new TimesheetReportsResponseDTO();
        List<TimesheetReportsDTO> all = reportsDAO.getTimesheetsApproved(
                ConversionUtils.convertLocalDateToISODateString(start),
                ConversionUtils.convertLocalDateToISODateString(end));

        if (teamId != null && teamId != 0) {
            all = all.stream()
                    .filter(dto -> dto.getTeamId() != null && dto.getTeamId().intValue() == teamId.intValue())
                    .collect(Collectors.toList());
        }

        List<String> lstEmails = new ArrayList<>();
        all.forEach(dto -> {
            dto.updateEmailPonto();
            if (!lstEmails.contains(dto.getEmailPonto())) {
                lstEmails.add(dto.getEmailPonto());
            }
        });

        List<PontoDTO> allData = pontoService.getAllData(lstEmails, start, end);
        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        if (user == null || !user.isActive()) {
            throw new NoLoggedUserException();
        }
        pontoService.setPontoHours(allData, all, user.getKey());

        responseDTO.setApprovedTimesheetsList(all);
        return responseDTO;
    }

    @Override
    public TimesheetReportsResponseDTO getTimesheetsSubmitted(String startDate, String endDate, Integer teamId) {
        log.info("getTimesheetsSubmitted: startDate = " + startDate + ", endDate = " + endDate + ", teamId = " + teamId);
        LocalDate start = ConversionUtils.convertISODateStringToLocalDate(startDate);
        LocalDate end = ConversionUtils.convertISODateStringToLocalDate(endDate);
        periodValidator.validateDateInterval(start, end);

        TimesheetReportsResponseDTO responseDTO = new TimesheetReportsResponseDTO();
        List<TimesheetReportsDTO> all = reportsDAO.getTimesheetsSubmitted(
                ConversionUtils.convertLocalDateToISODateString(start),
                ConversionUtils.convertLocalDateToISODateString(end));

        if (teamId != null && teamId != 0) {
            all = all.stream()
                    .filter(dto -> dto.getTeamId() != null && dto.getTeamId().intValue() == teamId.intValue())
                    .collect(Collectors.toList());
        }

        List<String> lstEmails = new ArrayList<>();
        all.forEach(dto -> {
            dto.updateEmailPonto();
            if (!lstEmails.contains(dto.getEmailPonto())) {
                lstEmails.add(dto.getEmailPonto());
            }
        });

        List<PontoDTO> allData = pontoService.getAllData(lstEmails, start, end);
        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        if (user == null || !user.isActive()) {
            throw new NoLoggedUserException();
        }
        pontoService.setPontoHours(allData, all, user.getKey());

        responseDTO.setSubmittedTimesheetsList(all);
        return responseDTO;
    }

    @Override
    public HoursReportsResponseDTO getUndersubmittedHours(String startDate, String endDate, Integer teamId) {
        log.info("getUndersubmittedHours: startDate = " + startDate + ", endDate = " + endDate + ", teamId = " + teamId);
        LocalDate start = ConversionUtils.convertISODateStringToLocalDate(startDate);
        LocalDate end = ConversionUtils.convertISODateStringToLocalDate(endDate);
        periodValidator.validateDateInterval(start, end);

        HoursReportsResponseDTO hoursReportsResponseDTO = new HoursReportsResponseDTO();
        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        if (user == null || !user.isActive()) {
            throw new NoLoggedUserException();
        }

        List<PartialTimesheetReportsDTO> list = reportsDAO.getTimesheetsSubmittedPartialHours(startDate, endDate);

        if (teamId != null && teamId != 0) {
            list = list.stream()
                    .filter(dto -> dto.getTeamId() != null && dto.getTeamId().intValue() == teamId.intValue())
                    .collect(Collectors.toList());
        }

        List<String> lstEmails = new ArrayList<>();
        list.forEach(dto -> {
            dto.updateEmailPonto();
            if (!lstEmails.contains(dto.getEmailPonto())) {
                lstEmails.add(dto.getEmailPonto());
            }
        });

        List<PontoDTO> allData = pontoService.getAllData(lstEmails, start, end);
        pontoService.setPontoHours(allData, list, user.getKey());

        hoursReportsResponseDTO.setPartialTimesheetReportsDTOList(list);
        return hoursReportsResponseDTO;
    }

    @Override
    public HoursReportsResponseDTO getUnsubmittedHours(List<TimesheetsPeriodDTO> periods, Integer teamId) {
        log.info("getUnsubmittedHours: periods = " + periods + ", teamId = " + teamId);
        HoursReportsResponseDTO hoursReportsResponseDTO = new HoursReportsResponseDTO();
        hoursReportsResponseDTO.setUnsubmittedHoursDTOList(Collections.synchronizedList(new ArrayList<>()));
        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        if (user == null || !user.isActive()) {
            throw new NoLoggedUserException();
        }

        if (periods != null && !periods.isEmpty()) {
            periods.parallelStream().forEach(period -> {
                List<UnsubmittedHoursDTO> list = reportsDAO.getUnsubmittedHours(period.getStartDate(), period.getEndDate());

                List<String> lstEmails = new ArrayList<>();
                list.forEach(dto -> {
                    dto.updateEmailPonto();
                    if (!lstEmails.contains(dto.getEmailPonto())) {
                        lstEmails.add(dto.getEmailPonto());
                    }
                });

                List<PontoDTO> allData = pontoService.getAllData(lstEmails,
                        LocalDate.parse(period.getStartDate()),
                        LocalDate.parse(period.getEndDate()));
                pontoService.setPontoHours(allData, list, user.getKey());

                hoursReportsResponseDTO.getUnsubmittedHoursDTOList().addAll(list);
            });
        }

        if (teamId != null && teamId != 0) {
            hoursReportsResponseDTO.setUnsubmittedHoursDTOList(
                    hoursReportsResponseDTO.getUnsubmittedHoursDTOList().stream()
                            .filter(dto -> dto.getTeamId() != null && dto.getTeamId().intValue() == teamId.intValue())
                            .collect(Collectors.toList())
            );
        }

        return hoursReportsResponseDTO;
    }

    @Override
    public HoursReportsResponseDTO getAccountHours(String startDate, String endDate, Integer accountId, Integer categoryaccountId) {
        log.info("getAccountHours: startDate = " + startDate + ", endDate = " + endDate + ", accountId = " + accountId + ", categoryaccountId = " + categoryaccountId);
        LocalDate start = ConversionUtils.convertISODateStringToLocalDate(startDate);
        LocalDate end = ConversionUtils.convertISODateStringToLocalDate(endDate);
        periodValidator.validateDateInterval(start, end);

        HoursReportsResponseDTO responseDTO = new HoursReportsResponseDTO();
        List<AccountTimesheetReportsDTO> list = reportsDAO.getAccountHours(
                ConversionUtils.convertLocalDateToISODateString(start),
                ConversionUtils.convertLocalDateToISODateString(end));

        if (accountId != null && accountId != 0) {
            list = list.stream()
                    .filter(dto -> dto.getAccountId() != null && dto.getAccountId().intValue() == accountId.intValue())
                    .collect(Collectors.toList());
        }

        if (categoryaccountId != null && categoryaccountId != 0) {
            list = list.stream()
                    .filter(dto -> dto.getCategoryaccountId() != null && dto.getCategoryaccountId().intValue() == categoryaccountId.intValue())
                    .collect(Collectors.toList());
        }

        responseDTO.setAccountHoursDTOList(list);
        return responseDTO;
    }

    @Override
    public List<TeamDTO> getAllTeams() {
        return reportsDAO.getAllTeams();
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        return reportsDAO.getAllAccounts();
    }

    @Override
    public List<CategoryAccountDTO> getAllCategoryAccounts() {
        return reportsDAO.getAllCategoryAccounts();
    }
}
