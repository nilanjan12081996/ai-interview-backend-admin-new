package resume.miles.dashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import resume.miles.interview.entity.InterviewEntity;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.interview.repository.InterviewLinkRepository;
import resume.miles.interview.repository.InterviewRepository;
import resume.miles.jobs.entity.JobEntity;
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

        List<InterviewLinkEntity> allLinks = interviewLinkRepository.findAll();

        // 3. Interviews Today
        // Pending = Scheduled for today but not yet completed
        List<InterviewEntity> todayInterviews = interviewRepository.findByInterviewDate(LocalDate.now());
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
            if (!isCompleted) {
                pendingToday++;
            }
        }

        // Completed = Any interview link actually completed today (based on updated_at/created_at)
        long completedToday = allLinks.stream()
                .filter(l -> l.getIs_complete() != null && l.getIs_complete() == 1)
                .filter(l -> {
                    LocalDateTime dt = l.getUpdatedAt() != null ? l.getUpdatedAt() : l.getCreatedAt();
                    return dt != null && dt.toLocalDate().equals(LocalDate.now());
                })
                .count();

        long interviewsTodayCount = pendingToday + completedToday;
       

        // 4. Conversion Rate (Completed Interviews / Total Candidates)
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

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getRecentActivity() {
        List<Map<String, Object>> activities = new java.util.ArrayList<>();

        // 1. New Candidate Applied
        List<InterviewEntity> recentCandidates = interviewRepository.findTop10ByOrderByCreatedAtDesc();
        for (InterviewEntity candidate : recentCandidates) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("type", "CANDIDATE_APPLIED");
            activity.put("title", "New Candidate Applied");
            String roleName = candidate.getJobEntity() != null ? candidate.getJobEntity().getRole() : "Unknown Role";
            activity.put("description", candidate.getCandidateName() + " applied for " + roleName);
            activity.put("timestamp", candidate.getCreatedAt());
            activities.add(activity);
        }

        // 2. Interview Completed
        List<InterviewLinkEntity> completedInterviews = interviewLinkRepository.findRecentCompletedInterviews(org.springframework.data.domain.PageRequest.of(0, 10));
        for (InterviewLinkEntity link : completedInterviews) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("type", "INTERVIEW_COMPLETED");
            activity.put("title", "Interview Completed");
            String candidateName = link.getInterview() != null ? link.getInterview().getCandidateName() : "Unknown Candidate";
            activity.put("description", candidateName + " completed the coding assessment");
            // If updatedAt is not available, fallback to createdAt
            LocalDateTime ts = link.getUpdatedAt() != null ? link.getUpdatedAt() : link.getCreatedAt();
            activity.put("timestamp", ts);
            activities.add(activity);
        }

        // 3. New Job Posted
        List<JobEntity> recentJobs = jobRepository.findTop10ByOrderByCreatedAtDesc();
        for (JobEntity job : recentJobs) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("type", "JOB_POSTED");
            activity.put("title", "New Job Posted");
            String clientName = job.getClient() != null ? job.getClient().getClientName() : "Unknown Company";
            activity.put("description", job.getRole() + " role at " + clientName);
            activity.put("timestamp", job.getCreatedAt());
            activities.add(activity);
        }

        // Sort dynamically by timestamp descending
        activities.sort((a, b) -> {
            LocalDateTime timeA = (LocalDateTime) a.get("timestamp");
            LocalDateTime timeB = (LocalDateTime) b.get("timestamp");
            if (timeA == null && timeB == null) return 0;
            if (timeA == null) return 1;
            if (timeB == null) return -1;
            return timeB.compareTo(timeA);
        });

        // Calculate timeAgo
        LocalDateTime now = LocalDateTime.now();
        for (Map<String, Object> activity : activities) {
            LocalDateTime timestamp = (LocalDateTime) activity.get("timestamp");
            activity.put("timeAgo", calculateTimeAgo(timestamp, now));
        }

        // Return top 10
        return activities.size() > 10 ? activities.subList(0, 10) : activities;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getInterviewActivity() {
        List<Map<String, Object>> activityData = new java.util.ArrayList<>();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6); // Last 7 days including today

        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("EEE");

        // Pre-fetch all links to calculate completed sessions per day efficiently
        List<InterviewLinkEntity> allLinks = interviewLinkRepository.findAll();

        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            String dayName = date.format(formatter);
            
            // Fetch scheduled interviews for this date
            List<InterviewEntity> interviews = interviewRepository.findByInterviewDate(date);
            int sessions = interviews != null ? interviews.size() : 0;
            
            // Calculate completed sessions ON this specific date directly from links
            long completedSessions = allLinks.stream()
                    .filter(l -> l.getIs_complete() != null && l.getIs_complete() == 1)
                    .filter(l -> {
                        LocalDateTime dt = l.getUpdatedAt() != null ? l.getUpdatedAt() : l.getCreatedAt();
                        return dt != null && dt.toLocalDate().equals(date);
                    })
                    .count();
            
            Map<String, Object> dailyData = new HashMap<>();
            // Capitalize to match "Mon", "Tue", etc. depending on locale, EEE usually handles it.
            // Ensure first letter is capitalized just in case
            dayName = dayName.substring(0, 1).toUpperCase() + dayName.substring(1);
            
            dailyData.put("name", dayName);
            dailyData.put("sessions", sessions); // Total scheduled sessions
            dailyData.put("completed", completedSessions); // Completed sessions
            dailyData.put("date", date.toString()); // useful for debugging or alternative display
            
            activityData.add(dailyData);
        }
        
        return activityData;
    }

    private String calculateTimeAgo(LocalDateTime past, LocalDateTime now) {
        if (past == null) return "Unknown";
        java.time.Duration duration = java.time.Duration.between(past, now);
        long seconds = duration.getSeconds();
        if (seconds < 60) {
            return "Just now";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + "m ago";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + "h ago";
        } else {
            long days = seconds / 86400;
            return days + "d ago";
        }
    }
}
