package searchengine.controllers.indexing;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.services.indexing.IndexingService;

@RestController
@RequestMapping("/api")
public class IndexingController
{
    private final IndexingService indexingService;

    public IndexingController(IndexingService indexingService)
    {
        this.indexingService = indexingService;
    }

    @GetMapping("/start-indexing")
    public ResponseEntity<IndexingResponse> startIndexing()
    {
        return ResponseEntity.ok(indexingService.startIndexing());
    }

    @GetMapping("/stop-indexing")
    public ResponseEntity<IndexingResponse> stopIndexing()
    {
        return ResponseEntity.ok(indexingService.stopIndexing());
    }

    @PostMapping("/index-page")
    public ResponseEntity<IndexingResponse> startIndexingPage()
    {
        return ResponseEntity.ok(indexingService.startIndexingPage());
    }
}
