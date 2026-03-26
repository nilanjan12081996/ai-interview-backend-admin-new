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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import resume.miles.client.entity.ClientEntity;
import resume.miles.client.repository.ClientRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    private static final Map<String, List<String>> DIFFICULTY_TIERS = new LinkedHashMap<>();
    static {
        // ADVANCE: Global Tech Giants, AI Labs, and High-Frequency Trading
        DIFFICULTY_TIERS.put("ADVANCE", List.of(
                "Google", "Amazon", "Meta", "Apple", "Microsoft", "Netflix",
                "Databricks", "Uber", "OpenAI", "Anthropic", "ByteDance",
                "Snowflake", "Palantir", "Jane Street", "Citadel", "Two Sigma",
                "NVIDIA", "DeepMind", "Bloomberg", "LinkedIn", "Tesla"
        ));

        // HARD: Scale-ups, Unicorns, and Top-Tier FinTech
        DIFFICULTY_TIERS.put("HARD", List.of(
                "Stripe", "Airbnb", "Atlassian", "Swiggy", "Zomato", "Flipkart",
                "Paytm", "Gojek", "Spotify", "Razorpay", "Cred", "PhonePe",
                "Meesho", "Slack", "Shopify", "DoorDash", "Coinbase", "Canva",
                "Postman", "BrowserStack", "Discord", "Pinterest", "Robinhood"
        ));

        // MEDIUM: Global System Integrators, Enterprise Software, and Big Banks
        DIFFICULTY_TIERS.put("MEDIUM", List.of(
                "TCS", "Cognizant", "Infosys", "Wipro", "Accenture", "Capgemini",
                "Tech Mahindra", "IBM", "HCL", "L&T Technology Services",
                "Oracle", "SAP", "Cisco", "JP Morgan Chase", "Goldman Sachs",
                "Deloitte", "Morgan Stanley", "Barclays", "EPAM", "Mindtree"
        ));

        // LOW: Early-stage, Agencies, and Local Setups
        DIFFICULTY_TIERS.put("LOW", List.of(
                "Local Tech Startup", "Digital Web Agency", "IT Support Services",
                "Small E-commerce Company", "Boutique Software House",
                "EdTech Startup", "WordPress Development Agency", "Freelance Client",
                "Local SaaS Provider"
        ));
    }

    private static final Map<String, String> EXPERIENCE_LEVELS = new LinkedHashMap<>();
    static {
        EXPERIENCE_LEVELS.put("0-2", "fresher entry level 0 to 2 years experience");
        EXPERIENCE_LEVELS.put("2-5", "mid level SDE 2 to 5 years experience");
        EXPERIENCE_LEVELS.put("5-10", "senior engineer 5 to 10 years experience");
        EXPERIENCE_LEVELS.put("10+", "staff principal architect 10+ years experience");
    }

    private static final String[] INTERVIEW_ROUNDS = {
            "Online Assessment OA HackerRank Codility questions",
            "Machine Coding Round low level implementation actual experience",
            "Whiteboard DSA Data Structures Algorithms live coding round",
            "High Level System Design Architecture whiteboard round",
            "Hiring Manager Behavioral Leadership Principles STAR method round",
            "Core Subject Fundamentals OS DBMS Computer Networks round"
    };

    private static final String[] CORE_TOPICS = {
            // ALGORITHMS & DATA STRUCTURES
            "Array String Two Pointers HashMap LeetCode",
            "Binary Tree Graph DFS BFS Traversal LeetCode",
            "Dynamic Programming Backtracking Greedy LeetCode",
            // BACKEND SPECIFIC
            "Backend Java Spring Boot Hibernate Concurrency",
            "Backend Node.js Event Loop Express TypeScript",
            "Backend Python Django FastAPI Asyncio",
            "Backend Golang Goroutines Channels Microservices",
            "Backend C# .NET Core Entity Framework",
            // FRONTEND SPECIFIC
            "Frontend React Component Lifecycle Hooks State Management",
            "Frontend Angular RxJS Observables Dependency Injection",
            "Frontend JavaScript DOM Manipulation Browser Rendering",
            // DATA ENGINEERING & DATABASES
            "Database Relational SQL Advanced Query Indexing Optimization",
            "Database NoSQL MongoDB Cassandra Schema Design",
            "Data Engineering Spark Hadoop Kafka Data Pipeline",
            "Caching Redis Memcached Distributed Cache",
            // DEVOPS, CLOUD & SRE
            "Cloud AWS Azure GCP Infrastructure Deployment",
            "DevOps Docker Kubernetes Container Orchestration",
            "CI/CD Pipelines Jenkins GitHub Actions Terraform",
            // MOBILE & TESTING
            "Mobile Android Kotlin Coroutines Architecture",
            "Mobile iOS Swift UI Memory Management",
            "QA Automation Selenium Cypress Testing",
            // CUTTING EDGE TECH
            "Generative AI LLM Integration Prompt Engineering NLP",
            "Machine Learning PyTorch TensorFlow Model Training",
            "Cybersecurity OWASP Penetration Testing Cryptography",
            "Web3 Smart Contracts Solidity Ethereum Blockchain",
            "C++ Unreal Engine Unity OpenGL Graphics Programming"
    };

    // ========================================================================
    // 🚀 HERE IS YOUR METHOD THAT LOOPS THROUGH EVERYTHING ABOVE
    // ========================================================================

    @Async // Runs in a separate thread automatically!
    public void scrapeAndIngestTieredData() {
        System.out.println("🚀 Starting MASSIVE AI Memory Ingestion for ALL TIERS...");

        WebSearchEngine webSearchEngine = TavilyWebSearchEngine.builder()
                .apiKey(tavilyApiKey)
                .build();

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        // 🌟 NEW OUTER LOOP: Automatically process every tier one by one
        for (Map.Entry<String, List<String>> tierEntry : DIFFICULTY_TIERS.entrySet()) {
            String currentTier = tierEntry.getKey();
            List<String> companies = tierEntry.getValue();

            System.out.println("\n🔥 =========================================");
            System.out.println("🔥 PROCESSING DIFFICULTY TIER: " + currentTier);
            System.out.println("🔥 =========================================");

            for (String company : companies) {
                System.out.println("\n🏢 Scraping Source: " + company);

                for (Map.Entry<String, String> expEntry : EXPERIENCE_LEVELS.entrySet()) {
                    String yoeKey = expEntry.getKey();
                    String yoeQuery = expEntry.getValue();

                    for (String roundFormat : INTERVIEW_ROUNDS) {
                        for (String topic : CORE_TOPICS) {

                            // Construct the massive, hyper-specific query
                            String fullSearchQuery = company + " " + roundFormat + " " + topic + " " + yoeQuery;
                            System.out.println("   🔍 Search: " + fullSearchQuery);

                            try {
                                WebSearchResults searchResults = webSearchEngine.search(fullSearchQuery);
                                List<Document> documentsToIngest = new ArrayList<>();

                                for (WebSearchOrganicResult result : searchResults.results()) {
                                    String scrapedText = result.content() != null ? result.content() : result.snippet();
                                    if (scrapedText == null || scrapedText.trim().isEmpty()) continue;

                                    // 🌟 METADATA TAGGING 🌟
                                    Metadata metadata = new Metadata();
                                    metadata.put("difficulty_level", currentTier);
                                    metadata.put("source_company", company);
                                    metadata.put("experience_level", yoeKey);
                                    metadata.put("interview_round", roundFormat);
                                    metadata.put("topic", topic);
                                    metadata.put("source_url", result.url().toString());

                                    documentsToIngest.add(new Document(scrapedText, metadata));
                                }

                                if (!documentsToIngest.isEmpty()) {
                                    ingestor.ingest(documentsToIngest);
                                    System.out.println("     ✅ Saved " + documentsToIngest.size() + " contexts to Pinecone.");
                                }

                                // 🛑 CRITICAL: Sleep for 3 seconds to avoid getting IP Banned by Tavily
                                Thread.sleep(3000);

                            } catch (Exception e) {
                                System.err.println("     ⚠️ Error retrieving data: " + e.getMessage());
                            }
                        }
                    }
                }
            }
        }
        System.out.println("🎉 MASSIVE Ingestion Complete! All Tiers have been saved to Pinecone.");
    }
}