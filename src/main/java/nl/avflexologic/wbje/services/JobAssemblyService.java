package nl.avflexologic.wbje.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import nl.avflexologic.wbje.dtos.cylinder.CylinderRequestDTO;
import nl.avflexologic.wbje.dtos.cylinder.CylinderResponseDTO;
import nl.avflexologic.wbje.dtos.job.JobRequestDTO;
import nl.avflexologic.wbje.dtos.job.JobResponseDTO;
import nl.avflexologic.wbje.dtos.report.ReportRequestDTO;
import nl.avflexologic.wbje.entities.TapeSpecEntity;
import nl.avflexologic.wbje.entities.ReportSpecEntity;
import nl.avflexologic.wbje.repositories.TapeSpecRepository;
import nl.avflexologic.wbje.repositories.ReportSpecRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class JobAssemblyService {

    private final JobService jobService;
    private final CylinderService cylinderService;
    private final ReportService reportService;
    private final ObjectMapper objectMapper;
    private final TapeSpecRepository tapeSpecRepository;
    private final ReportSpecRepository reportSpecRepository;

    public JobAssemblyService(
            JobService jobService,
            CylinderService cylinderService,
            ReportService reportService,
            ObjectMapper objectMapper,
            TapeSpecRepository tapeSpecRepository,
            ReportSpecRepository reportSpecRepository
    ) {
        this.jobService = jobService;
        this.cylinderService = cylinderService;
        this.reportService = reportService;
        this.objectMapper = objectMapper;
        this.tapeSpecRepository = tapeSpecRepository;
        this.reportSpecRepository = reportSpecRepository;
    }

    /**
     * Maakt in één keer een volledige Job inclusief Cylinders en Reports aan.
     * Verwachte JSON-structuur:
     *
     * {
     *   "job": { ... },
     *   "cylinders": [
     *     {
     *       "cylinderNr": 1,
     *       "color": "Cyan",
     *       "cylinderInfo": "First",
     *       "tapeSpecId": 1,
     *       "reports": [
     *         {
     *           "reportNr": 1,
     *           "reportWidth": 320,
     *           "xOffset": 0,
     *           "yOffset": 0,
     *           "reportSpecId": 3
     *         }
     *       ]
     *     }
     *   ]
     * }
     */
    @Transactional
    public JobResponseDTO createFullJob(Map<String, Object> body) {

        // 0. Map -> JsonNode
        JsonNode root = objectMapper.valueToTree(body);
        JsonNode jobNode = root.get("job");

        if (jobNode == null || jobNode.isNull()) {
            throw new IllegalArgumentException("Request body must contain 'job' object.");
        }

        // -----------------------------
        // 1. JobRequestDTO bouwen
        // -----------------------------
        LocalDateTime jobDate = LocalDateTime.parse(jobNode.get("jobDate").asText());

        JobRequestDTO jobDto = new JobRequestDTO(jobDate);
        jobDto.setJobNumber(jobNode.get("jobNumber").asText());
        jobDto.setJobName(jobNode.get("jobName").asText());
        jobDto.setCylinderWidth(jobNode.get("cylinderWidth").asInt());
        jobDto.setCylinderCircumference(jobNode.get("cylinderCircumference").asInt());
        jobDto.setInfo(jobNode.get("info").asText());
        jobDto.setNoteInfo(
                jobNode.has("noteInfo") && !jobNode.get("noteInfo").isNull()
                        ? jobNode.get("noteInfo").asText()
                        : null
        );

        JobResponseDTO createdJob = jobService.createJob(jobDto);
        Long jobId = createdJob.id;

        // -----------------------------
        // 2. Cylinders verwerken
        // -----------------------------
        JsonNode cylindersNode = root.get("cylinders");
        if (cylindersNode != null && cylindersNode.isArray()) {

            for (JsonNode cylinderNode : cylindersNode) {

                Long resolvedTapeSpecId = resolveTapeSpecId(cylinderNode);

                CylinderRequestDTO cylinderDto = new CylinderRequestDTO(
                        cylinderNode.get("cylinderNr").asInt(),
                        cylinderNode.get("color").asText(),
                        cylinderNode.get("cylinderInfo").asText(),
                        resolvedTapeSpecId
                );

                CylinderResponseDTO savedCylinder =
                        cylinderService.createCylinder(jobId, cylinderDto);

                Long cylinderId = savedCylinder.id();

                // -----------------------------
                // 3. Reports (plates) verwerken
                // -----------------------------
                JsonNode reportsNode = cylinderNode.get("reports");
                if (reportsNode != null && reportsNode.isArray()) {
                    for (JsonNode reportNode : reportsNode) {

                        Long resolvedReportSpecId = resolveReportSpecId(reportNode);

                        ReportRequestDTO reportDto = new ReportRequestDTO(
                                reportNode.get("reportNr").asInt(),
                                reportNode.get("reportWidth").asInt(),
                                reportNode.get("xOffset").asInt(),
                                reportNode.get("yOffset").asInt(),
                                cylinderId,
                                resolvedReportSpecId
                        );

                        reportService.createReport(reportDto);
                    }
                }
            }
        }

        return createdJob;
    }

    /**
     * Zorgt ervoor dat er een geldige TapeSpec bestaat.
     * - Als tapeSpecId aanwezig én bestaand is -> gebruik dat ID.
     * - Als tapeSpecId ontbreekt of niet bestaat -> maak een nieuwe TapeSpec aan.
     */
    private Long resolveTapeSpecId(JsonNode cylinderNode) {
        if (cylinderNode.has("tapeSpecId") && !cylinderNode.get("tapeSpecId").isNull()) {
            Long requestedId = cylinderNode.get("tapeSpecId").asLong();
            return tapeSpecRepository.findById(requestedId)
                    .map(TapeSpecEntity::getId)
                    .orElseGet(() -> createDefaultTapeSpec(cylinderNode).getId());
        }

        // Geen id meegegeven -> nieuwe aanmaken
        return createDefaultTapeSpec(cylinderNode).getId();
    }

    /**
     * Maakt een nieuwe TapeSpecEntity met generieke/default waarden.
     * Pas dit aan op jouw echte TapeSpecEntity velden.
     */
    private TapeSpecEntity createDefaultTapeSpec(JsonNode cylinderNode) {
        TapeSpecEntity tapeSpec = new TapeSpecEntity();
        // TODO: pas onderstaande setters aan naar jouw echte entity-velden

        // Voorbeeld (als je zulke velden hebt):
        tapeSpec.setTapeName("Auto-generated TapeSpec for cylinder " + cylinderNode.get("cylinderNr").asInt());
        tapeSpec.setInfo("Created automatically by JobAssemblyService");
        tapeSpec.setThickness(40);

        return tapeSpecRepository.save(tapeSpec);
    }

    /**
     * Zorgt ervoor dat er een geldige ReportSpec bestaat.
     * - Als reportSpecId aanwezig én bestaand is -> gebruik dat ID.
     * - Als reportSpecId ontbreekt of niet bestaat -> maak een nieuwe ReportSpec aan.
     */
    private Long resolveReportSpecId(JsonNode reportNode) {
        if (reportNode.has("reportSpecId") && !reportNode.get("reportSpecId").isNull()) {
            Long requestedId = reportNode.get("reportSpecId").asLong();
            return reportSpecRepository.findById(requestedId)
                    .map(ReportSpecEntity::getId)
                    .orElseGet(() -> createDefaultReportSpec(reportNode).getId());
        }

        // Geen id meegegeven -> nieuwe aanmaken
        return createDefaultReportSpec(reportNode).getId();
    }

    /**
     * Maakt een nieuwe ReportSpecEntity met generieke/default waarden.
     * Pas dit aan op jouw echte ReportSpecEntity velden.
     */
    private ReportSpecEntity createDefaultReportSpec(JsonNode reportNode) {
        ReportSpecEntity reportSpec = new ReportSpecEntity();
        // TODO: pas onderstaande setters aan naar echte entity-velden

        // Voorbeeld (als je zulke velden hebt):
        // reportSpec.setName("Auto-generated ReportSpec " + reportNode.get("reportNr").asInt());
        // reportSpec.setRepeatLength(1000);
        // reportSpec.setDescription("Created automatically by JobAssemblyService");

        return reportSpecRepository.save(reportSpec);
    }
}
