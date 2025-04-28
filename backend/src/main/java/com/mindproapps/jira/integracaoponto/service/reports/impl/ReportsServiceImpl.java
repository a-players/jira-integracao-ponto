package com.mindproapps.jira.integracaoponto.service.reports.impl;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.ApplicationUser;
import com.mindproapps.jira.integracaoponto.dao.reports.ReportsDAO;
import com.mindproapps.jira.integracaoponto.exception.NoLoggedUserException;
import com.mindproapps.jira.integracaoponto.model.dto.period.TimesheetsPeriodDTO;
import com.mindproapps.jira.integracaoponto.model.dto.ponto.PontoDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.HoursReportsResponseDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.PartialTimesheetReportsDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.TeamDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.AccountDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.CategoryAccountDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.TimesheetReportsDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.TimesheetReportsResponseDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.UnsubmittedHoursDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.AccountTimesheetReportsDTO;
import com.mindproapps.jira.integracaoponto.service.ponto.PontoService;
import com.mindproapps.jira.integracaoponto.service.reports.ReportsService;
import com.mindproapps.jira.integracaoponto.util.ConversionUtils;
import com.mindproapps.jira.integracaoponto.util.UnsubmittedHoursDTOComparator;
import com.mindproapps.jira.integracaoponto.util.AccountHoursDTOComparator;
import com.mindproapps.jira.integracaoponto.validator.PeriodValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j
public class ReportsServiceImpl implements ReportsService {
    private ReportsDAO reportsDAO;
    private PeriodValidator periodValidator;
    private PontoService pontoService;

    @Autowired
    public ReportsServiceImpl(ReportsDAO reportsDAO, PeriodValidator periodValidator, PontoService pontoService) {
        log.info("ReportsServiceImpl: reportsDAO = " + reportsDAO + ", periodValidator = " + periodValidator + ", pontoService = " + pontoService);
        this.reportsDAO = reportsDAO;
        this.periodValidator = periodValidator;
        this.pontoService = pontoService;
    }
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
        if(teamId != null && teamId.intValue() != 0) {
            all = all.stream()
                    .filter(timesheetReportsDTO -> timesheetReportsDTO.getTeamId().intValue() == teamId.intValue())
                    .collect(Collectors.toList());            
        }

        List<String> lstEmails = new ArrayList<String>();
        all.forEach(dto -> {
            dto.updateEmailPonto();
            if (!lstEmails.contains(dto.getEmailPonto())) {
                lstEmails.add(dto.getEmailPonto());
            }
        });
        List<PontoDTO> allData = pontoService.getAllData(lstEmails, LocalDate.parse(startDate), LocalDate.parse(endDate)); 

        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        if (user == null || !user.isActive()) {
            throw new NoLoggedUserException();
        }
        String key = user.getKey();
        pontoService.setPontoHours(allData, all, key);
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
        if(teamId != null && teamId.intValue() != 0) {
            all = all.stream()
                    .filter(timesheetReportsDTO -> timesheetReportsDTO.getTeamId().intValue() == teamId.intValue())
                    .collect(Collectors.toList());
        }

        List<String> lstEmails = new ArrayList<String>();
        all.forEach(dto -> {
            dto.updateEmailPonto();
            if (!lstEmails.contains(dto.getEmailPonto())) {
                lstEmails.add(dto.getEmailPonto());
            }
        });
        List<PontoDTO> allData = pontoService.getAllData(lstEmails, LocalDate.parse(startDate), LocalDate.parse(endDate)); 
        
        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        if (user == null || !user.isActive()) {
            throw new NoLoggedUserException();
        }
        String key = user.getKey();
        pontoService.setPontoHours(allData, all, key);
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
        String key = user.getKey();

        List<PartialTimesheetReportsDTO> partialTimesheetReportsDTOS = reportsDAO.getTimesheetsSubmittedPartialHours(startDate, endDate);
        if(teamId != null && teamId.intValue() != 0) {
            partialTimesheetReportsDTOS = partialTimesheetReportsDTOS.stream()
                    .filter(timesheetReportsDTO -> timesheetReportsDTO.getTeamId().intValue() == teamId.intValue())
                    .collect(Collectors.toList());
        }

        List<String> lstEmails = new ArrayList<String>();
        partialTimesheetReportsDTOS.forEach(dto -> {
            dto.updateEmailPonto();
            if (!lstEmails.contains(dto.getEmailPonto())) {
                lstEmails.add(dto.getEmailPonto());
            }
        });
        List<PontoDTO> allData = pontoService.getAllData(lstEmails, LocalDate.parse(startDate), LocalDate.parse(endDate));
        
        pontoService.setPontoHours(allData, partialTimesheetReportsDTOS, key);
        hoursReportsResponseDTO.setPartialTimesheetReportsDTOList(partialTimesheetReportsDTOS);
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
        String key = user.getKey();
        if(periods != null && !periods.isEmpty()) {
            periods.parallelStream().forEach(period -> {
                        List<UnsubmittedHoursDTO> unsubmittedHoursDTOList = reportsDAO.getUnsubmittedHours(
                                period.getStartDate(),
                                period.getEndDate());

                        List<String> lstEmails = new ArrayList<String>();
                        unsubmittedHoursDTOList.forEach(dto -> {
                            dto.updateEmailPonto();
                            if (!lstEmails.contains(dto.getEmailPonto())) {
                                lstEmails.add(dto.getEmailPonto());
                            }
                        });        
                        List<PontoDTO> allData = pontoService.getAllData(lstEmails, LocalDate.parse(period.getStartDate()), LocalDate.parse(period.getEndDate()));
                        
                        pontoService.setPontoHours(allData, unsubmittedHoursDTOList, key);
                        hoursReportsResponseDTO.getUnsubmittedHoursDTOList().addAll(unsubmittedHoursDTOList);
                    }

            );
        }
        if(teamId != null && teamId.intValue() != 0) {
            hoursReportsResponseDTO.setUnsubmittedHoursDTOList(
                    hoursReportsResponseDTO.getUnsubmittedHoursDTOList().stream()
                    .filter(timesheetReportsDTO -> timesheetReportsDTO.getTeamId().intValue() == teamId.intValue())
                    .collect(Collectors.toList())
            );
        }
        UnsubmittedHoursDTOComparator comparator = new UnsubmittedHoursDTOComparator();
        Collections.sort(hoursReportsResponseDTO.getUnsubmittedHoursDTOList(), comparator);
        return hoursReportsResponseDTO;
    }
/*
    @Override
    public HoursReportsResponseDTO getAccountHours(String startDate, String endDate, Integer accountId, Integer categoryaccountId) {
        log.info("getAccountHours: periods = accpountid = " +accountId);
        LocalDate start = ConversionUtils.convertISODateStringToLocalDate(startDate);
        LocalDate end = ConversionUtils.convertISODateStringToLocalDate(endDate);
        periodValidator.validateDateInterval(start, end);
        HoursReportsResponseDTO hoursReportsResponseDTO = new HoursReportsResponseDTO();
        hoursReportsResponseDTO.setAccountHoursDTOList(Collections.synchronizedList(new ArrayList<>()));
        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        if (user == null || !user.isActive()) {
            throw new NoLoggedUserException();
        }
        if(accountId != null && accountId.intValue() != 0) {
            hoursReportsResponseDTO.setAccountHoursDTOList(
                    hoursReportsResponseDTO.getAccountHoursDTOList().stream()
                    .filter(timesheetReportsDTO -> timesheetReportsDTO.getAccountId().intValue() == accountId.intValue())
                    .collect(Collectors.toList())
            );
        }
        if(categoryaccountId != null && categoryaccountId.intValue() != 0) {
            hoursReportsResponseDTO.setAccountHoursDTOList(
                    hoursReportsResponseDTO.getAccountHoursDTOList().stream()
                    .filter(timesheetReportsDTO -> timesheetReportsDTO.getCategoryaccountId().intValue() == categoryaccountId.intValue())
                    .collect(Collectors.toList())
            );
        }
        String key = user.getKey();
        return hoursReportsResponseDTO;
    }
    */
    @Override
    public HoursReportsResponseDTO getAccountHours(String startDate, String endDate, Integer accountId, Integer categoryaccountId) {
        log.info("getAccountHours: startDate = " + startDate + ", endDate = " + endDate + ", teamId = " + accountId + ", categoryaccountId = " + categoryaccountId);
        LocalDate start = ConversionUtils.convertISODateStringToLocalDate(startDate);
        LocalDate end = ConversionUtils.convertISODateStringToLocalDate(endDate);
        periodValidator.validateDateInterval(start, end);
        HoursReportsResponseDTO hoursReportsResponseDTO = new HoursReportsResponseDTO();
        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        if (user == null || !user.isActive()) {
            throw new NoLoggedUserException();
        }
        String key = user.getKey();

        List<AccountTimesheetReportsDTO> accountTimesheetReportsDTOS = reportsDAO.getAccountHours(
            ConversionUtils.convertLocalDateToISODateString(start),
            ConversionUtils.convertLocalDateToISODateString(end));
        log.error("accountTimesheetReportsDTOS: " +accountTimesheetReportsDTOS);
        if(accountId != null && accountId.intValue() != 0) {
            log.error("Entrou no if de account");
            accountTimesheetReportsDTOS = accountTimesheetReportsDTOS.stream()
                    .filter(timesheetReportsDTO -> timesheetReportsDTO.getAccountId().intValue() == accountId.intValue())
                    .collect(Collectors.toList());
        }
        if(categoryaccountId != null && categoryaccountId.intValue() != 0) {
            log.error("Entrou no if de categoryaccount");
            accountTimesheetReportsDTOS = accountTimesheetReportsDTOS.stream()
                    .filter(timesheetReportsDTO -> timesheetReportsDTO.getCategoryaccountId().intValue() == categoryaccountId.intValue())
                    .collect(Collectors.toList());
        }

        hoursReportsResponseDTO.setAccountHoursDTOList(accountTimesheetReportsDTOS);
        log.error(accountTimesheetReportsDTOS);
        return hoursReportsResponseDTO;
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
