package searchengine.services.indexing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.config.SiteConfig;
import searchengine.config.SitesConfigList;
import searchengine.dto.indexing.IndexingPageItem;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.models.IndexingStatus;
import searchengine.models.Site;
import searchengine.models.repositories.PageRepository;
import searchengine.models.repositories.SiteRepository;
import searchengine.services.indexing.indexing_tools.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@Service
@RequiredArgsConstructor
public class IndexingServiceImpl implements IndexingService {
    private final SitesConfigList sitesConfigList;
    private static final List<Site> sitesForValid = new ArrayList<>();

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;

    private static Thread indexingThread;

    @Getter
    private static boolean is_alive;

    public IndexingResponse startIndexing() {
        siteRepository.deleteAll();
        pageRepository.deleteAll();


        IndexingResponse indexingResponse = new IndexingResponse();

        if (is_alive) {
            indexingResponse.setResult(false);
            indexingResponse.setError("Индексация уже запущена");
        } else {
            is_alive = true;
            indexingResponse.setResult(true);
            List<SiteConfig> configListSites = sitesConfigList.getSites();

            for (SiteConfig site : configListSites) {
                Site indexingSite = new Site();
                indexingSite.setName(site.getName());
                indexingSite.setStatusTime(LocalDateTime.now());
                indexingSite.setUrl(site.getUrl());
                indexingSite.setIndexingStatus(IndexingStatus.INDEXING);

                siteRepository.save(indexingSite);
            }
            sitesForValid.addAll(siteRepository.findAll());

            if (configListSites.isEmpty()) {
                throw new NullPointerException("пустой config list");
            } else {
                indexingThread = new Thread(() -> {
                    for (SiteConfig site : configListSites) {
                        try (ForkJoinPool pool = new ForkJoinPool(sitesConfigList.getSites().size())) {
                            IndexingPageItem rootPage = new IndexingPageItem();
                            Site site1 = siteRepository.findSiteByName(site.getName());

                            rootPage.setSiteId(site1.getId());
                            rootPage.setPath(site1.getUrl());

                            Task task = new Task(rootPage, pageRepository, siteRepository);
                            pool.invoke(task);
                        } catch (RuntimeException e) {
                            e.getMessage();
                        }
                    }
                });
                indexingThread.start();
            }
        }
        return indexingResponse;
    }

    @Override
    public IndexingResponse stopIndexing() {
        IndexingResponse indexingResponse = new IndexingResponse();

        if (!is_alive) {
            indexingResponse.setResult(false);
            indexingResponse.setError("Индексация не запущена");
        } else {
            indexingThread.interrupt();
            is_alive = false;
            indexingResponse.setResult(true);
        }

        return indexingResponse;
    }

    @Override
    public IndexingResponse startIndexingPage() {
        IndexingResponse indexingResponse = new IndexingResponse();
        indexingResponse.setResult(true);

        return indexingResponse;
    }

    public static boolean isValidAddress(IndexingPageItem page) {
        int count = 0;
        for (Site site : sitesForValid) {
            if (page.getSiteId() == site.getId()) {
                String rootUrl = site.getUrl();
                String key = rootUrl.substring(rootUrl.lastIndexOf("://") + 3, rootUrl.lastIndexOf(".") + 1);
                if (page.getPath().contains(key)) count = 1;
            }
        }
        return count == 1;
//        return true;
    }
}
