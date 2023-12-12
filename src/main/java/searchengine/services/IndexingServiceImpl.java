package searchengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.indexingengine.Link;
import searchengine.indexingengine.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@Service
@RequiredArgsConstructor
public class IndexingServiceImpl implements IndexingService {

    static List<Site> rootUrls = new SitesList().getSites();

    @Override
    public void executeIndexing() {

        List<ForkJoinPool> pools = new ArrayList<>();

        for (Site site : rootUrls) {
            ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() / rootUrls.size());
            pools.add(pool);
            Link rootLink = new Link(site.getUrl());
            Task task = new Task(rootLink);
            pool.invoke(task);
        }
        for(ForkJoinPool pool : pools) {
            pool.close();
        }
    }

    public static boolean isValidAddress(String linkToCheck) {
        boolean isValid = false;
        for (Site site : rootUrls) {
            String url = site.getUrl();
            String sample = url.substring(url.lastIndexOf("://") + 3, url.lastIndexOf("."));
            if (linkToCheck.contains(sample)) {
                isValid = true;
            }
        }
        return isValid;
    }
}
