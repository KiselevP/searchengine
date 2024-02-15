package searchengine.dto.indexing;

import lombok.Data;

@Data
public class IndexingPageItem
{
    private int id;
    private String path;
    private int code;
    private String content;
    private int siteId;
}
