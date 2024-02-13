package searchengine.dto.indexing;

import lombok.Data;

@Data
public class IndexingPageItem
{
    private int id;
    private int siteId;
    private String path;
    private Integer code;
    private String content;
}
