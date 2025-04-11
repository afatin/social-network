package org.example.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.example.dto.DTOConverter;
import org.example.dto.PostDTO;
import org.example.entity.Post;
import org.example.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private DTOConverter dtoConverter;

    @Transactional
    public Post createPost(String content, Integer authorId) {
        User author = entityManager.find(User.class, authorId);
        Post Post = new Post();
        Post.setContent(content);
        Post.setAuthor(author);
        entityManager.persist(Post);
        return Post;
    }

    public List<PostDTO> getAllPosts() {
        String query = "SELECT c FROM Post c";
        List<Post> posts = entityManager.createQuery(query, Post.class).getResultList();
        return posts.stream()
                .map(dtoConverter::convertToPostDTO)
                .toList();
    }

    public List<Post> getPostById(Integer id) {
        String query = "SELECT c FROM Post c WHERE c.id = :id";
        return entityManager.createQuery(query, Post.class)
                .setParameter("id", id)
                .getResultList();
    }

    public List<PostDTO> getPostsByAuthorId(Integer authorId) {
        String query = "SELECT p FROM Post p WHERE p.author.id = :authorId";
        List<Post> posts = entityManager.createQuery(query, Post.class)
                .setParameter("authorId", authorId)
                .getResultList();

        return posts.stream()
                .map(dtoConverter::convertToPostDTO)
                .toList();
    }




}
