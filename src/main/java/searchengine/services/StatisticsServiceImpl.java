package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.config.ConfigSite;
import searchengine.config.ConfigSitesList;
import searchengine.dto.statistics.DetailedStatisticsItem;
import searchengine.dto.statistics.StatisticsData;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.TotalStatistics;
import searchengine.indexingengine.Task;
import searchengine.models.IndexingStatus;
import searchengine.models.Site;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService
{
    static List<ConfigSite> rootUrls = new ConfigSitesList().getSites();
    private final Random random = new Random();
    private final ConfigSitesList sites;

    @Override
    public StatisticsResponse getStatistics() {

        List<ForkJoinPool> pools = new ArrayList<>();
        List<Site> listRootSites = new ArrayList<>();

        for (ConfigSite site : rootUrls) {
            ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() / rootUrls.size());
            pools.add(pool);
            Site rootSite = new Site();
            listRootSites.add(rootSite);
            rootSite.setUrl(site.getUrl());
            rootSite.setIndexingStatus(IndexingStatus.INDEXING);
            Task task = new Task(rootSite);
            pool.invoke(task);
        }
        for (int i = 0; i < pools.size(); i++) {
            pools.get(i).close();
            listRootSites.get(i).setIndexingStatus(IndexingStatus.INDEXED);
        }

        String[] statuses = {"INDEXED", "FAILED", "INDEXING"};
        String[] errors = {
                "Ошибка индексации: главная страница сайта не доступна",
                "Ошибка индексации: сайт не доступен",
                ""
        };

        TotalStatistics total = new TotalStatistics();
        total.setSites(sites.getSites().size());
        total.setIndexing(true);

        List<DetailedStatisticsItem> detailed = new ArrayList<>();
        List<ConfigSite> sitesList = sites.getSites();
        for (ConfigSite site : sitesList) {
            DetailedStatisticsItem item = new DetailedStatisticsItem();
            item.setName(site.getName());
            item.setUrl(site.getUrl());
            int pages = 1;
            int lemmas = 1;
            item.setPages(pages);
            item.setLemmas(lemmas);
            item.setStatus(statuses[2]);
            item.setError(errors[2]);
            item.setStatusTime(System.currentTimeMillis() -
                    (random.nextInt(10_000)));
            total.setPages(total.getPages() + pages);
            total.setLemmas(total.getLemmas() + lemmas);
            detailed.add(item);
        }

        StatisticsResponse response = new StatisticsResponse();
        StatisticsData data = new StatisticsData();
        data.setTotal(total);
        data.setDetailed(detailed);
        response.setStatistics(data);
        response.setResult(true);
        return response;
    }

    public static boolean isValidAddress(String linkToCheck) {
        boolean isValid = false;
        for (ConfigSite site : rootUrls) {
            String url = site.getUrl();
            String sample = url.substring(url.lastIndexOf("://") + 3, url.lastIndexOf("."));
            if (linkToCheck.contains(sample)) {
                isValid = true;
            }
        }
        return isValid;
    }
}
