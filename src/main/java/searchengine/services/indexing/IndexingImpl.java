package searchengine.services.indexing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import searchengine.config.SitesConfigList;
import searchengine.dto.indexing.PageDto;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.models.IndexingStatus;
import searchengine.models.Site;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@AllArgsConstructor
public class IndexingImpl implements IndexingService {

    private final SitesConfigList sitesConfigList;

    private static final List<Site> sitesForValid = new ArrayList<>();

    @Getter
    @Setter
    public static AtomicBoolean is_alive;

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;

    // TODO: доделать
    @Transactional
    public IndexingResponse startIndexing() {

        IndexingResponse indexingResponse = new IndexingResponse();

        if (is_alive.get()) {

            indexingResponse.setResult(true);
            indexingResponse.setError("Индексация уже запущена");

            return indexingResponse;
        } else  {

            is_alive.set(true);
            beforeIndexingEvent();



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

    // TODO: дописать-додумать
    public static boolean isValidPath(PageDto page) {

        int count = 0;
        for (Site site : sitesForValid) {
            String rootUrl = site.getUrl();
            String key = rootUrl.substring(rootUrl.lastIndexOf("://") + 3, rootUrl.lastIndexOf(".") + 1);
            if (page.getPath().contains(key)) count = 1;
        }
        return count == 1;

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
        sitesForValid.addAll(siteRepository.findAll());
    }

    private void updateIndexedSite(Site site) {
        site.setIndexingStatus(IndexingStatus.INDEXED);
        siteRepository.save(site);
    }
}
