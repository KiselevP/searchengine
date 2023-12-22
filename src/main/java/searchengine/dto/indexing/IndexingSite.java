package searchengine.dto.indexing;

import lombok.Data;
import searchengine.models.IndexingStatus;

@Data
public class IndexingSite
{
    private IndexingStatus indexingStatus;
    private String lastError;
    private String url;
    private String name;
}
