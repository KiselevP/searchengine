package searchengine.dto.indexing;

import lombok.Data;

@Data
public class IndexingResponse
{
    private boolean result;
    private IndexingData indexing;
}
