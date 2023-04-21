package pl.coderslab;

import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        User user = new User(0, "adrian12053", "adrian@gmail.com", "abc123");
        UserDao userDao = new UserDao();
        System.out.println("Nowy user");

        //userDao.create(user)
        User user1 = new User(0, "test2", "test2@gmailcom", "test123");
        //userDao.update(user)
        //System.out.println("update user");

        System.out.println("Wszyscy: ");
        List<User>all = userDao.findAll();
        //System.out.println(all);
    }
}
