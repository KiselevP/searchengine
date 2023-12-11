package searchengine.models;

import javax.persistence.*;

@Entity
public class Lemma
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "site_id", nullable = false)
    private Site siteId;
    @Column(nullable = false)
    private String lemma;
    @Column(nullable = false)
    private Integer frequency;
}
