package searchengine.services.indexing.indexing_tools;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.RecursiveAction;

import static java.lang.Thread.sleep;

@RequiredArgsConstructor
@AllArgsConstructor
@Service
public class PagesCrawler extends RecursiveAction {

    private final String pageUrl;

    @Override
    protected void compute() {
        synchronized (pageUrl) {
            try {
                int statusCode = getStatusCode(pageUrl);
                Document htmlPageBody = getHtmlPageBody(pageUrl);



            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            } catch (IOException | InterruptedException ignored) {
            }
        }

    }

    private synchronized int getStatusCode(String pageUrl)
            throws InterruptedException, IOException {

        sleep(150);
        return Jsoup.connect(pageUrl)
                .timeout(2000)
                .ignoreHttpErrors(true)
                .followRedirects(false)
                .ignoreContentType(true)
                .execute()
                .statusCode();
    }

    private synchronized Document getHtmlPageBody(String pageUrl)
            throws IOException, InterruptedException {

        sleep(150);
        return Jsoup.connect(pageUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36")
                .referrer("http://www.google.com")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .timeout(2000)
                .followRedirects(false)
                .get();
    }

}