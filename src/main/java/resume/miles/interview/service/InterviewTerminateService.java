package resume.miles.interview.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import resume.miles.interview.dto.InterviewTerminateReasonDto;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.interview.repository.InterviewLinkRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterviewTerminateService {

    private final InterviewLinkRepository interviewLinkRepository;

    public String updateTerminateReason(InterviewTerminateReasonDto interviewTerminateReasonDto) {
        Optional<InterviewLinkEntity> findData =  interviewLinkRepository.findByToken(interviewTerminateReasonDto.getToken());
        if(findData.isEmpty()) {
            throw new RuntimeException("Interview Link Not Found For This Token");
        }
        InterviewLinkEntity interviewLinkdata = findData.get();
        interviewLinkdata.setTerminationCause(interviewTerminateReasonDto.getReason());
        interviewLinkRepository.save(interviewLinkdata);
        return "done";
    }

    public String updateTerminateReasonUser(InterviewTerminateReasonDto interviewTerminateReasonDto) {
        Optional<InterviewLinkEntity> findData =  interviewLinkRepository.findByToken(interviewTerminateReasonDto.getToken());
        if(findData.isEmpty()) {
            throw new RuntimeException("Interview Link Not Found For This Token");
        }
        InterviewLinkEntity interviewLinkdata = findData.get();

        interviewLinkdata.setUserJustification(interviewTerminateReasonDto.getReason());

        interviewLinkRepository.save(interviewLinkdata);
        return "done";
    }
}
