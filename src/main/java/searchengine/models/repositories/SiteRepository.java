package searchengine.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import searchengine.models.Site;

public interface SiteRepository extends JpaRepository<Site, Integer>
{
    Site findSiteByName(String name);
    Site findSiteById(int id);

    @Modifying
    @Query(
            value = "SET FOREIGN_KEY_CHECKS = 0",
            nativeQuery = true
    )
    void setForeignKeyOnZero();
    @Modifying
    @Query(
            value = "TRUNCATE `site`",
            nativeQuery = true
    )
    void deleteAllElements();
    @Modifying
    @Query(
            value = "SET FOREIGN_KEY_CHECKS = 1",
            nativeQuery = true
    )
    void setForeignKeyOnOne();
}
