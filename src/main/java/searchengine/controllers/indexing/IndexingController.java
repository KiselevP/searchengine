package searchengine.controllers.indexing;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.services.indexing.IndexingService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/indexing/")
public class IndexingController {

    private final IndexingService indexingService;

    @GetMapping("/start")
    public ResponseEntity<IndexingResponse> startIndexing() {

        return ResponseEntity.ok(indexingService.startIndexing());

    }

    @GetMapping("/stop")
    public ResponseEntity<IndexingResponse> stopIndexing() {

        return ResponseEntity.ok(indexingService.stopIndexing());

    }

    @PostMapping("/start-page")
    public ResponseEntity<IndexingResponse> startIndexingPage() {

        return ResponseEntity.ok(indexingService.startIndexingPage());

    }

}
