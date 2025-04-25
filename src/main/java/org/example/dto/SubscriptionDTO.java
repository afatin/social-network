package org.example.dto;

public class SubscriptionDTO {
    private Long id;
    private Long subscriberId;
    private String subscriberName;
    private Long authorId;
    private String authorName;

    public SubscriptionDTO() {}

    public SubscriptionDTO(Long id, Long subscriberId, String subscriberName, Long authorId, String authorName) {
        this.id = id;
        this.subscriberId = subscriberId;
        this.subscriberName = subscriberName;
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSubscriberId() { return subscriberId; }
    public void setSubscriberId(Long subscriberId) { this.subscriberId = subscriberId; }

    public String getSubscriberName() { return subscriberName; }
    public void setSubscriberName(String subscriberName) { this.subscriberName = subscriberName; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
}
