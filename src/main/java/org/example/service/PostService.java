package org.example.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.example.dto.DTOConverter;
import org.example.dto.PostDTO;
import org.example.dto.UserDTO;
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
    public Post createPost(String content, Long authorId) {
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

    public Post getPostById(Long id) {
        Post post = entityManager.find(Post.class, id);
        return post != null ? post : null;
    }

    public List<PostDTO> getPostsByAuthorId(Long authorId) {
        String query = "SELECT p FROM Post p WHERE p.author.id = :authorId ORDER BY p.id DESC";
        List<Post> posts = entityManager.createQuery(query, Post.class)
                .setParameter("authorId", authorId)
                .getResultList();

        return posts.stream()
                .map(dtoConverter::convertToPostDTO)
                .toList();
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = entityManager.find(Post.class, postId);
        if (post != null) {
            entityManager.remove(post);
        }
    }


}

