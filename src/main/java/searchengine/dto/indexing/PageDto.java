package searchengine.dto.indexing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import searchengine.models.Site;

@Data
@Builder
@AllArgsConstructor
public class PageDto
{

    private Long id;

    private Site siteId;

    private String path;

    private Integer code;

    private String content;

}
