package resume.miles.codingquestion.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import resume.miles.client.repository.ClientRepository;
import resume.miles.codingquestion.agent.CodingQuestionAssistant;
import resume.miles.codingquestion.entity.CodingEntity;
import resume.miles.codingquestion.repository.CodingRepository;
import resume.miles.codingquestion.specification.CodeGenerateFindSpecification;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.interview.repository.InterviewLinkRepository;
import resume.miles.interview.repository.InterviewRepository;
import resume.miles.jobs.enums.JobLevel;
import resume.miles.mandatoryskill.repository.MandatorySkillRepository;
import resume.miles.musthaveskill.repository.MustHaveSkillRepository;

// --- New Imports Needed for Explicit RAG ---
import dev.langchain4j.store.embedding.filter.Filter;
import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.data.segment.TextSegment;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.data.embedding.Embedding;  // 👈 This is the one that was tricky!
import dev.langchain4j.model.output.Response;

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
    private final CodingQuestionAssistant aiAssistant;

    // 👈 1. INJECT PINECONE AND OPENAI DIRECTLY
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingModel embeddingModel;

//    public String generateCode(String token) {
//        InterviewLinkEntity linkEntity = interviewLinkRepository.findOne(CodeGenerateFindSpecification.getFullInterviewDetailsByToken(token))
//                .orElseThrow(() -> new RuntimeException("Invalid token: No interview link found."));
//
//        System.out.println(linkEntity.toString() + "interviewDetais");
//
//        String clientName = linkEntity.getInterview().getJobEntity().getClient().getClientName();
//        String role = linkEntity.getInterview().getJobEntity().getRole();
//        String experience = linkEntity.getInterview().getJobEntity().getExperience();
//        String description = linkEntity.getInterview().getJobEntity().getJd();
//
//        String skills = linkEntity.getInterview().getJobEntity().getMustHaveSkills()
//                .stream()
//                .map(skillEntity -> skillEntity.getSkillName())
//                .collect(Collectors.joining(", "));
//
//        System.out.println("🤖 AI Engine Starting -> Generating question for Client: " + clientName + " | Role: " + role);
//
//        // --- STEP 2: MANUALLY FETCH DATA FROM PINECONE ---
//
//        // Create the filter
//        Filter pineconeClientFilter = metadataKey("client_name").isEqualTo(clientName);
//
//        // Build a temporary retriever for this specific request
//        EmbeddingStoreContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
//                .embeddingStore(embeddingStore)
//                .embeddingModel(embeddingModel)
//                .maxResults(3) // Get the top 3 best paragraphs
//                .filter(pineconeClientFilter) // Apply the Amazon/TCS filter
//                .build();
//
//        // Search Pinecone using the Role and Skills
//        String searchQuery = "Interview questions for " + role + " covering " + skills;
//        List<Content> searchResults = retriever.retrieve(Query.from(searchQuery));
//
//        // Combine the results into a single String
//        String companyContextText = searchResults.stream()
//                .map(content -> content.textSegment().text())
//                .collect(Collectors.joining("\n\n---\n\n"));
//
//        System.out.println("📚 Found " + searchResults.size() + " context paragraphs from Pinecone!");
//
//        // --- STEP 3: PASS THE STRING TO THE AI ---
//
//        String generatedJson = aiAssistant.generateQuestion(
//                clientName,
//                role,
//                experience,
//                skills,
//                description,
//                companyContextText // 👈 We are now passing the String, not the Filter!
//        );
//
//        return generatedJson;
//    }


    // Helper 1: Capitalizes the client name (e.g., "amazon" -> "Amazon", "tcs" -> "Tcs")
    private String formatClientName(String name) {
        if (name == null || name.trim().isEmpty()) return "";
        name = name.trim();
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    // Helper 2: Converts messy HR text into strict Pinecone experience buckets
    private String normalizeExperienceBucket(String rawExperience) {
        if (rawExperience == null || rawExperience.trim().isEmpty()) {
            return "0-2"; // Default fallback
        }

        Matcher matcher = Pattern.compile("\\d+").matcher(rawExperience);
        if (matcher.find()) {
            int years = Integer.parseInt(matcher.group());
            if (years < 2) return "0-2";
            if (years >= 2 && years < 5) return "2-5";
            if (years >= 5 && years < 10) return "5-10";
            return "10+";
        }
        return "0-2";
    }


// ======================================================================================
// MAIN GENERATION METHOD
// ======================================================================================

    public String generateCode(String token) {
        // 1. Fetch Interview Details
        InterviewLinkEntity linkEntity = interviewLinkRepository.findOne(CodeGenerateFindSpecification.getFullInterviewDetailsByToken(token))
                .orElseThrow(() -> new RuntimeException("Invalid token: No interview link found."));

        // Extract & Normalize Job Data (Added .trim() to prevent hidden space bugs!)
        String rawClientName = linkEntity.getInterview().getJobEntity().getClient().getClientName();
        String clientName = formatClientName(rawClientName);

        String role = linkEntity.getInterview().getJobEntity().getRole().trim();
        String description = linkEntity.getInterview().getJobEntity().getJd().trim();

        // Convert Enum to String
        JobLevel jobLevelEnum = linkEntity.getInterview().getJobEntity().getLevel();
        String difficultyLevel = (jobLevelEnum != null) ? jobLevelEnum.name().toUpperCase().trim() : "MEDIUM";

        // 🌟 CRITICAL FIX: If your DB Enum is 'ADVANCED' (with a D), force it to match Pinecone's 'ADVANCE'
        if (difficultyLevel.equals("ADVANCED")) {
            difficultyLevel = "ADVANCE";
        }

        // Normalize the experience text to match Pinecone buckets
        String rawExperience = linkEntity.getInterview().getJobEntity().getExperience();
        String experienceBucket = normalizeExperienceBucket(rawExperience);

        // 2. Fetch ONLY Mandatory Skills
        String skills = linkEntity.getInterview().getJobEntity().getMandatorySkills()
                .stream()
                .map(skillEntity -> skillEntity.getSkillName().trim())
                .collect(Collectors.joining(", "));

        System.out.println("\n=============================================");
        System.out.println("🤖 AI Engine -> Client: " + clientName + " | Role: " + role);
        System.out.println("🎯 Profile -> Difficulty: '" + difficultyLevel + "' | Exp Bucket: '" + experienceBucket + "'");
        System.out.println("🎯 Skills -> " + skills);
        System.out.println("=============================================\n");

        // --- 3. THE SMART PINECONE FILTER WITH DEBUGGING ---
        String searchQuery = "Actual interview coding questions for " + role + " using " + skills + " at " + clientName;

        System.out.println("🔍 --- PINECONE SEARCH DEBUG INFO ---");
        System.out.println("🔍 Raw Search Query: " + searchQuery);
        System.out.println("🔍 Filter 1: difficulty_level MUST EQUAL '" + difficultyLevel + "'");
        System.out.println("🔍 Filter 2: experience_level MUST EQUAL '" + experienceBucket + "'");
        System.out.println("🔍 ----------------------------------\n");

        Filter smartFilter = metadataKey("difficulty_level").isEqualTo(difficultyLevel)
                .and(metadataKey("experience_level").isEqualTo(experienceBucket));

        EmbeddingStoreContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(4)
                .filter(smartFilter) // 🛑 If it STILL returns 0, comment THIS line out, save, and test again!
                .build();

        List<Content> searchResults = retriever.retrieve(Query.from(searchQuery));

        String companyContextText = searchResults.stream()
                .map(content -> content.textSegment().text())
                .collect(Collectors.joining("\n\n---\n\n"));

        System.out.println("📚 Found " + searchResults.size() + " precise context paragraphs from Pinecone!");

        // 4. Generate the JSON using AI
        String generatedJson = aiAssistant.generateQuestion(
                clientName,
                role,
                rawExperience,
                skills,
                difficultyLevel,
                description,
                companyContextText
        );

        System.out.println("✅ AI Generation Complete! Saving to databases...");

        // 5. Save to MySQL (Upsert Logic)
        Optional<CodingEntity> existingRecord = codingRepository.findByToken(token);
        boolean isNewRecord = existingRecord.isEmpty();

        CodingEntity codingEntity = existingRecord.orElseGet(() -> {
            CodingEntity newEntity = new CodingEntity();
            newEntity.setToken(token);
            return newEntity;
        });

        codingEntity.setQuestionData(generatedJson);
        codingRepository.save(codingEntity);
        System.out.println("💾 Saved/Updated successfully to MySQL table 'coding_questions'");

        // 6. Save back to Pinecone (Memory Loop)
        if (isNewRecord) {
            Metadata newMetadata = new Metadata();
            newMetadata.put("client_name", clientName);
            newMetadata.put("role", role);
            newMetadata.put("token", token);
            newMetadata.put("source", "ai_generated_history");
            newMetadata.put("difficulty_level", difficultyLevel);
            newMetadata.put("experience_level", experienceBucket);

            TextSegment newSegment = TextSegment.from(generatedJson, newMetadata);

            try {
                Response<Embedding> embeddingResponse = embeddingModel.embed(newSegment);
                embeddingStore.add(embeddingResponse.content(), newSegment);
                System.out.println("🧠 Successfully saved history to Pinecone!");
            } catch (Exception e) {
                System.err.println("⚠️ Could not save history to Pinecone: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ Token existed. Skipped Pinecone to prevent duplicate vectors.");
        }

        return generatedJson;
    }
    public String getQuestion(String token) {
        return codingRepository.findByToken(token)
                .map(CodingEntity::getQuestionData)
                .orElseThrow(() -> new RuntimeException("Question not found for this token."));
    }
}