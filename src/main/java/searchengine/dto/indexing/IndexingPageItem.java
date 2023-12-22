package searchengine.dto.indexing;

import lombok.Data;

@Data
public class IndexingPageItem
{
    private String path;
    private Integer code;
    private String content;
}
