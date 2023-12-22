package searchengine.dto.indexing;

import lombok.Data;

import java.util.List;

@Data
public class IndexingData
{
    private Integer pageCount;
    private List<IndexingPageItem> pageItem;
}
