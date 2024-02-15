package searchengine.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import searchengine.models.Site;

public interface SiteRepository extends JpaRepository<Site, Integer>
{
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE site", nativeQuery = true)
    void truncateTable();

    Site findSiteByName(String name);
    Site findSiteById(int id);
}
