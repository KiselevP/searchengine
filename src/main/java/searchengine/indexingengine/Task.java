package searchengine.indexingengine;

import searchengine.dto.indexing.IndexingPageItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveAction;

public class Task extends RecursiveAction {
    private final IndexingPageItem pageItem;
    private static final Map<String, IndexingPageItem> usedLink = new LinkedHashMap<>();
    private final List<Task> tasks = new ArrayList<>();

    private boolean isError;
    public boolean isError() {
        return isError;
    }
    public void setError(boolean error) {
        isError = error;
    }

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
