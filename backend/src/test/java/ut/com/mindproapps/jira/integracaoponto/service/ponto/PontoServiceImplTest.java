package ut.com.mindproapps.jira.integracaoponto.service.ponto;

import com.mindproapps.jira.integracaoponto.dao.ponto.PontoDAO;
import com.mindproapps.jira.integracaoponto.dao.user.depara.DeParaUserDAO;
import com.mindproapps.jira.integracaoponto.model.dto.ponto.PontoDTO;
import com.mindproapps.jira.integracaoponto.service.ponto.impl.PontoServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PontoServiceImplTest {
    private PontoDAO pontoDAO;
    private DeParaUserDAO deParaUserDAO;

    private PontoServiceImpl service;

    @Before
    public void setUp() throws Exception {
        pontoDAO = Mockito.mock(PontoDAO.class);
        deParaUserDAO = Mockito.mock(DeParaUserDAO.class);
        service = new PontoServiceImpl(pontoDAO, deParaUserDAO);
    }

    @Test
    public void testGetPontoByEmailAndPeriod() {
        //ARRANGE
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);
        String pontoDate = LocalDateTime.now().plusDays(2).format(DateTimeFormatter.ISO_DATE_TIME);
        String pontoDateTooEarly = LocalDateTime.now().minusDays(3).format(DateTimeFormatter.ISO_DATE_TIME);
        String pontoDateTooLate = LocalDateTime.now().plusDays(8).format(DateTimeFormatter.ISO_DATE_TIME);
        List<PontoDTO> listPonto = Arrays.asList(
                PontoDTO.builder().email("some@email.com").dataInformacao(pontoDate).qtdeHorasTrabalhadas("03:12:00").build(),
                PontoDTO.builder().email("someother@email.com").dataInformacao(pontoDate).qtdeHorasTrabalhadas("03:12:00").build(),
                PontoDTO.builder().email("someother@email.com").dataInformacao(pontoDate).qtdeHorasTrabalhadas("03:12:00").build(),
                PontoDTO.builder().email("some@email.com").dataInformacao(pontoDateTooEarly).qtdeHorasTrabalhadas("05:12:00").build(),
                PontoDTO.builder().email("some@email.com").dataInformacao(pontoDateTooLate).qtdeHorasTrabalhadas("08:12:00").build()
        );

        List<String> lstEmails = new ArrayList<String>();
        listPonto.forEach(dto -> {
            if (!lstEmails.contains(dto.getEmail())) {
                lstEmails.add(dto.getEmail());
            }
        });
        Mockito.when(pontoDAO.getAllData(lstEmails, startDate, endDate)).thenReturn(listPonto);

        //ACT
        List<PontoDTO> sut = service.getPontoByEmailAndPeriod(listPonto, "some@email.com", startDate, endDate);
        //ASSERT
        assertNotNull(sut);
        assertEquals(1, sut.size());
        sut.forEach(pontoDTO -> assertEquals("03:12:00", pontoDTO.getQtdeHorasTrabalhadas()));
    }

}