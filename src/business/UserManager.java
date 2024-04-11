package business;

import core.Utility;
import dao.UserDao;
import entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private final UserDao userDao;

    public UserManager() {
        this.userDao = new UserDao();
    }

    public boolean save(User user) {
        if (this.getById(user.getUserId()) != null) {
            Utility.showMessage("error");
            return false;
        }
        return this.userDao.save(user);
    }

    public boolean update(User user) {
        if (this.getById(user.getUserId()) == null) {
            Utility.showMessage("Model with " + user.getUserId() + " could not be found.");
            return false;
        }
        return this.userDao.update(user);
    }

    public boolean delete(int id) {
        if (this.getById(id) == null) {
            Utility.showMessage("User with ID " + id + " could not be found.");
            return false;
        }
        return this.userDao.delete(id);
    }

    public User getById(int id) {
        return this.userDao.getById(id);
    }

    public User findByLogin(String username, String password) {
        return this.userDao.findByLogin(username, password);
    }

    public List<User> findAll() {
        return this.userDao.findAll();
    }

    public List<Object[]> getForTable(int size, List<User> userList) {
        List<Object[]> userObjList = new ArrayList<>();
        for (User obj : userList) {
            int i = 0;
            Object[] rowObject = new Object[size];
            rowObject[i++] = obj.getUserId();
            rowObject[i++] = obj.getUsername();
            rowObject[i++] = obj.getPassword();
            rowObject[i++] = obj.getRole();
            userObjList.add(rowObject);
        }
        return userObjList;
    }

    public List<User> searchForUsers(String role){
        String query = "SELECT * FROM public.users WHERE role = ";

        if(role != null){
            query += "'" + role + "'";
        }
        return this.userDao.selectByQuery(query);
    }

}
