package searchengine.services.indexing.indexing_tools;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.dto.indexing.IndexingPageItem;

import java.util.*;
import java.util.concurrent.RecursiveAction;


@Service
@RequiredArgsConstructor
public class Task extends RecursiveAction
{
    private IndexingPageItem pageItem;
    private final List<Task> tasks = new ArrayList<>();

    @Getter
    private static final Map<String, IndexingPageItem> usedLink = new HashMap<>();

    public Task(IndexingPageItem pageItem) {
        this.pageItem = pageItem;
    }

    @Override
    protected void compute()
    {
        if (!usedLink.containsKey(pageItem.getPath()))
        {
            usedLink.put(pageItem.getPath(), pageItem);
            HtmlParser parser = new HtmlParser(pageItem);
            if (!parser.getList().isEmpty())
            {
                for (IndexingPageItem childSite : parser.getList())
                {
                    if (!usedLink.containsKey(childSite.getPath()))
                    {
                        Task task = new Task(childSite);
                        task.fork();
                        tasks.add(task);
                    }
                }
            }
        }
        for (Task task : tasks)
        {
            task.join();
        }
    }
}
