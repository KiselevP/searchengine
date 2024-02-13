package searchengine.services.indexing.indexing_tools;

import lombok.Data;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchengine.dto.indexing.IndexingPageItem;
import searchengine.services.indexing.IndexingServiceImpl;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

@Data
public class HtmlParser
{
    private List<IndexingPageItem> list;

    private boolean isValidPage(IndexingPageItem pageItem) {
        String regexPath = "http[s]?://[^#, \\s]*\\.?[a-z]*\\.[a-z]{2,4}[^#,\\s]*";
        return !list.contains(pageItem) && pageItem.getPath().matches(regexPath);
    }

    public HtmlParser(IndexingPageItem pageItem) {
        synchronized (pageItem) {
            try {
                sleep(150);
                list = new ArrayList<>();
                Document doc = Jsoup.connect(pageItem.getPath())
                        .ignoreHttpErrors(false)
                        .ignoreContentType(true).timeout(2000)
                        .followRedirects(false).get();

                Elements elements = doc.select("a");
                for (Element element : elements) {
                    String url = element.absUrl("href");
                    IndexingPageItem pageChild = new IndexingPageItem();

                    if (isValidPage(pageChild)
                            && IndexingServiceImpl.isValidAddress(pageChild)
                            && !pageChild.getPath().contains(".jpg")
                            && !pageChild.getPath().contains(".png")
                            && !pageChild.getPath().contains(".pdf"))
                    {

                        Connection.Response response =
                                Jsoup.connect(pageItem.getPath())
                                        .followRedirects(false)
                                        .execute();

                        pageChild.setPath(url);
                        pageChild.setSiteId(pageChild.getSiteId());
                        pageChild.setCode(response.statusCode());
                        pageChild.setContent(doc.html());

                        list.add(pageChild);
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

