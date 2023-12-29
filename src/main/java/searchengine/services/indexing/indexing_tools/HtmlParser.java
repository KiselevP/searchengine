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
public class HtmlParser {
    private List<IndexingPageItem> list;

    private boolean isError;

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    private boolean isLink(IndexingPageItem pageItem) {
        String regex = "http[s]?://[^#, \\s]*\\.?[a-z]*\\.[a-z]{2,4}[^#,\\s]*";
        return !list.contains(pageItem) && pageItem.getPath().matches(regex);
    }

    public HtmlParser(IndexingPageItem pageItem) {
        synchronized (pageItem) {
            try {
                sleep(150);
                list = new ArrayList<>();
                Document doc = Jsoup.connect(pageItem.getPath())
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true).timeout(2000)
                        .followRedirects(false).get();

                Connection.Response response =
                        Jsoup.connect(pageItem.getPath())
                            .followRedirects(false)
                            .execute();
                pageItem.setCode(response.statusCode());
                pageItem.setContent(doc.html());

//                URL urlElem = new URL(pageItem.getUrl());
//                HttpURLConnection connection = (HttpURLConnection) urlElem.openConnection();
//                int respCod = connection.getResponseCode();

                Elements elements = doc.select("a");
                for (Element element : elements) {
                    String url = element.absUrl("href");
                    IndexingPageItem linkChild = new IndexingPageItem();
                    linkChild.setPath(url);
                    if (isLink(linkChild)
                            && IndexingServiceImpl.isValidAddress(linkChild.getPath())
                            && !linkChild.getPath().contains(".jpg")
                            && !linkChild.getPath().contains(".png")
                            && !linkChild.getPath().contains(".pdf"))
                    {
                        list.add(linkChild);
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

