package ut.com.mindproapps.jira.integracaoponto.service.period.impl;

import com.mindproapps.jira.integracaoponto.dao.period.AuditPeriodDAO;
import com.mindproapps.jira.integracaoponto.exception.InvalidDateIntervalException;
import com.mindproapps.jira.integracaoponto.model.dto.period.AuditPeriodDTO;
import com.mindproapps.jira.integracaoponto.service.i18n.I18nService;
import com.mindproapps.jira.integracaoponto.service.period.impl.AuditPeriodServiceImpl;
import com.mindproapps.jira.integracaoponto.validator.PeriodValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

public class AuditPeriodServiceImplTest {

    private AuditPeriodDAO auditPeriodDAO;
    private PeriodValidator periodValidator;
    private I18nService i18nService;
    private AuditPeriodServiceImpl service;

    @Before
    public void setUp() throws Exception {
        auditPeriodDAO = Mockito.mock(AuditPeriodDAO.class);
        i18nService = Mockito.mock(I18nService.class);
        periodValidator = new PeriodValidator();
        service = new AuditPeriodServiceImpl(auditPeriodDAO, periodValidator, i18nService);
    }

    @Test
    public void testGetListByIntervalNoErrors() {
        //ARRANGE
        List<AuditPeriodDTO> dtoList = Arrays.asList(
                AuditPeriodDTO.builder().id(1L).build(),
                AuditPeriodDTO.builder().id(2L).build()
        );
        Mockito.when(auditPeriodDAO.getListAuditPeriod(anyString(), anyString())).thenReturn(dtoList);
        //ACT
        List<AuditPeriodDTO> sut = service.getListByInterval("2020-10-01", "2020-10-30");
        //ASSERT
        assertEquals(dtoList, sut);
    }

    @Test(expected = InvalidDateIntervalException.class)
    public void testGetListByIntervalInvalidDatePeriod() {
        // ACT
        service.getListByInterval("2020-10-30", "2020-10-01");
    }

    @Test(expected = DateTimeParseException.class)
    public void testGetListByIntervalInvalidStartDateFormat() {
        // ACT
        service.getListByInterval("whatever", "2020-10-30");
    }

    @Test(expected = DateTimeParseException.class)
    public void testGetListByIntervalInvalidEndDateFormat() {
        // ACT
        service.getListByInterval("2020-10-01", "whatever");
    }

}