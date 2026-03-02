package resume.miles.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;

public class TranscriptionFormatter {
     public static String formatTranscript(String rawTranscript) {

        if (rawTranscript == null || rawTranscript.isEmpty()) {
            return "";
        }

        // Remove duplicate blocks
        String[] lines = rawTranscript.split("\n");
        Set<String> uniqueLines = new LinkedHashSet<>();

        for (String line : lines) {
            uniqueLines.add(line.trim());
        }

        StringBuilder formatted = new StringBuilder();

        formatted.append("INTERVIEW TRANSCRIPT\n");
        formatted.append("Generated On: ")
                .append(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                .append("\n");
        formatted.append("--------------------------------------------------\n\n");

        for (String line : uniqueLines) {

            if (line.contains("[Interviewer]")) {
                formatted.append("\n🟢 INTERVIEWER:\n");
                formatted.append(line.replace("[Interviewer]:", "").trim());
                formatted.append("\n");
            } else if (line.contains("[Candidate]")) {
                formatted.append("\n🔵 CANDIDATE:\n");
                formatted.append(line.replace("[Candidate]:", "").trim());
                formatted.append("\n");
            } else if (!line.isBlank()) {
                formatted.append(line).append("\n");
            }
        }

        return formatted.toString();
    }
}
