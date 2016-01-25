/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserPackage;

import Database.DatabaseConnection;
import amazingsharedproject.Interfaces.ILogin;
import amazingsharedproject.Stat;
import amazingsharedproject.User;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Hovsep
 */
public class UserManager extends UnicastRemoteObject implements ILogin {

    DatabaseConnection db = new DatabaseConnection();
    List<User> onlineUsers = new ArrayList<User>();
    List<User> allUsers = new ArrayList<User>();
    List<User> tempList = new ArrayList<User>();

    User newuser;
    Stat stat;
    Timer timer = new Timer("Timer");

    long start = 0;
    long interval = 60000;

    public UserManager() throws RemoteException, SQLException {
        loadAllUsers();
        // timer.scheduleAtFixedRate(new reloadUsersFromDB(), start, interval);
    }

    
    //wordt nu niet gebruikt
    private class reloadUsersFromDB extends TimerTask {

        @Override
        public void run() {
            try {
                loadAllUsers();
            } catch (SQLException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void loadAllUsers() throws SQLException {
        tempList.clear();
        db = new DatabaseConnection();
        for (User user : db.getUsers()) {
            tempList.add(user);
        }
        allUsers = tempList;
    }

    @Override
    public List<User> getAllUsers() {
        return allUsers;
    }

    //vraag alle online users op
    @Override
    public synchronized List<User> getOnlineUsers() {
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
    @Override
    public void registerUser(String username, String password) {
        User user = new User(allUsers.size() + 1, username, password, stat);
        allUsers.add(user);
        try {
            db.newUser(username, password);
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void addToOnline(User user) {
        System.out.println("User added:" + user);
        onlineUsers.add(user);
    }

    @Override
    public synchronized void removeFromOnline(User user) {
        ArrayList<User> decoyUserList = new ArrayList(onlineUsers);

        if (!decoyUserList.isEmpty()) {
            for (User userOn : decoyUserList) {
                if (user.getUserID() == (userOn.getUserID())) {

                    onlineUsers.remove(userOn);
                    System.out.println("Online user removed:" + userOn.getUserID() + "." + userOn.getName());
                }
            }
        }
    }

    /**
     * Login method for verifying user. Checks in database if name and password
     * has correspondig account. if User exist then return user with
     * corresponding stats
     *
     * @param username
     * @param password
     * @return User if account exists otherwise return null.
     */
    @Override
    public User Login(String username, String password) {
        try {
            for (User user : db.getUsers()) {
                if (user.getName().equals(username) && user.getPassword().equals(password)) {

                    System.out.println("User " + user.getName() + " has logged in");

                    return user;
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
