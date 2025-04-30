package ut.com.mindproapps.jira.integracaoponto.service.approval.impl;

import com.atlassian.jira.security.GlobalPermissionManager;
import com.mindproapps.jira.integracaoponto.dao.tempo.timesheet.TempoTimesheetDAO;
import com.mindproapps.jira.integracaoponto.dao.timesheet.TimesheetApprovalsOriginTraceDAO;
import com.mindproapps.jira.integracaoponto.dao.user.depara.DeParaUserDAO;
import com.mindproapps.jira.integracaoponto.service.approval.impl.ApproveHoursServiceImpl;
import com.mindproapps.jira.integracaoponto.service.i18n.I18nService;
import com.mindproapps.jira.integracaoponto.service.mail.MailService;
import com.mindproapps.jira.integracaoponto.service.ponto.PontoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApproveHoursServiceImplTest {

    @Mock
    private GlobalPermissionManager globalPermissionManager;

    @Mock
    private TempoTimesheetDAO tempoTimesheetDAO;

    @Mock
    private TimesheetApprovalsOriginTraceDAO timesheetApprovalsOriginTraceDAO;

    @Mock
    private PontoService pontoService;

    @Mock
    private DeParaUserDAO deParaUserDAO;

    @Mock
    private I18nService i18nService;

    @Mock
    private MailService mailService;

    @InjectMocks
    private ApproveHoursServiceImpl approveHoursServiceImpl;

    @Before
    public void setUp() {
        // mocks já injetados automaticamente
    }

    @Test
    public void dummyTest() {
        // Aqui você coloca seus testes reais
        assert true;
    }
}
