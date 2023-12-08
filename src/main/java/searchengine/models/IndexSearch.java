package searchengine.models;

import javax.persistence.*;

@Entity
public class IndexSearch
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private Integer pageId;
    @Column(nullable = false)
    private Integer lemmaId;
    @Column(nullable = false)
    private Float lemmaRank;
}
