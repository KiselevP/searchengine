package searchengine.services.search;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.dto.search.SearchResponse;

@Service
@RequiredArgsConstructor
@Data
public class SearchServiceImpl implements SearchService
{
    @Override
    public SearchResponse search() {
        return null;
    }
}