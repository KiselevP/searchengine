package searchengine.models;

import javax.persistence.*;
import java.time.*;

@Entity
public class Site
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private IndexingStatus indexingStatus;
    @Column(nullable = false)
    private LocalDateTime statusTime;
    @Column(columnDefinition = "TEXT")
    private String lastError;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String name;
}
