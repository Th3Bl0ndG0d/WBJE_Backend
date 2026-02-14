package nl.avflexologic.wbje.controllers.job;

import nl.avflexologic.wbje.controllers.JobController;
import nl.avflexologic.wbje.dtos.job.JobRequestDTO;
import nl.avflexologic.wbje.dtos.job.JobResponseDTO;
import nl.avflexologic.wbje.dtos.note.NoteResponseDTO;
import nl.avflexologic.wbje.services.JobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class TestConfig {

        @Bean
        JobService jobService() {
            return new JobService() {

                @Override
                public JobResponseDTO createJob(JobRequestDTO request) {
                    JobResponseDTO response = new JobResponseDTO();
                    response.id = 1L;
                    response.jobNumber = request.getJobNumber();
                    response.jobDate = request.getJobDate();
                    response.jobName = request.getJobName();
                    response.info = request.getInfo();
                    response.cylinderWidth = request.getCylinderWidth();
                    response.cylinderCircumference = request.getCylinderCircumference();

                    if (request.getNote() != null) {
                        response.note = new NoteResponseDTO(
                                1L,
                                request.getNote().content()
                        );
                    }

                    return response;
                }

                @Override
                public JobResponseDTO getJobById(Long id) {
                    JobResponseDTO response = new JobResponseDTO();
                    response.id = id;
                    response.jobNumber = "JB-1001";
                    response.jobDate = LocalDateTime.parse("2025-12-22T10:00:00");
                    response.jobName = "WBJE Demo";
                    response.info = "Initial setup for customer order.";
                    response.cylinderWidth = 120;
                    response.cylinderCircumference = 800;
                    response.note = new NoteResponseDTO(1L, "Note text");
                    return response;
                }

                @Override
                public List<JobResponseDTO> getAllJobs() {
                    JobResponseDTO job1 = new JobResponseDTO();
                    job1.id = 1L;
                    job1.jobNumber = "JB-1001";
                    job1.jobDate = LocalDateTime.parse("2025-12-22T10:00:00");
                    job1.jobName = "WBJE Demo";
                    job1.info = "Initial setup for customer order.";
                    job1.cylinderWidth = 120;
                    job1.cylinderCircumference = 800;
                    job1.note = new NoteResponseDTO(1L, "Note text");

                    JobResponseDTO job2 = new JobResponseDTO();
                    job2.id = 2L;
                    job2.jobNumber = "JB-1002";
                    job2.jobDate = LocalDateTime.parse("2025-12-23T09:30:00");
                    job2.jobName = "WBJE Production";
                    job2.info = "Second production run.";
                    job2.cylinderWidth = 130;
                    job2.cylinderCircumference = 820;
                    job2.note = null;

                    return List.of(job1, job2);
                }

                @Override
                public JobResponseDTO updateJob(Long id, JobRequestDTO request) {
                    JobResponseDTO response = new JobResponseDTO();
                    response.id = id;
                    response.jobNumber = request.getJobNumber();
                    response.jobDate = request.getJobDate();
                    response.jobName = request.getJobName();
                    response.info = request.getInfo();
                    response.cylinderWidth = request.getCylinderWidth();
                    response.cylinderCircumference = request.getCylinderCircumference();

                    if (request.getNote() != null) {
                        response.note = new NoteResponseDTO(
                                1L,
                                request.getNote().content()
                        );
                    }

                    return response;
                }

                @Override
                public void deleteJob(Long id) {
                }
            };
        }
    }

    @Test
    void createJob_returnsOkAndJobBody() throws Exception {
        String jsonRequest = """
                {
                  "jobNumber": "JB-1001",
                  "jobDate": "2025-12-22T10:00:00",
                  "jobName": "WBJE Demo",
                  "info": "Initial setup for customer order.",
                  "cylinderWidth": 120,
                  "cylinderCircumference": 800,
                  "note": {
                    "content": "Note text"
                  }
                }
                """;

        mockMvc.perform(post("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.jobNumber").value("JB-1001"))
                .andExpect(jsonPath("$.note.content").value("Note text"));
    }

    @Test
    void getJobById_returnsOkAndJobBody() throws Exception {
        mockMvc.perform(get("/jobs/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.note.content").value("Note text"));
    }

    @Test
    void getAllJobs_returnsOkAndList() throws Exception {
        mockMvc.perform(get("/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updateJob_returnsOkAndUpdatedBody() throws Exception {
        String jsonRequest = """
                {
                  "jobNumber": "JB-1001",
                  "jobDate": "2025-12-22T10:00:00",
                  "jobName": "WBJE Demo (updated)",
                  "info": "Updated info after customer feedback.",
                  "cylinderWidth": 120,
                  "cylinderCircumference": 800,
                  "note": {
                    "content": "Updated note text"
                  }
                }
                """;

        mockMvc.perform(put("/jobs/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobName").value("WBJE Demo (updated)"))
                .andExpect(jsonPath("$.note.content").value("Updated note text"));
    }

    @Test
    void deleteJob_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/jobs/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
