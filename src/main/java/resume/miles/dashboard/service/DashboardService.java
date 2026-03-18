package resume.miles.dashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import resume.miles.interview.entity.InterviewEntity;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.interview.repository.InterviewLinkRepository;
import resume.miles.interview.repository.InterviewRepository;
import resume.miles.jobs.repository.JobRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final InterviewRepository interviewRepository;
    private final JobRepository jobRepository;
    private final InterviewLinkRepository interviewLinkRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardMetrics() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyDaysAgo = now.minusDays(30);
        LocalDateTime sevenDaysAgo = now.minusDays(7);

        // 1. Total Candidates
        long totalCandidates = interviewRepository.count();
        long newCandidatesLastMonth = interviewRepository.countByCreatedAtAfter(thirtyDaysAgo);

        // 2. Active Jobs
        long activeJobs = jobRepository.countByStatus(1);
        long newJobsLastMonth = jobRepository.countByStatusAndCreatedAtAfter(1, thirtyDaysAgo);

        // 3. Interviews Today
        List<InterviewEntity> todayInterviews = interviewRepository.findByInterviewDate(LocalDate.now());
        int interviewsTodayCount = todayInterviews.size();
        long completedToday = 0;
        long pendingToday = 0;

        for (InterviewEntity interview : todayInterviews) {
            boolean isCompleted = false;
            List<InterviewLinkEntity> links = interviewLinkRepository.findAllByInterview(interview);
            for (InterviewLinkEntity link : links) {
                if (link.getIs_complete() != null && link.getIs_complete() == 1) {
                    isCompleted = true;
                    break;
                }
            }
            if (isCompleted) {
                completedToday++;
            } else {
                pendingToday++;
            }
        }

        // 4. Conversion Rate (Completed Interviews / Total Candidates)
        List<InterviewLinkEntity> allLinks = interviewLinkRepository.findAll();
        long totalCompleted = allLinks.stream()
                .filter(l -> l.getIs_complete() != null && l.getIs_complete() == 1)
                .map(l -> l.getInterview().getId())
                .distinct()
                .count();
        double conversionRate = totalCandidates == 0 ? 0 : ((double) totalCompleted / totalCandidates) * 100.0;

        // Calculate past conversion rate (Before 7 days)
        long totalCandidatesPast = interviewRepository.countByCreatedAtBefore(sevenDaysAgo);
        long totalCompletedPast = allLinks.stream()
                .filter(l -> l.getIs_complete() != null && l.getIs_complete() == 1)
                .filter(l -> l.getCreatedAt() != null && l.getCreatedAt().isBefore(sevenDaysAgo))
                .map(l -> l.getInterview().getId())
                .distinct()
                .count();

        double pastConversionRate = totalCandidatesPast == 0 ? 0 : ((double) totalCompletedPast / totalCandidatesPast) * 100.0;
        double conversionRateChange = conversionRate - pastConversionRate;

        // Build Response Map
        Map<String, Object> metrics = new HashMap<>();

        metrics.put("totalCandidates", Map.of(
                "value", totalCandidates,
                "subtext", "+" + newCandidatesLastMonth + " from last month"
        ));

        metrics.put("activeJobs", Map.of(
                "value", activeJobs,
                "subtext", "+" + newJobsLastMonth + " new positions"
        ));

        metrics.put("interviewsToday", Map.of(
                "value", interviewsTodayCount,
                "subtext", completedToday + " completed, " + pendingToday + " pending"
        ));

        metrics.put("conversionRate", Map.of(
                "value", String.format("%.1f%%", conversionRate),
                "subtext", String.format("%s%.1f%% from last week", (conversionRateChange >= 0 ? "+" : ""), conversionRateChange)
        ));

        return metrics;
    }
}
