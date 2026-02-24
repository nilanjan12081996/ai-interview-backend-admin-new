package resume.miles.jobs.service;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import resume.miles.client.entity.ClientEntity;
import resume.miles.client.repository.ClientRepository;
import resume.miles.jobs.dto.JobDto;
import resume.miles.jobs.entity.JobEntity;
import resume.miles.jobs.mapper.JobMapper;
import resume.miles.jobs.repository.JobRepository;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final ClientRepository clientRepository;
    private final JobMapper jobMapper;

    // 🔥 CREATE JOB
    public JobDto createJob(JobDto dto) {

        // 1️⃣ Find or create client
        ClientEntity client = clientRepository
                .findByClientName(dto.getClientName())
                .orElseGet(() -> {
                    ClientEntity newClient = ClientEntity.builder()
                            .clientName(dto.getClientName())
                            .status(1)
                            .build();
                    return clientRepository.save(newClient);
                });

        // 2️⃣ Generate unique jobId
        String generatedJobId = generateUniqueJobId();

        // 3️⃣ Convert DTO → Entity
        JobEntity jobEntity = JobEntity.builder()
                .jobId(generatedJobId)
                .client(client)
                .role(dto.getRole())
                .jd(dto.getJd())
                .build();

        // 4️⃣ Save
        JobEntity saved = jobRepository.save(jobEntity);

        return jobMapper.toDto(saved);
    }

    // 🔥 LIST ALL JOBS
    public List<JobDto> listJobs() {
        return jobRepository.findAll()
                .stream()
                .map(jobMapper::toDto)
                .toList();
    }

    // 🔥 Unique Job ID Generator
    private String generateUniqueJobId() {

        String jobId;
        do {
            jobId = "JOB-" + java.util.UUID.randomUUID()
                    .toString()
                    .substring(0, 8)
                    .toUpperCase();
        } while (jobRepository.findByJobId(jobId).isPresent());

        return jobId;
    }


@Transactional
public JobDto getJobById(Long id) {

    JobEntity job = jobRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Job not found"));

    return jobMapper.toDto(job);
}
@Transactional
public JobDto toggleJobStatus(Long jobId) {

    JobEntity job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Job not found"));

    // 🔥 Toggle logic
    if (job.getStatus() == null || job.getStatus() == 0) {
        job.setStatus(1);
    } else {
        job.setStatus(0);
    }

    JobEntity updated = jobRepository.save(job);

    return jobMapper.toDto(updated);
}

@Transactional
public JobDto updateJob(Long id, JobDto dto) {

    // 1️⃣ Find existing job
    JobEntity job = jobRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Job not found"));

    // 2️⃣ Update Client if changed
    if (dto.getClientName() != null) {

        ClientEntity client = clientRepository
                .findByClientName(dto.getClientName())
                .orElseGet(() -> {
                    ClientEntity newClient = ClientEntity.builder()
                            .clientName(dto.getClientName())
                            .status(1)
                            .build();
                    return clientRepository.save(newClient);
                });

        job.setClient(client);
    }

    // 3️⃣ Update other fields
    if (dto.getRole() != null) {
        job.setRole(dto.getRole());
    }

    if (dto.getJd() != null) {
        job.setJd(dto.getJd());
    }

    // 4️⃣ Save updated job
    JobEntity updated = jobRepository.save(job);

    return jobMapper.toDto(updated);
}


@Transactional
public void deleteJob(Long id) {

    JobEntity job = jobRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Job not found"));

    jobRepository.delete(job);
}

}


