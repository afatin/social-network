package org.example.dto;

public class SubscriptionDTO {
    private Integer id;
    private Integer subscriberId;
    private String subscriberName;
    private Integer authorId;
    private String authorName;

    public SubscriptionDTO() {}

    public SubscriptionDTO(Integer id, Integer subscriberId, String subscriberName, Integer authorId, String authorName) {
        this.id = id;
        this.subscriberId = subscriberId;
        this.subscriberName = subscriberName;
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getSubscriberId() { return subscriberId; }
    public void setSubscriberId(Integer subscriberId) { this.subscriberId = subscriberId; }

    public String getSubscriberName() { return subscriberName; }
    public void setSubscriberName(String subscriberName) { this.subscriberName = subscriberName; }

    public Integer getAuthorId() { return authorId; }
    public void setAuthorId(Integer authorId) { this.authorId = authorId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
}
