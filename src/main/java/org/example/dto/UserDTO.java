package org.example.dto;

public class UserDTO {
    private Integer id;
    private String name;
    private String login;

    public UserDTO(Integer id, String name, String login) {
        this.id = id;
        this.name = name;
        this.login = login;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
}
