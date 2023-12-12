package searchengine.models;

import javax.persistence.*;

@Entity
public class Page
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "site_id", unique = true, nullable = false)
    private Site siteId;
    @Column(name = "path", columnDefinition = "TEXT(255) NOT NULL, FULLTEXT KEY PATH_KEY (path)")
    private String path;
    @Column(nullable = false)
    private Integer code;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;
}
