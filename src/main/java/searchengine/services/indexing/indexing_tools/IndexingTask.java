package searchengine.services.indexing.indexing_tools;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.concurrent.RecursiveAction;

import static java.lang.Thread.sleep;

@RequiredArgsConstructor
@Service
public class IndexingTask extends RecursiveAction {

    private final String rootUrl;

    @Override
    protected void compute() {

        sleep(150);
        int responseCode = Jsoup.connect(pageDto.getPath())
                .timeout(2000)
                .ignoreHttpErrors(true)
                .followRedirects(false)
                .ignoreContentType(true)
                .execute().statusCode();

        sleep(150);
        Document htmlPageBody = Jsoup.connect(pageDto.getPath())
                .userAgent("Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36")
                .referrer("http://www.google.com")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .timeout(2000)
                .followRedirects(false)
                .get();

    }

}