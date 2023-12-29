package searchengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import searchengine.models.Site;

public interface SiteRepository extends JpaRepository<Site, Integer> {
    Site findSiteByName(String name);
}
