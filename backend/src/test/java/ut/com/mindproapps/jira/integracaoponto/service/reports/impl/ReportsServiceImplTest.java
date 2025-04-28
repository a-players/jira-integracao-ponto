package ut.com.mindproapps.jira.integracaoponto.service.reports.impl;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.mindproapps.jira.integracaoponto.dao.config.ConfigDAO;
import com.mindproapps.jira.integracaoponto.dao.ponto.PontoDAO;
import com.mindproapps.jira.integracaoponto.dao.reports.ReportsDAO;
import com.mindproapps.jira.integracaoponto.dao.user.depara.DeParaUserDAO;
import com.mindproapps.jira.integracaoponto.model.dto.period.TimesheetsPeriodDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.HoursReportsResponseDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.PartialTimesheetReportsDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.TimesheetReportsDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.TimesheetReportsResponseDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.UnsubmittedHoursDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.AccountTimesheetReportsDTO;
import com.mindproapps.jira.integracaoponto.rest.client.PontoRestClient;
import com.mindproapps.jira.integracaoponto.service.ponto.PontoService;
import com.mindproapps.jira.integracaoponto.service.ponto.impl.PontoServiceImpl;
import com.mindproapps.jira.integracaoponto.service.reports.impl.ReportsServiceImpl;
import com.mindproapps.jira.integracaoponto.util.ConversionUtils;
import com.mindproapps.jira.integracaoponto.validator.PeriodValidator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class ReportsServiceImplTest {

    private static ReportsDAO reportsDAO;
    private static PeriodValidator periodValidator;
    private static PontoService pontoService;
    private static final ApplicationUser APPLICATION_USER = Mockito.mock(ApplicationUser.class);
    private static final JiraAuthenticationContext JIRA_AUTHENTICATION_CONTEXT = Mockito.mock(JiraAuthenticationContext.class);
    private static final MockedStatic<ComponentAccessor> componentAccessorMockedStatic = mockStatic(ComponentAccessor.class);

    private static ReportsServiceImpl service;
    
    private static DeParaUserDAO deParaUserDAO;
    private static ConfigDAO configDAO;
    private static PontoDAO wsPontoDAO;
    private static PontoService wsPontoService;
    private static ReportsServiceImpl wsService;

    @BeforeClass
    public static void setUp() {
        Mockito.when(APPLICATION_USER.getKey()).thenReturn("KEY");
        Mockito.when(APPLICATION_USER.isActive()).thenReturn(Boolean.TRUE);
        Mockito.when(JIRA_AUTHENTICATION_CONTEXT.getLoggedInUser()).thenReturn(APPLICATION_USER);
        componentAccessorMockedStatic.when(ComponentAccessor::getJiraAuthenticationContext).thenReturn(JIRA_AUTHENTICATION_CONTEXT);

        reportsDAO = Mockito.mock(ReportsDAO.class);
        periodValidator = new PeriodValidator();
        pontoService = Mockito.mock(PontoService.class);

        service = new ReportsServiceImpl(reportsDAO, periodValidator, pontoService);

        deParaUserDAO = Mockito.mock(DeParaUserDAO.class);
        configDAO = Mockito.mock(ConfigDAO.class);
        Mockito.when(configDAO.getUrlPonto()).thenReturn("https://tisistemas-hml.linx.com.br/HorasTrabalhadasPontoNorberAPI/api/horastrabalhadas");
        Mockito.when(configDAO.getUrlLogin()).thenReturn("https://tisistemas-hml.linx.com.br/HorasTrabalhadasPontoNorberAPI/Login");
        Mockito.when(configDAO.getApiKey()).thenReturn("{ \"api_key\": \"F9F66E7A-10EE-4E23-85DD-33E796FFB730\","+
                " \"api_secret\": \"413A122D-162B-455B-8B05-E3A41BC06FC4\"}");

        PontoRestClient pontoRestClient = new PontoRestClient(configDAO);
        wsPontoDAO = new PontoDAO(pontoRestClient);
        wsPontoService = new PontoServiceImpl(wsPontoDAO, deParaUserDAO);
        wsService = new ReportsServiceImpl(reportsDAO, periodValidator, wsPontoService);
    }

    @AfterClass
    public static void tearDown() {
        componentAccessorMockedStatic.close();
    }

    @Test
    public void getTimesheetsApproved() {
        //ARRANGE
        String startDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String endDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        List<TimesheetReportsDTO> list = new ArrayList<TimesheetReportsDTO>();
        TimesheetReportsDTO tdto;
        tdto = TimesheetReportsDTO.builder()
                .id(1)
                .teamId(1)
                .build();
                tdto.setEmailPonto("some@email.com");
        list.add(tdto);
        tdto = TimesheetReportsDTO.builder()
                .id(2)
                .teamId(1)
                .build();
                tdto.setEmailPonto("other@email.com");
        list.add(tdto);
        when(reportsDAO.getTimesheetsApproved(startDate, endDate)).thenReturn(list);

        //ACT
        TimesheetReportsResponseDTO sut = service.getTimesheetsApproved(startDate, endDate, null);
        //ASSERT
        assertNotNull(sut);
        assertNotNull(sut.getApprovedTimesheetsList());
        assertNull(sut.getSubmittedTimesheetsList());
        assertEquals(2, sut.getApprovedTimesheetsList().size());
        assertEquals(1, sut.getApprovedTimesheetsList().stream().filter(dto -> dto.getId() == 1).count());
        assertEquals(1, sut.getApprovedTimesheetsList().stream().filter(dto -> dto.getId() == 2).count());
        assertEquals(0, sut.getApprovedTimesheetsList().stream().filter(dto -> dto.getId() == 3).count());
    }

    @Test
    public void getTimesheetsSubmitted() {
        //ARRANGE
        String startDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String endDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        List<TimesheetReportsDTO> list = new ArrayList<TimesheetReportsDTO>();
        TimesheetReportsDTO tdto;
        tdto = TimesheetReportsDTO.builder()
                .id(1)
                .teamId(1)
                .build();
                tdto.setEmailPonto("some@email.com");
        list.add(tdto);
        tdto = TimesheetReportsDTO.builder()
                .id(2)
                .teamId(1)
                .build();
                tdto.setEmailPonto("other@email.com");
        list.add(tdto);
        when(reportsDAO.getTimesheetsSubmitted(startDate, endDate)).thenReturn(list);

        //ACT
        TimesheetReportsResponseDTO sut = service.getTimesheetsSubmitted(startDate, endDate, null);
        //ASSERT
        assertNotNull(sut);
        assertNotNull(sut.getSubmittedTimesheetsList());
        assertNull(sut.getApprovedTimesheetsList());
        assertEquals(2, sut.getSubmittedTimesheetsList().size());
        assertEquals(1, sut.getSubmittedTimesheetsList().stream().filter(dto -> dto.getId() == 1).count());
        assertEquals(1, sut.getSubmittedTimesheetsList().stream().filter(dto -> dto.getId() == 2).count());
        assertEquals(0, sut.getSubmittedTimesheetsList().stream().filter(dto -> dto.getId() == 3).count());

    }

    @Test
    public void getUndersubmittedHours() {
        //ARRANGE
        String startDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String endDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        List<PartialTimesheetReportsDTO> list = new ArrayList<PartialTimesheetReportsDTO>();
        PartialTimesheetReportsDTO udto;
        udto = PartialTimesheetReportsDTO.builder()
                .id(1)
                .teamId(1)
                .build();
                udto.setEmailPonto("some@email.com");
        list.add(udto);
        udto = PartialTimesheetReportsDTO.builder()
                .id(2)
                .teamId(1)
                .build();
                udto.setEmailPonto("other@email.com");
        list.add(udto);

        when(reportsDAO.getTimesheetsSubmittedPartialHours(startDate, endDate)).thenReturn(list);
        //ACT
        HoursReportsResponseDTO sut = service.getUndersubmittedHours(startDate, endDate, null);
        //ASSERT
        assertNotNull(sut);
        assertNotNull(sut.getPartialTimesheetReportsDTOList());
        assertNull(sut.getUnsubmittedHoursDTOList());
        assertEquals(2, sut.getPartialTimesheetReportsDTOList().size());
        assertEquals(1, sut.getPartialTimesheetReportsDTOList().stream().filter(dto -> dto.getId() == 1).count());
        assertEquals(1, sut.getPartialTimesheetReportsDTOList().stream().filter(dto -> dto.getId() == 2).count());
        assertEquals(0, sut.getPartialTimesheetReportsDTOList().stream().filter(dto -> dto.getId() == 3).count());

    }

    @Test
    public void getUsubmittedHoursIntegracaoPonto() {
        //ARRANGE
        String startDate = "2021-07-01";
        String endDate = "2021-07-15";
        List<TimesheetsPeriodDTO> periods = new ArrayList<TimesheetsPeriodDTO>();
        periods.add(TimesheetsPeriodDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build());
        List<UnsubmittedHoursDTO> list = new ArrayList<UnsubmittedHoursDTO>();
        UnsubmittedHoursDTO udto;
        udto = UnsubmittedHoursDTO.builder()
                .teamId(1)
                .build();
        udto.setPeriodStartDate(ConversionUtils.formatISODateAsRegularDate(startDate));
        udto.setPeriodEndDate(ConversionUtils.formatISODateAsRegularDate(endDate));
        udto.setPeriodStartDateISO(startDate);
        udto.setPeriodEndDateISO(endDate);
        udto.setEmailPonto("ellen.gouveia@linx.com.br");
        list.add(udto);
        udto = UnsubmittedHoursDTO.builder()
                .teamId(1)        
                .build();
        udto.setPeriodStartDate(ConversionUtils.formatISODateAsRegularDate(startDate));
        udto.setPeriodEndDate(ConversionUtils.formatISODateAsRegularDate(endDate));
        udto.setPeriodStartDateISO(startDate);
        udto.setPeriodEndDateISO(endDate);
        udto.setEmailPonto("janilson.dias@linx.com.br");
        list.add(udto);
        when(reportsDAO.getUnsubmittedHours(periods.get(0).getStartDate(), periods.get(0).getEndDate())).thenReturn(list);
        //ACT
        HoursReportsResponseDTO sut = wsService.getUnsubmittedHours(periods, 1);
        //ASSERT
        assertNotNull(sut);
        assertNull(sut.getPartialTimesheetReportsDTOList());
        assertNotNull(sut.getUnsubmittedHoursDTOList());
        assertEquals(2, sut.getUnsubmittedHoursDTOList().size());
        assertEquals(2, sut.getUnsubmittedHoursDTOList().stream().filter(dto -> Double.parseDouble(dto.getHorasPonto())  > 0).count());

    }

    @Test
    public void getAccountHours() {
        //ARRANGE
        String startDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String endDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        List<AccountTimesheetReportsDTO> list = new ArrayList<AccountTimesheetReportsDTO>();
        AccountTimesheetReportsDTO udto;
        udto = AccountTimesheetReportsDTO.builder()
                .id(1)
                .accountId(1)
                .build();
                udto.setEmailPonto("some@email.com");
        list.add(udto);
        udto = AccountTimesheetReportsDTO.builder()
                .id(2)
                .accountId(1)
                .build();
                udto.setEmailPonto("other@email.com");
        list.add(udto);

        when(reportsDAO.getAccountHours(startDate, endDate)).thenReturn(list);
        //ACT
        HoursReportsResponseDTO sut = service.getAccountHours(startDate, endDate, null, null);
        //ASSERT
        assertNotNull(sut);
        assertNotNull(sut.getAccountHoursDTOList());
        assertNull(sut.getUnsubmittedHoursDTOList());
        assertEquals(2, sut.getAccountHoursDTOList().size());
        assertEquals(1, sut.getAccountHoursDTOList().stream().filter(dto -> dto.getId() == 1).count());
        assertEquals(1, sut.getAccountHoursDTOList().stream().filter(dto -> dto.getId() == 2).count());
        assertEquals(0, sut.getAccountHoursDTOList().stream().filter(dto -> dto.getId() == 3).count());

    }

}