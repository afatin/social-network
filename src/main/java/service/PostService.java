package service;

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

    public List<Post> getPostByLogin(String login) {
        String query = "SELECT c FROM Post c WHERE c.login LIKE :login";
        return entityManager.createQuery(query, Post.class)
                .setParameter("login", "%" + login + "%")
                .getResultList();
    }



}
