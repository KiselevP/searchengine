package searchengine.indexingengine;

import lombok.Data;

import java.util.List;

@Data
public class Link {
    private volatile String address;
    private volatile int level;
    private volatile List<Link> childList;
    private volatile String parentAddress;

    public Link(String address) {
        this.address = address;
    }
}
