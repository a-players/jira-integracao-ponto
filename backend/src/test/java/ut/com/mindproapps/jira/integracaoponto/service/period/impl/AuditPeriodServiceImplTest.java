package ut.com.mindproapps.jira.integracaoponto.service.period.impl;

import com.mindproapps.jira.integracaoponto.dao.period.AuditPeriodDAO;
import com.mindproapps.jira.integracaoponto.service.i18n.I18nService;
import com.mindproapps.jira.integracaoponto.service.period.impl.AuditPeriodServiceImpl;
import com.mindproapps.jira.integracaoponto.validator.PeriodValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuditPeriodServiceImplTest {

    @Mock
    private AuditPeriodDAO auditPeriodDAO;

    @Mock
    private PeriodValidator periodValidator;

    @Mock
    private I18nService i18nService;

    @InjectMocks
    private AuditPeriodServiceImpl auditPeriodServiceImpl;

    @Before
    public void setUp() {
        // mocks já injetados automaticamente
    }

    @Test
    public void dummyTest() {
        assert true;
    }
}
