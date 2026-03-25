package resume.miles.codingquestion.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import resume.miles.client.repository.ClientRepository;
import resume.miles.codingquestion.repository.CodingRepository;
import resume.miles.codingquestion.specification.CodeGenerateFindSpecification;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.interview.repository.InterviewLinkRepository;
import resume.miles.interview.repository.InterviewRepository;
import resume.miles.mandatoryskill.repository.MandatorySkillRepository;
import resume.miles.musthaveskill.repository.MustHaveSkillRepository;

@Service
@RequiredArgsConstructor
public class CodingService {

    private final CodingRepository codingRepository;
    private final InterviewLinkRepository interviewLinkRepository;
    private final InterviewRepository interviewRepository;
    private final MustHaveSkillRepository mustHaveSkillRepository;
    private final MandatorySkillRepository mandatorySkillRepository;
    private final resume.miles.jobs.repository.JobRepository jobRepository;
    private final ClientRepository clientRepository;


    public String generateCode(String token) {
        InterviewLinkEntity linkEntity = interviewLinkRepository.findOne(CodeGenerateFindSpecification.getFullInterviewDetailsByToken(token))
                .orElseThrow(() -> new RuntimeException("Invalid token: No interview link found."));
        System.out.println(linkEntity.toString()+"interviewDetais");
        return "Successfully fetched data for ";
    }
}
