package searchengine.services.indexing.indexing_tools;
import lombok.Getter;
import org.springframework.stereotype.Service;
import searchengine.dto.indexing.IndexingPageItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveAction;

public class Task extends RecursiveAction
{
    private final IndexingPageItem pageItem;
    private final List<Task> tasks = new ArrayList<>();
    private static final Map<String, IndexingPageItem> usedLink = new LinkedHashMap<>();

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
