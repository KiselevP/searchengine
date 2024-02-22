package searchengine.models.repositories_tools;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import searchengine.models.IndexSearch;
import searchengine.models.Lemma;
import searchengine.models.Page;
import searchengine.models.Site;
import searchengine.models.repositories.IndexSearchRepository;
import searchengine.models.repositories.LemmaRepository;
import searchengine.models.repositories.PageRepository;
import searchengine.models.repositories.SiteRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RepositoriesService
{
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final LemmaRepository lemmaRepository;
    private final IndexSearchRepository indexSearchRepository;

    public void addItem(Site site){}
    public void addItem(Page page){}
    public void addItem(Lemma lemma){}
    public void addItem(IndexSearch indexSearch){}

    public void updateItem (){}

    public void findItem(){}

    @Transactional
    public void clearRepositories(String nameRepository) {
        if (Objects.equals(nameRepository, "page"))
        {
            siteRepository.setForeignKeyOnZero();
            siteRepository.deleteAllElements();
            siteRepository.setForeignKeyOnOne();
        }
        else if (Objects.equals(nameRepository, "site"))
        {
            pageRepository.setForeignKeyOnZero();
            pageRepository.deleteAllElements();
            pageRepository.setForeignKeyOnOne();
        }
        else if (Objects.equals(nameRepository, "lemma"))
        {
            lemmaRepository.setForeignKeyOnZero();
            lemmaRepository.deleteAllElements();
            lemmaRepository.setForeignKeyOnOne();
        }
        else if (Objects.equals(nameRepository, "index_search"))
        {
            indexSearchRepository.setForeignKeyOnZero();
            indexSearchRepository.deleteAllElements();
            indexSearchRepository.setForeignKeyOnOne();
        }
    }
}