package searchengine.services.indexing.indexing_tools;

import org.springframework.beans.factory.annotation.Autowired;
import searchengine.dto.indexing.IndexingPageItem;
import searchengine.models.Page;
import searchengine.models.repositories.PageRepository;
import searchengine.models.repositories.SiteRepository;

import java.util.*;
import java.util.concurrent.RecursiveAction;


public class Task extends RecursiveAction {
    private IndexingPageItem pageItem;
    private final List<Task> tasks = new ArrayList<>();
    private static final Map<String, IndexingPageItem> usedLink = new LinkedHashMap<>();

    @Autowired
    private final PageRepository pageRepository;
    @Autowired
    private final SiteRepository siteRepository;

    public Task(IndexingPageItem pageItem, PageRepository pageRepository, SiteRepository siteRepository) {
        this.pageItem = pageItem;
        this.pageRepository = pageRepository;
        this.siteRepository = siteRepository;
    }

    @Override
    protected void compute()
    {
        if (!usedLink.containsKey(pageItem.getPath())) {
            usedLink.put(pageItem.getPath(), pageItem);
            PageParser parser = new PageParser(pageItem);
            if (!parser.getList().isEmpty()) {
                for (IndexingPageItem item : parser.getList()) {
                    Page pageModel = new Page();
                    pageModel.setCode(item.getCode());
                    pageModel.setSiteId(siteRepository.findSiteById(item.getSiteId()));
                    pageModel.setPath(item.getPath());
                    pageModel.setContent(item.getContent());
                    pageRepository.save(pageModel);
                }


                for (IndexingPageItem childSite : parser.getList()) {
                    if (!usedLink.containsKey(childSite.getPath())) {
                        Task task = new Task(childSite, pageRepository, siteRepository);
                        task.fork();
                        tasks.add(task);
                    }
                }
            }
        }
        for (Task task : tasks) {
            task.join();
        }
    }
}
