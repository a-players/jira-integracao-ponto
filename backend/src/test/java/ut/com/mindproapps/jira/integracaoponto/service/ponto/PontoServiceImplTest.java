package ut.com.mindproapps.jira.integracaoponto.service.ponto.impl;

import com.mindproapps.jira.integracaoponto.dao.ponto.PontoDAO;
import com.mindproapps.jira.integracaoponto.dao.user.depara.DeParaUserDAO;
import com.mindproapps.jira.integracaoponto.service.ponto.impl.PontoServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PontoServiceImplTest {

    @Mock
    private PontoDAO pontoDAO;

    @Mock
    private DeParaUserDAO deParaUserDAO;

    @InjectMocks
    private PontoServiceImpl pontoServiceImpl;

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
