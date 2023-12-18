package searchengine.indexingengine;

import searchengine.models.Site;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveAction;

public class Task extends RecursiveAction {
    private final Site site;
    private static final Map<String, Site> usedLink = new LinkedHashMap<>();
    private final List<Task> tasks = new ArrayList<>();

    private boolean isError;
    public boolean isError() {
        return isError;
    }
    public void setError(boolean error) {
        isError = error;
    }

    public Task(Site site) {
        this.site = site;
    }

    @Override
    protected void compute()
    {
        if (!usedLink.containsKey(site.getUrl()))
        {
            usedLink.put(site.getUrl(), site);
            HtmlParser parser = new HtmlParser(site);
            if (!parser.getList().isEmpty())
            {
                for (Site childSite : parser.getList())
                {
                    if (!usedLink.containsKey(childSite.getUrl()))
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
