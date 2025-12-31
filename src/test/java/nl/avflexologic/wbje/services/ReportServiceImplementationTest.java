package nl.avflexologic.wbje.services;

import nl.avflexologic.wbje.dtos.report.ReportRequestDTO;
import nl.avflexologic.wbje.dtos.report.ReportResponseDTO;
import nl.avflexologic.wbje.entities.CylinderEntity;
import nl.avflexologic.wbje.entities.JobEntity;
import nl.avflexologic.wbje.entities.ReportEntity;
import nl.avflexologic.wbje.entities.ReportSpecEntity;
import nl.avflexologic.wbje.entities.TapeSpecEntity;
import nl.avflexologic.wbje.mappers.ReportDTOMapper;
import nl.avflexologic.wbje.repositories.CylinderRepository;
import nl.avflexologic.wbje.repositories.ReportRepository;
import nl.avflexologic.wbje.repositories.ReportSpecRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReportServiceImplementationTest {

    @Test
    void createReport_persists_and_returns_dto() {
        ReportRepository reportRepository = mock(ReportRepository.class);
        CylinderRepository cylinderRepository = mock(CylinderRepository.class);
        ReportSpecRepository reportSpecRepository = mock(ReportSpecRepository.class);
        ReportDTOMapper reportDTOMapper = new ReportDTOMapper();

        ReportService service = new ReportServiceImplementation(
                reportRepository,
                cylinderRepository,
                reportSpecRepository,
                reportDTOMapper
        );

        JobEntity job = new JobEntity(LocalDateTime.now());
        TapeSpecEntity tapeSpec = new TapeSpecEntity();
        CylinderEntity cylinder = new CylinderEntity(job, tapeSpec, 1);

        // De service/mapper zal cylinderId afleiden uit cylinder.getId(), dus deze moet gezet zijn.
        setIdViaReflection(cylinder, 10L);

        // ReportSpec is gemockt; de mapper zal reportSpecId meestal afleiden uit reportSpec.getId().
        ReportSpecEntity reportSpec = mock(ReportSpecEntity.class);
        when(reportSpec.getId()).thenReturn(20L);

        when(cylinderRepository.findById(10L)).thenReturn(Optional.of(cylinder));
        when(reportSpecRepository.findById(20L)).thenReturn(Optional.of(reportSpec));

        ArgumentCaptor<ReportEntity> captor = ArgumentCaptor.forClass(ReportEntity.class);
        when(reportRepository.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

        ReportRequestDTO request = new ReportRequestDTO(
                1,
                400,
                10,
                20,
                10L,
                20L
        );

        ReportResponseDTO response = service.createReport(request);

        assertNotNull(response);
        assertEquals(1, response.reportNr());
        assertEquals(400, response.reportWidth());
        assertEquals(10, response.xOffset());
        assertEquals(20, response.yOffset());
        assertEquals(10L, response.cylinderId());
        assertEquals(20L, response.reportSpecId());

        ReportEntity persisted = captor.getValue();
        assertNotNull(persisted);
        assertEquals(1, persisted.getReportNr());
        assertEquals(cylinder, persisted.getCylinder());
    }

    /**
     * Testhulp: zet het JPA-id veld op een entity zonder database roundtrip.
     * Werkt ook wanneer er geen publieke setId(...) aanwezig is.
     */
    private static void setIdViaReflection(Object entity, Long id) {
        try {
            Field idField = findField(entity.getClass(), "id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (Exception ex) {
            fail("Failed to set id via reflection on " + entity.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }

    private static Field findField(Class<?> type, String fieldName) throws NoSuchFieldException {
        Class<?> current = type;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field '" + fieldName + "' not found in class hierarchy of " + type.getName());
    }
}
