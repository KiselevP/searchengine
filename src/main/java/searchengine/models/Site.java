package searchengine.models;

import javax.persistence.*;
import java.time.*;

@Entity
public class Site
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, columnDefinition = "ENUM('INDEXING', 'INDEXED', 'FAILED')")
    private IndexingStatus indexingStatus;
    @Column(nullable = false)
    private LocalDateTime statusTime;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String lastError;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String name;
}
