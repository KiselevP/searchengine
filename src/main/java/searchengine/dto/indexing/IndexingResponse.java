package searchengine.dto.indexing;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class IndexingResponse {

    private Boolean result;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

}
