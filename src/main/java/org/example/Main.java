package org.example;

import org.example.entity.Post;
import org.example.entity.User;
import org.example.entity.UserDAO;
import org.example.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {
    public static void main(String[] args) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        // создание пользователя
        User user = new User();
        user.setLogin("nastya");
        user.setPassword("pass2");
        user.setName("Настя");

        session.save(user);

        // Создаем пост
        Post post = new Post();
        post.setContent("Всем привет!!");
        post.setAuthor(user);

        session.save(post);

        //удаление пользователя
        //UserDAO userDAO = new UserDAO();
        //userDAO.deleteUserById(4L);

        transaction.commit();
        session.close();

        HibernateUtil.shutdown();
    }
}

//    DbFunctions db=new DbFunctions() ;
//        db.connect_to_db( "mydatabase", "myuser",
//                "mypassword");