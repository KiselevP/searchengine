package searchengine.dto.indexing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import searchengine.models.IndexingStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class SiteDto {

    private Long id;

    private IndexingStatus indexingStatus;

    private LocalDateTime statusTime;

    private String lastError;

    private String url;

    private String name;

}
