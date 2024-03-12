package searchengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import searchengine.models.Site;

import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Long>, ModelRepository<Site> {
    Optional<Site> findSiteByName(String name);
}
