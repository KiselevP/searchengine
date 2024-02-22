package searchengine.services.indexing.indexing_tools;

import org.springframework.beans.factory.annotation.Autowired;
import searchengine.dto.indexing.IndexingPageItem;
import searchengine.models.Page;
import searchengine.models.repositories.PageRepository;
import searchengine.models.repositories.SiteRepository;
import searchengine.services.indexing.IndexingImpl;

import java.util.*;
import java.util.concurrent.RecursiveAction;


public class Task extends RecursiveAction {
    private final List<IndexingPageItem> itemList;
    private static final List<Task> tasks = new ArrayList<>();
    private static final Map<String, IndexingPageItem> usedLink = new LinkedHashMap<>();

    @Autowired
    private final PageRepository pageRepository;
    @Autowired
    private final SiteRepository siteRepository;

    public Task(List<IndexingPageItem> itemList,
                PageRepository pageRepository,
                SiteRepository siteRepository) {
        this.itemList = itemList;
        this.pageRepository = pageRepository;
        this.siteRepository = siteRepository;
    }

    @Override
    protected void compute() {
        for (IndexingPageItem pageItem : itemList) {
            if (!usedLink.containsKey(pageItem.getPath()) && IndexingImpl.is_alive()) {
                usedLink.put(pageItem.getPath(), pageItem);
                PageParser parser = new PageParser(pageItem);
                if (!parser.getMapParsingPages().isEmpty()) {
                    List<IndexingPageItem> childPageList = new ArrayList<>();
                    for (IndexingPageItem childPage : parser.getMapParsingPages().values()) {
                        Page pageModel = new Page();
                        pageModel.setCode(childPage.getCode());
                        pageModel.setSiteId(siteRepository.findSiteById(childPage.getSiteId()));
                        pageModel.setPath(childPage.getPath());
                        pageModel.setContent(childPage.getContent());

                        if (pageRepository.findPageByPath(pageModel.getPath()) != null) {
                            usedLink.remove(pageModel.getPath());
                        } else {
                            pageRepository.save(pageModel);
                            childPageList.add(childPage);
                        }
                    }
                    Task task = new Task(childPageList, pageRepository, siteRepository);
                    task.fork();
                    tasks.add(task);
                }
                for (Task task : tasks) {
                    task.join();
                }
            }
        }
    }
}