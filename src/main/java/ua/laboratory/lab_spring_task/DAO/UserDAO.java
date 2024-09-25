package ua.laboratory.lab_spring_task.DAO;

import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.User;

import java.util.List;

public interface UserDAO {
    User createOrUpdateUser(User user);
    User getUserById(Long id);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    void deleteUser(Long id);
    void deleteUser(String username);
}
