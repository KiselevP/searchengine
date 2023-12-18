package searchengine.indexingengine;

import lombok.Getter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchengine.models.Site;
import searchengine.services.StatisticsServiceImpl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class HtmlParser {
    private List<Site> list;

    private boolean isError;

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    private boolean isLink(Site site) {
        String regex = "http[s]?://[^#, \\s]*\\.?[a-z]*\\.[a-z]{2,4}[^#,\\s]*";
        return !list.contains(site) && site.getUrl().matches(regex);
    }

    public HtmlParser(Site site) {
        synchronized (site) {
            try {
                sleep(150);
                list = new ArrayList<>();
                Document doc = Jsoup.connect(site.getUrl())
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true).timeout(2000)
                        .followRedirects(false).get();

                Connection.Response response =
                        Jsoup.connect(site.getUrl())
                            .followRedirects(false)
                            .execute();
                response.statusCode();

//                URL urlElem = new URL(site.getUrl());
//                HttpURLConnection connection = (HttpURLConnection) urlElem.openConnection();
//                int respCod = connection.getResponseCode();

                Elements elements = doc.select("a");
                for (Element element : elements) {
                    String url = element.absUrl("href");
                    Site linkChild = new Site();
                    linkChild.setUrl(url);
                    if (isLink(linkChild)
                            && StatisticsServiceImpl.isValidAddress(linkChild.getUrl())
                            && !linkChild.getUrl().contains(".jpg")
                            && !linkChild.getUrl().contains(".png")
                            && !linkChild.getUrl().contains(".pdf"))
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

