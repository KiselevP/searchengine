package searchengine.models;

import javax.persistence.*;

@Entity
public class Lemma
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private Integer siteId;
    @Column(nullable = false)
    private String lemma;
    @Column(nullable = false)
    private Integer frequency;
}
