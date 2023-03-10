package edu.servidor.objects.Objects.repos;

import edu.servidor.objects.Objects.models.User;

import java.util.List;

public interface UserDao {
    int addUser(User user);

    int modifyName(String name, String username);

    int modifyPassword(String password, String username);

    List<User> getUsersByUsername(String username);

    int deleteUserByUserName(String username);
}
