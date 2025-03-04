package org.example;

import org.example.entity.User;
import org.example.methods.SubscriptionDAO;
import org.example.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {
    public static void main(String[] args) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        if (session != null){
            System.out.println("Connection Established");
        }


        Transaction transaction = session.beginTransaction();
//
        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        subscriptionDAO.subscribe(14L, 13L);
//
//        // создание пользователя
//        User user = new User();
//        user.setLogin("nastya");
//        user.setPassword("12345");
//        user.setName("Настя");
//
//        session.save(user);
//

        // Создаем пост
//        Post post = new Post();
//        post.setContent("Всем ваолимлипривет!! Как дела?");
//        post.setAuthor(user);

//        session.save(post);

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