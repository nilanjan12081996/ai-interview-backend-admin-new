package resume.miles.config;



import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RagConfig {

    @Value("${langchain4j.pinecone.api.key}")
    private String pineconeApiKey;

    @Value("${langchain4j.pinecone.api.name}")
    private String indexName;

    @Value("${langchain4j.open-ai.chat-model.api-key}")
    private String openAiApiKey;

    // 1. Same Embedding Model as before
    @Bean
    public EmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(openAiApiKey)
                .modelName("text-embedding-3-small")
                .build();
    }

    // 2. NEW: Persistent Pinecone Store
    @Bean
    @Primary
    public EmbeddingStore pineconeEmbeddingStore() {
        return PineconeEmbeddingStore.builder()
                .apiKey(pineconeApiKey)
                .index(indexName)
                .nameSpace("client-questions")
                .build();
    }

    // 2. Update the retriever to use the new name
    @Bean
    public ContentRetriever contentRetriever(EmbeddingStore pineconeEmbeddingStore, EmbeddingModel embeddingModel) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(pineconeEmbeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(3)
                .build();
    }
}