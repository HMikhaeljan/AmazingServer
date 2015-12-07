/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserPackage;

import Database.DatabaseConnection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Hovsep
 */
public class UserManager {

    DatabaseConnection db = new DatabaseConnection();
    List<User> users;
    User newuser;
    Stat stat;

    public void loadallUsers() throws SQLException{
        for (User user : db.getUsers()) {
            newuser = new User(user.getUserID(), user.getName(), user.getPassword(), user.getStats());
        }
        
    }
    
    //vraag alle online users op
    public List<User> getOnlineUsers() {
        return users;
    }

    /**
     * newUser Creates an account in the database.
     *
     * @param userid cannot not be null and must be unique.
     * @param name cannot not be null and must be unique.
     * @param password cannot not be null.
     * @param email cannot be null.
     * @param stats cannot be null.
     * @return true if succesfully added in database.
     */
    public void newUser(String username, String password, String email) {
        User user = new User(users.size() + 1, password, email, stat);
    }

    /**
     * Login method for verifying user. Checks in database if name and password
     * has correspondig account. if User exist then return user with
     * corresponding stats
     *
     * @param name
     * @param password
     * @return User if account exists otherwise return null.
     */
    public User Login(String username, String password) throws SQLException {

        for (User user : db.getUsers()) {
            if (user.getName().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

}
