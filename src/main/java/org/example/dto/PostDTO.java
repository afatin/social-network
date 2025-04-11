package org.example.dto;

public class PostDTO {
    private Integer id;
    private String content;
    private Integer authorId;
    private String authorName;

    public PostDTO(Integer id, String content, Integer authorId, String authorName) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getAuthorId() { return authorId; }
    public void setAuthorId(Integer authorId) { this.authorId = authorId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
}
