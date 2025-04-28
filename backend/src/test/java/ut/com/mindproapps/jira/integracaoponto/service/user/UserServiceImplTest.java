package ut.com.mindproapps.jira.integracaoponto.service.user;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.mindproapps.jira.integracaoponto.dao.user.depara.DeParaUserDAO;
import com.mindproapps.jira.integracaoponto.dao.user.jira.JiraUserDAO;
import com.mindproapps.jira.integracaoponto.model.dto.user.DeParaUserDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.DeParaUserListDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.DeParaUserListRequestDTO;
import com.mindproapps.jira.integracaoponto.service.ponto.PontoService;
import com.mindproapps.jira.integracaoponto.service.user.impl.UserServiceImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mockStatic;

public class UserServiceImplTest {
    private static JiraUserDAO jiraUserDAO;
    private static DeParaUserDAO deParaUserDAO;
    private static PontoService pontoService;
    private static final ApplicationUser APPLICATION_USER = Mockito.mock(ApplicationUser.class);
    private static final JiraAuthenticationContext JIRA_AUTHENTICATION_CONTEXT = Mockito.mock(JiraAuthenticationContext.class);
    private static final MockedStatic<ComponentAccessor> componentAccessorMockedStatic = mockStatic(ComponentAccessor.class);

    private static UserServiceImpl service;



    @BeforeClass
    public static void setUp() throws Exception {
        Mockito.when(APPLICATION_USER.getKey()).thenReturn("KEY");
        Mockito.when(APPLICATION_USER.isActive()).thenReturn(Boolean.TRUE);
        Mockito.when(JIRA_AUTHENTICATION_CONTEXT.getLoggedInUser()).thenReturn(APPLICATION_USER);
        componentAccessorMockedStatic.when(ComponentAccessor::getJiraAuthenticationContext).thenReturn(JIRA_AUTHENTICATION_CONTEXT);

        jiraUserDAO = Mockito.mock(JiraUserDAO.class);
        deParaUserDAO = Mockito.mock(DeParaUserDAO.class);
        pontoService = Mockito.mock(PontoService.class);
        service = new UserServiceImpl(jiraUserDAO, deParaUserDAO, pontoService);
    }

    @AfterClass
    public static void tearDown() {
        componentAccessorMockedStatic.close();
    }

    @Test
    public void testGetUsersListComDados() {
        //ARRANGE
        DeParaUserListRequestDTO requestDTO = new DeParaUserListRequestDTO();
        requestDTO.setPrevious(3);
        requestDTO.setStartRecord(31);
        requestDTO.setNumberOfRecords(30);
        requestDTO.setDeParaType(3);
        Mockito.when(jiraUserDAO.listJiraUsers(null, null, requestDTO.getDeParaType(), null)).thenReturn(new ArrayList<>());
        Mockito.when(jiraUserDAO.getTotalJiraUsers(requestDTO.getDeParaType(), null)).thenReturn(3000);
        Mockito.when(jiraUserDAO.listJiraUsers(
                requestDTO.getStartRecord(), requestDTO.getNumberOfRecords(), requestDTO.getDeParaType(), null))
        .thenReturn(generateDeParaUserDtoList(31, 30));

        //ACT
        DeParaUserListDTO sut = service.getUsersList(requestDTO);
        //ASSERT
        assertNotNull(sut);
        assertNotNull(sut.getList());
        assertEquals(30, sut.getList().size());
        assertEquals(3000, sut.getTotalCount().intValue());
        assertNotNull(sut.getPagesMap());
        assertEquals(12, sut.getPagesMap().size());
        assertEquals(31, sut.getList().get(0).getRn().intValue());
        assertEquals(60, sut.getList().get(sut.getList().size() - 1).getRn().intValue());
    }

    @Test
    public void testGetUsersListDiferenteFiltro() {
        //ARRANGE
        DeParaUserListRequestDTO requestDTO = new DeParaUserListRequestDTO();
        requestDTO.setPrevious(1);
        requestDTO.setDeParaType(3);
        requestDTO.setStartRecord(1);
        requestDTO.setNumberOfRecords(100);
        Mockito.when(jiraUserDAO.listJiraUsers(null, null, requestDTO.getDeParaType(), null)).thenReturn(new ArrayList<>());
        Mockito.when(jiraUserDAO.getTotalJiraUsers(requestDTO.getDeParaType(), null)).thenReturn(3000);
        Mockito.when(jiraUserDAO.listJiraUsers(
                1, 100, requestDTO.getDeParaType(), null))
                .thenReturn(generateDeParaUserDtoList(1, 100));

        //ACT
        DeParaUserListDTO sut = service.getUsersList(requestDTO);
        //ASSERT
        assertNotNull(sut);
        assertNotNull(sut.getList());
        assertEquals(100, sut.getList().size());
        assertEquals(3000, sut.getTotalCount().intValue());
        assertNotNull(sut.getPagesMap());
        assertEquals(12, sut.getPagesMap().size());
        assertEquals(1, sut.getList().get(0).getRn().intValue());
        assertEquals(100, sut.getList().get(sut.getList().size() - 1).getRn().intValue());
    }

    private List<DeParaUserDTO> generateDeParaUserDtoList(Integer start, Integer numberOfRecords) {
        List<DeParaUserDTO> list = new ArrayList<>();
        for(Integer i = 1; i <= numberOfRecords; i++) {
            Integer rn = start - 1 + i;
            list.add(
                    DeParaUserDTO.builder()
                            .rn(rn)
                            .username("user" + rn)
                            .userKey("KEY" + rn)
                            .email("some" + rn + "@email.com")
                            .build()
            );
        }
        return list;
    }
}