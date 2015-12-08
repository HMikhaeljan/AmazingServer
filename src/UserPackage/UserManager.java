/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserPackage;

import Database.DatabaseConnection;
import Interfaces.ILogin;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Hovsep
 */
public class UserManager extends UnicastRemoteObject implements ILogin {

    DatabaseConnection db = new DatabaseConnection();
    List<User> onlineUsers;
    List<User> allUsers;
    User newuser;
    Stat stat;


    public UserManager() throws RemoteException{
    }

    public void loadAllUsers() throws SQLException {
        for (User user : db.getUsers()) {
            newuser = new User(user.getUserID(), user.getName(), user.getPassword(), user.getStats());
            allUsers.add(newuser);
        }

    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    //vraag alle online users op

    public List<User> getOnlineUsers() {
        return onlineUsers;
    }

    /**
     * newUser Creates an account in the database.
     *
     * @param username
     * @param password cannot not be null.
     * @param stats cannot be null.
     * @return true if succesfully added in database.
     * @throws java.sql.SQLException
     */
    public void registerUser(String username, String password) {
        User user = new User(allUsers.size() + 1, username, password, stat);
        allUsers.add(user);
        try {
            db.newUser(username, password);
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Login method for verifying user. Checks in database if name and password
     * has correspondig account. if User exist then return user with
     * corresponding stats
     *
     * @param username
     * @param name
     * @param password
     * @return User if account exists otherwise return null.
     * @throws java.sql.SQLException
     */
    public User Login(String username, String password) {

        try {
            for (User user : db.getUsers()) {
                if (user.getName().equals(username) && user.getPassword().equals(password)) {
                    onlineUsers.add(user);
                    return user;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
