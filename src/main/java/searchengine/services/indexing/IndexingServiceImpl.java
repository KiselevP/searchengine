package searchengine.services.indexing;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.config.SiteConfig;
import searchengine.config.SitesConfigList;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.models.IndexingStatus;
import searchengine.models.Site;
import searchengine.repositories.SiteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@Service
@RequiredArgsConstructor
@Data
public class IndexingServiceImpl implements IndexingService
{
    private final SitesConfigList sitesConfigList;

    private final SiteRepository siteRepository;

    public IndexingResponse startIndexing()
    {
        List<SiteConfig> configListSites = sitesConfigList.getSites();

        IndexingResponse indexingResponse = new IndexingResponse();

        if (!configListSites.isEmpty()) {
            new Thread(() -> {

                indexingResponse.setResult(true);
                for (SiteConfig site : configListSites) {
//                    try (ForkJoinPool pool = new ForkJoinPool(sitesConfigList.getSites().size()))
//                    {
//                        IndexingPageItem rootSitePage = new IndexingPageItem();
//                        rootSitePage.setPath(site.getUrl());
//                        Task task = new Task(rootSitePage);
//                        pool.invoke(task);

//                        Site siteCheck = siteRepository.findSiteByName(site.getName());
//
//                        if (siteCheck.getIndexingStatus().equals(IndexingStatus.INDEXED))
//                        {
//                            indexingResponse.setResult(false);
//                            indexingResponse.setError("Индексация уже запущена");
//                        }

                        Site indexingSite = new Site();
                        indexingSite.setName(site.getName());
                        indexingSite.setStatusTime(LocalDateTime.now());
                        indexingSite.setUrl(site.getUrl());
                        indexingSite.setIndexingStatus(IndexingStatus.INDEXING);

                        siteRepository.save(indexingSite);
//                    }
//                    catch (Exception e) {
//                        //
//                    }
                }
            }).start();
        }

        return indexingResponse;
    }

    public static boolean isValidAddress(String path) {
        return true;
    }
}
