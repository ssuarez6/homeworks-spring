package com.epam.homework2.dao;

import com.epam.homework2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Predicate;

@Repository
public class UserDao {

    private InMemoryStorage users;

    public UserDao(){}

    @Autowired
    public void setUsers(InMemoryStorage storage) {
        this.users = storage;
    }

    public void save(User user){
        users.getUsersMap().put(user.id(), user);
    }

    public User findById(Long id) {
        return users.getUsersMap().get(id);
    }

    public User findByEmail(String email) {
        var result = users.getUsersMap().values().stream().filter(user -> user.email().equals(email)).toList();
        if(result.isEmpty()) return null;
        else return result.get(0);
    }

    public List<User> getFilteredUsers(Predicate<User> predicate, int pageSize, int pageNum) {
        var filteredUsers = users.getUsersMap().values().stream().filter(predicate).toList();

        int fromIndex = (pageNum -1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, filteredUsers.size());

        return filteredUsers.subList(fromIndex, toIndex);
    }

    public boolean deleteUser(Long userId) {
        if(users.getUsersMap().containsKey(userId)) {
            users.getUsersMap().remove(userId);
            return true;
        } else return false;
    }
}
