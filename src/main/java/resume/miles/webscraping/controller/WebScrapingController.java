package resume.miles.webscraping.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resume.miles.webscraping.service.ClientDataIngestionService;


@RestController
@RequestMapping("/api/admin/rag")
@RequiredArgsConstructor
public class WebScrapingController {
    private final ClientDataIngestionService ingestionService;

    @PostMapping("/populate")
    public String populateVectorDatabase() {
        new Thread(ingestionService::scrapeAndIngestClientData).start();
        return "Web scraping and Vector DB ingestion started in the background!";
    }

    @PostMapping("/scrap")
    public String scrapVectorDatabase() {
        new Thread(ingestionService::scrapeAndIngestTieredData).start();
        return "Web scraping and Vector DB ingestion started in the background!";
    }
}
