package searchengine.services.indexing.indexing_tools;

import lombok.Data;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchengine.dto.indexing.IndexingPageItem;
import searchengine.services.indexing.IndexingImpl;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

@Data
public class PageParser
{
    private Map<String, IndexingPageItem> mapParsingPages;


    private boolean isValidPage(IndexingPageItem pageItem) {
        String regexPath = "https?://[^#, \\s]*\\.?[a-z]*\\.[a-z]{2,4}[^#,\\s]*";

        return !mapParsingPages.containsKey(pageItem.getPath())
                && pageItem.getPath().matches(regexPath)
                && IndexingImpl.isValidAddress(pageItem)
                && !pageItem.getPath().contains(".jpg")
                && !pageItem.getPath().contains(".png")
                && !pageItem.getPath().contains(".pdf")
                && !pageItem.getPath().contains(".css");
    }

    public PageParser(IndexingPageItem pageItem) {
        synchronized (pageItem) {
            try {
                sleep(150);
                mapParsingPages = new HashMap<>();
                Document doc = Jsoup.connect(pageItem.getPath())
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true).timeout(2000)
                        .followRedirects(false).get();
                Elements elements = doc.select("a");
                for (Element element : elements) {
                    String url = element.absUrl("href");
                    IndexingPageItem newPages = new IndexingPageItem();

                    Connection.Response response =
                            Jsoup.connect(pageItem.getPath())
                                    .followRedirects(false)
                                    .execute();

                    newPages.setPath(url);
                    newPages.setSiteId(pageItem.getSiteId());
                    newPages.setCode(response.statusCode());
                    newPages.setContent(doc.html());

                    if (isValidPage(newPages)) {
                        mapParsingPages.put(newPages.getPath(), newPages);
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

