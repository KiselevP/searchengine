package searchengine.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Page
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private Integer siteId;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String path;
    @Column(nullable = false)
    private Integer code;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;
}
