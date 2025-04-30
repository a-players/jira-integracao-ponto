package ut.com.mindproapps.jira.integracaoponto.service.reports.impl;

import com.mindproapps.jira.integracaoponto.dao.reports.ReportsDAO;
import com.mindproapps.jira.integracaoponto.service.ponto.PontoService;
import com.mindproapps.jira.integracaoponto.service.reports.impl.ReportsServiceImpl;
import com.mindproapps.jira.integracaoponto.validator.PeriodValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReportsServiceImplTest {

    @Mock
    private ReportsDAO reportsDAO;

    @Mock
    private PeriodValidator periodValidator;

    @Mock
    private PontoService pontoService;

    @InjectMocks
    private ReportsServiceImpl reportsServiceImpl;

    @Before
    public void setUp() {
        // mocks já injetados automaticamente
    }

    @Test
    public void dummyTest() {
        assert true;
    }
}
