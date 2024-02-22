package searchengine.services.indexing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.config.SitesConfigList;
import searchengine.dto.indexing.IndexingPageItem;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.models.Site;
import searchengine.models.repositories_tools.RepositoriesService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IndexingImpl implements IndexingService {
    private final SitesConfigList sitesConfigList;
    private static final List<Site> sitesForValid = new ArrayList<>();

    private final RepositoriesService repositoriesService;

    @Getter
    private static boolean is_alive;

    // TODO: переделать
    public IndexingResponse startIndexing()
    {
        IndexingResponse indexingResponse = new IndexingResponse();

        repositoriesService.clearRepositories("site");
        repositoriesService.clearRepositories("page");



        indexingResponse.setResult(true);
        return indexingResponse;
    }

    // TODO: дописать-додумать
    @Override
    public IndexingResponse stopIndexing() {
        IndexingResponse indexingResponse = new IndexingResponse();

        if (!is_alive) {
            indexingResponse.setResult(false);
            indexingResponse.setError("Индексация не запущена");
        } else {
            is_alive = false;
//            pool.shutdownNow();

            indexingResponse.setResult(true);
        }

        return indexingResponse;
    }

    // TODO: дописать-додумать
    @Override
    public IndexingResponse startIndexingPage() {
        IndexingResponse indexingResponse = new IndexingResponse();
        indexingResponse.setResult(true);

        return indexingResponse;
    }

    // TODO: дописать-додумать
    public static boolean isValidAddress(IndexingPageItem page) {
        int count = 0;
        for (Site site : sitesForValid) {
            String rootUrl = site.getUrl();
            String key = rootUrl.substring(rootUrl.lastIndexOf("://") + 3, rootUrl.lastIndexOf(".") + 1);
            if (page.getPath().contains(key)) count = 1;
        }
        return count == 1;
    }
}