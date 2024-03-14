package searchengine.services.indexing.indexing_tools;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchengine.dto.indexing.SiteDto;
import searchengine.services.indexing.IndexingServiceImpl;

import java.util.*;


public class HtmlParser {

    private final static Set<String> validChildLinks = new HashSet<>();

    public static Set<String> parseHtmlPage(Document htmlPageBody) {

        Elements elementsPage = htmlPageBody.select("a");

        for (Element element : elementsPage) {

            String link = element.absUrl("href");

            if (isValidPage(link)) {
                validChildLinks.add(link);
                System.out.println("page is added");
            }
        }

        return validChildLinks;
    }

    private static boolean isValidPage(String pagePath) {

        String regexPath = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

        String domain = regexPath.replaceAll("http(s)?://|www\\.|/.*", "");

        List<SiteDto> searchedElem = IndexingServiceImpl.getSitesForValid().stream()
                .filter(site -> site.getUrl().contains(domain)).toList();

        return !searchedElem.isEmpty() &&
                pagePath.matches(regexPath) &&
                !pagePath.contains(".jpg") &&
                !pagePath.contains(".png") &&
                !pagePath.contains(".pdf") &&
                !pagePath.contains(".css");
    }
}

