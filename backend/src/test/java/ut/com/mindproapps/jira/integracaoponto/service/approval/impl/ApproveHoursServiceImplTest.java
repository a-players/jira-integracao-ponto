package ut.com.mindproapps.jira.integracaoponto.service.approval.impl;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;

import com.mindproapps.jira.integracaoponto.dao.config.ConfigDAO;
import com.mindproapps.jira.integracaoponto.dao.ponto.PontoDAO;
import com.mindproapps.jira.integracaoponto.dao.tempo.timesheet.TempoTimesheetDAO;
import com.mindproapps.jira.integracaoponto.dao.timesheet.TimesheetApprovalsOriginTraceDAO;
import com.mindproapps.jira.integracaoponto.dao.user.depara.DeParaUserDAO;
import com.mindproapps.jira.integracaoponto.model.dto.approval.TimesheetsWaitingForApprovalDTO;
import com.mindproapps.jira.integracaoponto.model.dto.approval.TimesheetsWaitingForApprovalResponseDTO;
import com.mindproapps.jira.integracaoponto.model.dto.ponto.PontoDTO;
import com.mindproapps.jira.integracaoponto.rest.client.PontoRestClient;
import com.mindproapps.jira.integracaoponto.service.approval.impl.ApproveHoursServiceImpl;
import com.mindproapps.jira.integracaoponto.service.i18n.I18nService;
import com.mindproapps.jira.integracaoponto.service.mail.MailService;
import com.mindproapps.jira.integracaoponto.service.ponto.PontoService;
import com.mindproapps.jira.integracaoponto.service.ponto.impl.PontoServiceImpl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

public class ApproveHoursServiceImplTest {
    private static GlobalPermissionManager globalPermissionManager;
    private static TempoTimesheetDAO tempoTimesheetDAO;
    private static TimesheetApprovalsOriginTraceDAO timesheetApprovalsOriginTraceDAO;
    private static PontoDAO pontoDAO;
    private static PontoService pontoService;
    private static DeParaUserDAO deParaUserDAO;
    private static I18nService i18nService;
    private static MailService mailService;
    private static final ApplicationUser APPLICATION_USER = Mockito.mock(ApplicationUser.class);
    private static final JiraAuthenticationContext JIRA_AUTHENTICATION_CONTEXT = Mockito.mock(JiraAuthenticationContext.class);
    private static final MockedStatic<ComponentAccessor> componentAccessorMockedStatic = mockStatic(ComponentAccessor.class);

    private static ApproveHoursServiceImpl service;

    private static ConfigDAO configDAO;
    private static PontoDAO wsPontoDAO;
    private static PontoService wsPontoService;
    private static ApproveHoursServiceImpl wsService;

    @BeforeClass
    public static void setUp() throws Exception {
        Mockito.when(APPLICATION_USER.getKey()).thenReturn("KEY");
        Mockito.when(APPLICATION_USER.isActive()).thenReturn(Boolean.TRUE);
        Mockito.when(JIRA_AUTHENTICATION_CONTEXT.getLoggedInUser()).thenReturn(APPLICATION_USER);
        componentAccessorMockedStatic.when(ComponentAccessor::getJiraAuthenticationContext).thenReturn(JIRA_AUTHENTICATION_CONTEXT);

        globalPermissionManager = Mockito.mock(GlobalPermissionManager.class);
        tempoTimesheetDAO = Mockito.mock(TempoTimesheetDAO.class);
        timesheetApprovalsOriginTraceDAO = Mockito.mock(TimesheetApprovalsOriginTraceDAO.class);
        pontoService = Mockito.mock(PontoService.class);
        pontoDAO = Mockito.mock(PontoDAO.class);
        
        deParaUserDAO = Mockito.mock(DeParaUserDAO.class);
        i18nService = Mockito.mock(I18nService.class);
        Mockito.when(i18nService.getTexts(anyString())).thenReturn(new HashMap<>());
        mailService = Mockito.mock(MailService.class);
        service = new ApproveHoursServiceImpl(globalPermissionManager,
                tempoTimesheetDAO, timesheetApprovalsOriginTraceDAO, pontoService, deParaUserDAO, i18nService, mailService);

        configDAO = Mockito.mock(ConfigDAO.class);
        Mockito.when(configDAO.getUrlPonto()).thenReturn("https://tisistemas-hml.linx.com.br/HorasTrabalhadasPontoNorberAPI/api/horastrabalhadas");
        Mockito.when(configDAO.getUrlLogin()).thenReturn("https://tisistemas-hml.linx.com.br/HorasTrabalhadasPontoNorberAPI/Login");
        Mockito.when(configDAO.getApiKey()).thenReturn("{ \"api_key\": \"096E5035-A9C1-4D94-A8F4-46DEF0BB3E0F\","+
                " \"api_secret\": \"033663C2-21FF-46A3-B7C1-F8C09688632E\"}");
        PontoRestClient pontoRestClient = new PontoRestClient(configDAO);
        wsPontoDAO = new PontoDAO(pontoRestClient);
        wsPontoService = new PontoServiceImpl(wsPontoDAO, deParaUserDAO);
        wsService = new ApproveHoursServiceImpl(globalPermissionManager,
                tempoTimesheetDAO, timesheetApprovalsOriginTraceDAO, wsPontoService, deParaUserDAO, i18nService, mailService);
    }

    @AfterClass
    public static void tearDown() {
        componentAccessorMockedStatic.close();
    }

    @Test
    public void testGetTimesheetsWaitingForApprovalListExistenteEmailInformado() {
        //ARRANGE
        List<TimesheetsWaitingForApprovalDTO> list = new ArrayList<>();
        TimesheetsWaitingForApprovalDTO dto = TimesheetsWaitingForApprovalDTO.builder()
                .status("review")
                .build();
        dto.setJiraEmail("some@email.com");
        dto.setEmailPonto("some@email.com");
        dto.setEmailInformado("another@email.com");
        list.add(dto);
        Mockito.when(tempoTimesheetDAO.getTimesheetsWaitingForApprovalList("2020-10-01", "2020-10-07","1020", "KEY")).thenReturn(list);
        List<PontoDTO> listPonto = new ArrayList<>();
        listPonto.add(
                PontoDTO.builder().qtdeHorasTrabalhadas("03:12:00").build()
        );
        listPonto.add(
                PontoDTO.builder().qtdeHorasTrabalhadas("08:00:00").build()
        );
        Mockito.when(
                pontoService.getPontoByEmailAndPeriod(listPonto, list.get(0).getEmailInformado(), LocalDate.parse("2020-10-01"), LocalDate.parse("2020-10-07")))
        .thenReturn(listPonto);
        
        List<String> lstEmails = new ArrayList<String>();
        list.forEach(ddto -> {
                ddto.updateEmailPonto();
                if (!lstEmails.contains(ddto.getEmailPonto())) {
                        lstEmails.add(ddto.getEmailPonto());
                }
        });
        List<PontoDTO> allData = pontoService.getAllData(lstEmails, LocalDate.parse("2020-10-01"), LocalDate.parse("2020-10-07"));
        
        //ACT
        TimesheetsWaitingForApprovalResponseDTO sut = service.getTimesheetsWaitingForApprovalList("2020-10-01", "2020-10-07");
        //ASSERT
        assertNotNull(sut);
        assertNotNull(sut.getSubmittedTimesheets());
        assertEquals(1, sut.getSubmittedTimesheets().size());
    }

    @Test
    public void testGetTimesheetsWaitingForApprovalListExistenteEmailPonto() {
        //ARRANGE
        String pontoDate;
        List<TimesheetsWaitingForApprovalDTO> list = new ArrayList<>();
        TimesheetsWaitingForApprovalDTO dto = TimesheetsWaitingForApprovalDTO.builder()
                .status("review")
                .build();
        dto.setJiraEmail("some@email.com");
        dto.setEmailPonto("some@email.com");
        dto.setEmailInformado("another@email.com");
        list.add(dto);
        Mockito.when(tempoTimesheetDAO.getTimesheetsWaitingForApprovalList("2020-10-01", "2020-10-07","1020", "KEY")).thenReturn(list);
        List<PontoDTO> listPonto = new ArrayList<>();
        pontoDate = LocalDateTime.now().minusDays(2).format(DateTimeFormatter.ISO_DATE_TIME);
        listPonto.add(
                PontoDTO.builder().qtdeHorasTrabalhadas("03:12:00").dataInformacao(pontoDate).email("some@email.com").build()
        );
        pontoDate = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ISO_DATE_TIME);
        listPonto.add(
                PontoDTO.builder().qtdeHorasTrabalhadas("08:00:00").dataInformacao(pontoDate).email("another@email.com").build()
        );
        Mockito.when(
                pontoService.getPontoByEmailAndPeriod(listPonto, list.get(0).getEmailPonto(), LocalDate.parse("2020-10-01"), LocalDate.parse("2020-10-07")))
                .thenReturn(listPonto);
        //ACT
        TimesheetsWaitingForApprovalResponseDTO sut = service.getTimesheetsWaitingForApprovalList("2020-10-01", "2020-10-07");
        //ASSERT
        assertNotNull(sut);
        assertNotNull(sut.getSubmittedTimesheets());
        assertEquals(1, sut.getSubmittedTimesheets().size());
        TimesheetsWaitingForApprovalDTO sutDto = sut.getSubmittedTimesheets().get(0);
        assertEquals(list.get(0).getEmailPonto(), sutDto.getEmailPonto());
    }

    @Test
    public void testGetTimesheetsWaitingForApprovalListInexistenteExisteEmPonto() {
        //ARRANGE
        String pontoDate;
        List<TimesheetsWaitingForApprovalDTO> list = new ArrayList<>();
        TimesheetsWaitingForApprovalDTO dto = TimesheetsWaitingForApprovalDTO.builder()
                .status("review")
                .build();
        dto.setJiraEmail("some@email.com");
        dto.setEmailPonto("some@email.com");
        list.add(dto);
        Mockito.when(tempoTimesheetDAO.getTimesheetsWaitingForApprovalList("2020-10-01", "2020-10-07","1020", "KEY")).thenReturn(list);
        List<PontoDTO> listPonto = new ArrayList<>();
        pontoDate = LocalDateTime.now().minusDays(2).format(DateTimeFormatter.ISO_DATE_TIME);
        listPonto.add(
                PontoDTO.builder().qtdeHorasTrabalhadas("03:12:00").dataInformacao(pontoDate).email("some@email.com").build()
        );
        pontoDate = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ISO_DATE_TIME);
        listPonto.add(
                PontoDTO.builder().qtdeHorasTrabalhadas("08:00:00").dataInformacao(pontoDate).email("another@email.com").build()
        );
        Mockito.when(pontoService.emailExists(listPonto, list.get(0).getEmail())).thenReturn(list.get(0).getWorkerKey());
        Mockito.when(
                pontoService.getPontoByEmailAndPeriod(listPonto, list.get(0).getEmail(), LocalDate.parse("2020-10-01"), LocalDate.parse("2020-10-07")))
                .thenReturn(listPonto);
        //ACT
        TimesheetsWaitingForApprovalResponseDTO sut = service.getTimesheetsWaitingForApprovalList("2020-10-01", "2020-10-07");
        //ASSERT
        assertNotNull(sut);
        assertNotNull(sut.getSubmittedTimesheets());
        assertEquals(1, sut.getSubmittedTimesheets().size());
    }

    @Test
    public void testGetTimesheetsWaitingForApprovalListInexistenteNaoExisteEmPonto() {
        //ARRANGE
        String pontoDate;
        List<TimesheetsWaitingForApprovalDTO> list = new ArrayList<>();
        TimesheetsWaitingForApprovalDTO dto = TimesheetsWaitingForApprovalDTO.builder()
                .status("review")
                .build();
        dto.setJiraEmail("some@email.com");
        list.add(dto);
        List<PontoDTO> listPonto = new ArrayList<>();
        pontoDate = LocalDateTime.now().minusDays(2).format(DateTimeFormatter.ISO_DATE_TIME);
        listPonto.add(
                PontoDTO.builder().qtdeHorasTrabalhadas("03:12:00").dataInformacao(pontoDate).email("some@email.com").build()
        );
        pontoDate = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ISO_DATE_TIME);
        listPonto.add(
                PontoDTO.builder().qtdeHorasTrabalhadas("08:00:00").dataInformacao(pontoDate).email("another@email.com").build()
        );
        Mockito.when(tempoTimesheetDAO.getTimesheetsWaitingForApprovalList("2020-10-01", "2020-10-07","1020", "KEY")).thenReturn(list);
        Mockito.when(pontoService.emailExists(listPonto, list.get(0).getEmail())).thenReturn(null);

        //ACT
        TimesheetsWaitingForApprovalResponseDTO sut = service.getTimesheetsWaitingForApprovalList("2020-10-01", "2020-10-07");
        //ASSERT
        assertNotNull(sut);
        assertNotNull(sut.getSubmittedTimesheets());
        assertEquals(1, sut.getSubmittedTimesheets().size());
        TimesheetsWaitingForApprovalDTO sutDto = sut.getSubmittedTimesheets().get(0);
        assertNull(sutDto.getEmailPonto());
    }

    @Test
    public void testGetTimesheetsWaitingForApprovalListIntegracaoPonto() {
        //ARRANGE
        List<TimesheetsWaitingForApprovalDTO> list = new ArrayList<>();
        TimesheetsWaitingForApprovalDTO dto;
        dto = TimesheetsWaitingForApprovalDTO.builder()
                .status("review")
                .build();
        dto.setEmailPonto("some@email.com");
        list.add(dto);
        dto = TimesheetsWaitingForApprovalDTO.builder()
                .status("review")
                .build();
        dto.setEmailPonto("ellen.gouveia@linx.com.br");
        list.add(dto);
        dto = TimesheetsWaitingForApprovalDTO.builder()
        .status("review")
        .build();
        dto.setEmailPonto("janilson.dias@linx.com.br");
        list.add(dto);
        Mockito.when(tempoTimesheetDAO.getTimesheetsWaitingForApprovalList("2021-06-01", "2021-06-16","0621", "KEY")).thenReturn(list);
        //ACT
        TimesheetsWaitingForApprovalResponseDTO sut = wsService.getTimesheetsWaitingForApprovalList("2021-06-01", "2021-06-16");
        //ASSERT
        assertNotNull(sut);
        assertNotNull(sut.getSubmittedTimesheets());
        assertEquals(3, sut.getSubmittedTimesheets().size());
    }

}