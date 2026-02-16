package resume.miles.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync // Optional, if you want to use @Async annotation later
public class AsyncConfig {

    @Bean("loginTaskExecutor")
    public ThreadPoolTaskExecutor loginTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // Always keep 2 threads ready
        executor.setMaxPoolSize(10); // Grow to 10 if busy
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("otp-email-");
        
        // OPTIONAL: If you want extra threads to die faster (e.g., 30 seconds)
        executor.setKeepAliveSeconds(30); 
        
        // IMPOTANT: Ensure emails finish sending if server restarts
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        
        executor.initialize();
        return executor;
    }
}
