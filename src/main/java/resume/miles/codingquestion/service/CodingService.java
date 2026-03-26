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
        public String generateCode(String token) {
            // 1. Fetch Interview Details
            InterviewLinkEntity linkEntity = interviewLinkRepository.findOne(CodeGenerateFindSpecification.getFullInterviewDetailsByToken(token))
                    .orElseThrow(() -> new RuntimeException("Invalid token: No interview link found."));

            // Extract Job Data
            String clientName = linkEntity.getInterview().getJobEntity().getClient().getClientName().trim();
            String role = linkEntity.getInterview().getJobEntity().getRole();
            String experience = linkEntity.getInterview().getJobEntity().getExperience();
            String description = linkEntity.getInterview().getJobEntity().getJd();

            // 2. Fetch ONLY Mandatory Skills
            String skills = linkEntity.getInterview().getJobEntity().getMandatorySkills()
                    .stream()
                    .map(skillEntity -> skillEntity.getSkillName())
                    .collect(Collectors.joining(", "));

            System.out.println("🤖 AI Engine Starting -> Client: " + clientName + " | Role: " + role);
            System.out.println("🎯 Mandatory Skills: " + skills);

            // 3. Fetch Data from Pinecone (RAG)
            Filter pineconeClientFilter = metadataKey("client_name").isEqualTo(clientName);
            EmbeddingStoreContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
                    .embeddingStore(embeddingStore)
                    .embeddingModel(embeddingModel)
                    .maxResults(3)
                    .filter(pineconeClientFilter)
                    .build();

            String searchQuery = "Interview questions for " + role + " covering " + skills;
            List<Content> searchResults = retriever.retrieve(Query.from(searchQuery));

            String companyContextText = searchResults.stream()
                    .map(content -> content.textSegment().text())
                    .collect(Collectors.joining("\n\n---\n\n"));

            System.out.println("📚 Found " + searchResults.size() + " context paragraphs from Pinecone!");

            // 4. Generate the JSON using AI
            String generatedJson = aiAssistant.generateQuestion(
                    clientName,
                    role,
                    experience,
                    skills,
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

            // 6. Save to Pinecone (Deduplication Logic)
            if (isNewRecord) {
                Metadata newMetadata = new Metadata();
                newMetadata.put("client_name", clientName);
                newMetadata.put("role", role);
                newMetadata.put("token", token);
                newMetadata.put("source", "ai_generated_history");

                TextSegment newSegment = TextSegment.from(generatedJson, newMetadata);
                Response<Embedding> embeddingResponse = embeddingModel.embed(newSegment);

                embeddingStore.add(embeddingResponse.content(), newSegment);
                System.out.println("🧠 Saved successfully to Pinecone Vector Database!");
            } else {
                System.out.println("⚠️ Token existed. Updated MySQL, but skipped Pinecone to prevent duplicate vectors.");
            }

            return generatedJson;
        }
}