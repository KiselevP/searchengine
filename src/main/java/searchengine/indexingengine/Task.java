package searchengine.indexingengine;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveAction;

public class Task extends RecursiveAction {
    private final Link link;
    private static final Map<String, Link> usedLink = new LinkedHashMap<>();
    private final List<Task> tasks = new ArrayList<>();

    public Task(Link link) {
        this.link = link;
    }

    @Override
    protected void compute()
    {
        if (!usedLink.containsKey(link.getAddress()))
        {
            usedLink.put(link.getAddress(), link);
            HtmlParser parser = new HtmlParser(link);
            if (!parser.getList().isEmpty())
            {
                for (Link childLink : parser.getList())
                {
                    if (!usedLink.containsKey(childLink.getAddress()))
                    {
                        childLink.setLevel(link.getLevel() + 1);
                        childLink.setParentAddress(link.getAddress());

                        Task task = new Task(childLink);
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
