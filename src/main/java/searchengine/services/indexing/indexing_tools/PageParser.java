package searchengine.services.indexing.indexing_tools;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import searchengine.dto.indexing.PageDto;
import searchengine.services.indexing.IndexingImpl;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Thread.sleep;

@Data
@Service
public class PageParser {

    private final Map<String, PageDto> mapParsingPages = new ConcurrentHashMap<>();

    private boolean isValidPage(PageDto pageItem) {

        String regexPath = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

        return !mapParsingPages.containsKey(pageItem.getPath()) &&
                pageItem.getPath().matches(regexPath) &&
                IndexingImpl.isValidPath(pageItem) &&
                !pageItem.getPath().contains(".jpg") &&
                !pageItem.getPath().contains(".png") &&
                !pageItem.getPath().contains(".pdf") &&
                !pageItem.getPath().contains(".css");
    }

    public PageParser(PageDto pageItem) {
        synchronized (pageItem) {
            try {
                sleep(150);
                int responseCode = Jsoup.connect(pageItem.getPath())
                        .timeout(2000)
                        .ignoreHttpErrors(true)
                        .followRedirects(false)
                        .ignoreContentType(true)
                        .execute().statusCode();

                sleep(150);
                Document doc = Jsoup.connect(pageItem.getPath())
                        .userAgent("Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36")
                        .referrer("http://www.google.com")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .timeout(2000)
                        .followRedirects(false)
                        .get();

                Elements elements = doc.select("a");

                for (Element element : elements) {
                    String url = element.absUrl("href");

                    PageDto childPage = PageDto.builder()
                            .path(url)
                            .siteId(pageItem.getSiteId())
                            .code(responseCode)
                            .build();

                    if (isValidPage(childPage)) {
                        mapParsingPages.put(childPage.getPath(), childPage);
                    }
                }

            } catch (SocketTimeoutException e) {
                System.out.println("Таймаут при чтении данных");
            } catch (IOException | InterruptedException e) {
                System.err.println("Ошибка со стороны сайта");
            }
        }
    }
}

