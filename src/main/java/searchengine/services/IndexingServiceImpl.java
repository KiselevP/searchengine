package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.config.SiteConfig;
import searchengine.config.SitesConfigList;
import searchengine.dto.indexing.IndexingPageItem;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.indexingengine.Task;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

@Service
@RequiredArgsConstructor
public class IndexingServiceImpl implements IndexingService
{
    private final SitesConfigList sitesConfigList;

    public static boolean isValidAddress(String path) {
        return true;
    }

    public IndexingResponse executeIndexing()
    {
        List<SiteConfig> configListSites= sitesConfigList.getSites();

        for (SiteConfig site : configListSites)
        {
            try
            {
                ForkJoinPool pool = new ForkJoinPool(sitesConfigList.getSites().size());
                IndexingPageItem rootSitePage = new IndexingPageItem();
                Task task = new Task(rootSitePage);
                pool.invoke(task);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

        }

        return  null;
    }
}
