package org.example.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.example.entity.Post;
import org.example.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Post createPost(String content, User author) {
        Post Post = new Post();
        Post.setContent(content);
        Post.setAuthor(author);
        entityManager.persist(Post);
        return Post;
    }

    public List<Post> getAllPost() {
        String query = "SELECT c FROM Post c";
        return entityManager.createQuery(query, Post.class).getResultList();
    }

    public List<Post> getPostById(Integer id) {
        String query = "SELECT c FROM Post c WHERE c.id = :id";
        return entityManager.createQuery(query, Post.class)
                .setParameter("id", id)
                .getResultList();
    }

    public List<Post> getPostsByAuthorId(Integer authorId) {
        String query = "SELECT p FROM Post p WHERE p.author.id = :authorId";
        return entityManager.createQuery(query, Post.class)
                .setParameter("authorId", authorId)
                .getResultList();
    }

}
