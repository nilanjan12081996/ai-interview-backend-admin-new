package resume.miles.dashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resume.miles.dashboard.service.DashboardService;

import java.util.Map;

@RestController
@RequestMapping("/api/goodmood/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/metrics")
    public ResponseEntity<?> getDashboardMetrics() {
        try {
            Map<String, Object> metrics = dashboardService.getDashboardMetrics();
            return ResponseEntity.status(200).body(Map.of(
                    "message", "Dashboard metrics fetched successfully.",
                    "status", true,
                    "statusCode", 200,
                    "data", metrics
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 400
            ));
        }
    }

    @GetMapping("/recent-activity")
    public ResponseEntity<?> getRecentActivity() {
        try {
            java.util.List<Map<String, Object>> activity = dashboardService.getRecentActivity();
            return ResponseEntity.status(200).body(Map.of(
                    "message", "Recent activity fetched successfully.",
                    "status", true,
                    "statusCode", 200,
                    "data", activity
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                    "message", e.getMessage(),
                    "status", false,
                    "statusCode", 400
            ));
        }
    }
}
