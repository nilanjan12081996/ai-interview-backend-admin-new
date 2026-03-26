package resume.miles.webscraping.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.WebSearchOrganicResult;
import dev.langchain4j.web.search.WebSearchResults;
import dev.langchain4j.web.search.tavily.TavilyWebSearchEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import resume.miles.client.entity.ClientEntity;
import resume.miles.client.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientDataIngestionService {

    private final ClientRepository clientRepository;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore embeddingStore;

    @Value("${langchain4j.tavily.api.key}")
    private String tavilyApiKey;

    public void scrapeAndIngestClientData() {
        System.out.println("🚀 Starting Automated Web Scraping for Client Interview Data...");

        // 1. Initialize Engines
        WebSearchEngine webSearchEngine = TavilyWebSearchEngine.builder()
                .apiKey(tavilyApiKey)
                .build();

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        // 2. Comprehensive Topic List
        String[] searchTopics = {
                // 🔴 1. CORE DATA STRUCTURES & ALGORITHMS (DSA)
                "Data Structures and Algorithms LeetCode medium hard questions",
                "Dynamic programming and Graph algorithm interview questions",
                "Arrays, Strings, and HashMap manipulation coding questions",
                "Bit manipulation and math-based programming interview questions",

                // 🟠 2. SYSTEM DESIGN & ARCHITECTURE
                "High level system design scalability architecture questions",
                "Microservices architecture and distributed systems interview questions",
                "Event-driven architecture Kafka RabbitMQ message broker questions",
                "Low level object oriented design (LLD) design patterns questions",
                "Domain Driven Design DDD and Clean Architecture interview questions",

                // 🟡 3. BACKEND ENGINEERING (THE BIG LANGUAGES)
                "Java Spring Boot REST API Hibernate core backend interview questions",
                "Node.js Express TypeScript asynchronous programming questions",
                "Python Django FastAPI backend development questions",
                "Golang concurrency Goroutines Channels microservices interview questions", // 🔥 GO
                "PHP Laravel Symfony core PHP backend interview questions",                 // 🔥 PHP
                "Ruby on Rails backend scalability and MVC interview questions",            // 🔥 RUBY
                "C# .NET Core ASP.NET backend interview questions",
                "Rust ownership borrowing memory safety backend interview questions",        // 🔥 RUST
                "C++ pointers memory management backend questions",

                // 🟢 4. FRONTEND ENGINEERING
                "React hooks state management Next.js frontend questions",
                "Angular RxJS observables component lifecycle questions",
                "Vue.js Nuxt JavaScript DOM manipulation interview questions",
                "Frontend web performance optimization and accessibility questions",
                "Micro-frontends and Module Federation interview questions",

                // 🔵 5. DATABASE & STORAGE
                "Advanced SQL queries indexing performance tuning DBA questions",
                "NoSQL MongoDB Cassandra schema design questions",
                "Redis Memcached caching strategies interview questions",
                "Elasticsearch Solr search engine and indexing interview questions",

                // 🟣 6. CLOUD, DEVOPS & SRE
                "AWS Azure GCP cloud architecture interview questions",
                "Docker Kubernetes container orchestration DevOps questions",
                "CI/CD pipelines Jenkins GitHub Actions deployment questions",
                "Terraform Infrastructure as Code (IaC) DevOps questions",
                "Site Reliability Engineering SRE monitoring Prometheus Grafana questions",

                // 🟤 7. DATA SCIENCE, AI & MACHINE LEARNING
                "Machine learning algorithms model training interview questions",
                "Data engineering PySpark Hadoop big data pipeline questions",
                "Generative AI LLM integration prompt engineering questions",
                "NLP and Computer Vision technical interview questions",

                // ⚫ 8. MOBILE DEVELOPMENT
                "Android native Kotlin coroutines architecture questions",
                "iOS native Swift UI memory management questions",
                "React Native Flutter cross-platform mobile interview questions",

                // ⚪ 9. QA, TESTING & CYBERSECURITY
                "Automation testing Selenium Cypress QA interview questions",
                "Cybersecurity OWASP top 10 penetration testing questions",
                "Smart contract security and Blockchain interview questions",

                // 🔘 10. LOW-LEVEL & EMBEDDED
                "Embedded systems C programming RTOS microcontroller questions",            // 🔥 EMBEDDED
                "Socket programming and networking protocol interview questions",

                // 🎯 11. BEHAVIORAL & AGILE
                "Behavioral interview STAR method leadership principles questions",
                "Agile Scrum master project management situational questions"
        };

        // 3. Process each Client from MySQL
        List<ClientEntity> clients = clientRepository.findAll();

        for (ClientEntity client : clients) {
            String companyName = client.getClientName();
            System.out.println("🏢 Processing Client: " + companyName);

            // 4. Nested Loop: Search every topic for this specific client
            for (String topic : searchTopics) {
                String fullSearchQuery = companyName + " " + topic + " actual interview questions";
                System.out.println("   🔍 Searching: " + fullSearchQuery);

                try {
                    WebSearchResults searchResults = webSearchEngine.search(fullSearchQuery);
                    List<Document> documentsToIngest = new ArrayList<>();

                    for (WebSearchOrganicResult result : searchResults.results()) {
                        String scrapedText = result.content() != null ? result.content() : result.snippet();

                        if (scrapedText == null || scrapedText.trim().isEmpty()) continue;

                        // Create Metadata for powerful filtering later
                        Metadata metadata = new Metadata();
                        metadata.put("client_name", companyName);
                        metadata.put("topic", topic); // Tagging the technology/domain
                        metadata.put("source_url", result.url().toString());

                        documentsToIngest.add(new Document(scrapedText, metadata));
                    }

                    // 5. Save chunks to Vector DB
                    if (!documentsToIngest.isEmpty()) {
                        ingestor.ingest(documentsToIngest);
                        System.out.println("   ✅ Ingested " + documentsToIngest.size() + " pages for " + topic);
                    }

                    // Optional: Sleep for a second to avoid hitting Tavily rate limits too hard
                    Thread.sleep(1000);

                } catch (Exception e) {
                    System.err.println("   ⚠️ Error on topic '" + topic + "': " + e.getMessage());
                }
            }
        }
        System.out.println("🎉 All client data fully ingested and indexed!");
    }
}