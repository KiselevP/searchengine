package searchengine.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.config.SiteConfig;
import searchengine.config.SitesConfigList;
import searchengine.dto.indexing.IndexingPageItem;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.dto.indexing.IndexingSite;
import searchengine.indexingengine.Task;
import searchengine.models.IndexingStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@Service
@RequiredArgsConstructor
public class IndexingServiceImpl implements IndexingService
{
    private static List<ForkJoinPool> pools = new ArrayList<>();
    List<SiteConfig> siteConfigsList = new SitesConfigList().getSites();
    private static List<IndexingSite> rootSites = new ArrayList<>();

    public IndexingResponse executeIndexing()
    {
        for (SiteConfig siteConfig : siteConfigsList)
        {
            IndexingSite indexingSite = new IndexingSite();
            indexingSite.setName(siteConfig.getName());
            indexingSite.setUrl(siteConfig.getUrl());

            rootSites.add(indexingSite);
        }

        for (IndexingSite rootSite : rootSites) {
            ForkJoinPool pool =
                    new ForkJoinPool(Runtime.getRuntime().availableProcessors() / rootSites.size());
            pools.add(pool);
            rootSite.setIndexingStatus(IndexingStatus.INDEXING);

            IndexingPageItem pageItem = new IndexingPageItem();
            pageItem.setPath(rootSite.getUrl());

            Task task = new Task(pageItem);
            pool.invoke(task);
        }
        for (int i = 0; i < pools.size(); i++) {
            pools.get(i).close();
            rootSites.get(i).setIndexingStatus(IndexingStatus.INDEXED);
        }

        IndexingResponse response = new IndexingResponse();

        return response;
    }

    public static boolean isValidAddress(String linkToCheck) {
        boolean isValid = false;
        for (IndexingSite site : rootSites) {
            String url = site.getUrl();
            String sample = url.substring(url.lastIndexOf("://") + 3, url.lastIndexOf("."));
            if (linkToCheck.contains(sample)) {
                isValid = true;
            }
        }
        return isValid;
    }
}
