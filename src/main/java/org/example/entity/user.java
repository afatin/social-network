//package org.example.entity;
//
//public class user {
//}
//
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "username", nullable = false, unique = true)
//    private String username;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Order> orders;
//
//    // Геттеры, сеттеры, конструкторы
//}