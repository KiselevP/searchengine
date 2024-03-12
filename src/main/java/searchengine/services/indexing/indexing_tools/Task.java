package searchengine.services.indexing.indexing_tools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.dto.indexing.PageDto;
import searchengine.models.Page;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;
import searchengine.services.indexing.IndexingImpl;

import java.util.*;
import java.util.concurrent.RecursiveAction;

@RequiredArgsConstructor
@AllArgsConstructor
@Service
@Builder
public class Task extends RecursiveAction {

    private final List<PageDto> itemList;

    private static final List<Task> tasks = new ArrayList<>();

    private static final Map<String, PageDto> usedLink = new LinkedHashMap<>();

    private final PageRepository pageRepository;
    private final SiteRepository siteRepository;

    public Task(List<PageDto> childPageList) {
    }


    @Override
    protected void compute() {
        for (PageDto pageItem : itemList) {
            if (!usedLink.containsKey(pageItem.getPath())
                    && IndexingImpl.getIs_alive().get()) {
                usedLink.put(pageItem.getPath(), pageItem);
                PageParser parser = new PageParser(pageItem);
                if (!parser.getMapParsingPages().isEmpty()) {
                    List<PageDto> childPageList = new ArrayList<>();
                    for (PageDto childPage : parser.getMapParsingPages().values()) {
                        Page pageModel = Page.builder()
                                .code(childPage.getCode())
                                .site(siteRepository.getById(childPage.getSiteId().getId()))
                                .path(childPage.getPath())
                                .content(childPage.getContent())
                                .build();

                        if (pageRepository.findPageByPath(pageModel.getPath()).isPresent()) {
                            usedLink.remove(pageModel.getPath());
                        } else {
                            pageRepository.save(pageModel);
                            childPageList.add(childPage);
                        }
                    }
                    Task task = new Task(childPageList);
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