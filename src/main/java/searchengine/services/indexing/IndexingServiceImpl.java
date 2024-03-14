package searchengine.services.indexing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import searchengine.config.SitesConfigList;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.dto.indexing.PageDto;
import searchengine.dto.indexing.SiteDto;
import searchengine.models.IndexingStatus;
import searchengine.models.Site;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;
import searchengine.services.indexing.indexing_tools.PagesCrawler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class IndexingServiceImpl implements IndexingService {

    private static final Logger logger = Logger.getAnonymousLogger();

    private final SitesConfigList sitesConfigList;

    @Getter
    private static final List<SiteDto> sitesForValid = new ArrayList<>();

    @Getter
    @Setter
    private static volatile HashMap<String, PageDto> listVisitedPages;

    @Getter
    public static AtomicBoolean is_alive;

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;

    // TODO: доделать
    @Transactional
    public IndexingResponse startIndexing() {

        IndexingResponse indexingResponse = new IndexingResponse();

        if (is_alive.get()) {

            indexingResponse.setResult(false);
            indexingResponse.setError("Индексация уже запущена");

            return indexingResponse;
        } else  {

            is_alive.set(true);
            beforeIndexingEvent();

            ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            try {
                List<PagesCrawler> taskList = new ArrayList<>();

                for (SiteDto rootSite : sitesForValid) {

                    PagesCrawler indexingTask = new PagesCrawler(rootSite.getUrl());

                    pool.invoke(indexingTask);
                    taskList.add(indexingTask);
                }

                for (PagesCrawler task : taskList) {
                    task.join();
                }

            } catch (Exception rte) {
                logger.info(rte.getMessage());
            } finally {
                pool.shutdown();
            }

            indexingResponse.setResult(true);
        }

        return indexingResponse;

    }

    // TODO: дописать-додумать
    @Override
    public IndexingResponse stopIndexing() {

        IndexingResponse indexingResponse = new IndexingResponse();

        if (!is_alive.get()) {
            indexingResponse.setResult(false);
            indexingResponse.setError("Индексация не запущена");
        } else {
            is_alive.set(false);
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


    @Transactional
    private void beforeIndexingEvent() {

        if (!siteRepository.findAll().isEmpty() &&
            !pageRepository.findAll().isEmpty()) {

            for (Site site : siteRepository.findAll()) {
                pageRepository.deleteBySiteId(site.getId());
                siteRepository.deleteById(site.getId());
            }
        }

        sitesConfigList.getSites().forEach(site ->
                siteRepository.save(
                        Site.builder()
                                .name(site.getName())
                                .url(site.getUrl())
                                .indexingStatus(IndexingStatus.INDEXING)
                                .statusTime(LocalDateTime.now())
                                .build()
                )
        );

        siteRepository.findAll().forEach(site ->
            sitesForValid.add(
                    SiteDto.builder()
                            .id(site.getId())
                            .indexingStatus(site.getIndexingStatus())
                            .statusTime(site.getStatusTime())
                            .url(site.getUrl())
                            .name(site.getName())
                            .build())
        );

    }

    private void updateIndexedSite(Site site) {
        site.setIndexingStatus(IndexingStatus.INDEXED);
        siteRepository.save(site);
    }
}
