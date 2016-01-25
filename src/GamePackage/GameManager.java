/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GamePackage;

import amazingsharedproject.Interfaces.IGame;
import amazingsharedproject.Interfaces.IGameManager;
import UserPackage.UserManager;
import amazingsharedproject.User;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author Hovsep
 */
public class GameManager extends UnicastRemoteObject implements IGameManager {

    private ArrayList<Game> activeGames;
    private ArrayList<String> lobbyChat;
    private UserManager umanager;

    public GameManager(UserManager umanager) throws RemoteException {
        this.activeGames = new ArrayList<Game>();
        this.umanager = umanager;
        lobbyChat = new ArrayList();

    }

    @Override
    public IGame newLobby(int userid) {
        User user = null;
        for (User u : umanager.getOnlineUsers()) {
            if (u.getUserID() == userid) {
                user = u;
                break;
            }
        }

        if (user == null) { //No logged in user
            System.out.println("User: " + userid + " not logged in!");
            return null;
        }

        Game game = null;
        try {
            game = new Game(activeGames.size());
            activeGames.add(game);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

        return (IGame) game;
    }

    @Override
    public IGame joinLobby(int gameid, int userid) {
        Game game = null;
        for (Game g : activeGames) {
            if (g.getGameID() == gameid) {
                game = g;
                break;
            }
        }

        if (game == null) {
            System.out.println("Game with id: " + gameid + " couldn't be found!");
        }

        return (IGame) game;
    }

    @Override
    public ArrayList<IGame> getGames() {
        ArrayList<IGame> igames = new ArrayList<IGame>();

        for (Game g : activeGames) {
            igames.add((IGame) g);
        }
        return igames;
    }

    @Override
    public void removeLobby(int gameid) throws RemoteException {
        Game g = null;

        for (Game a : activeGames) {
            if (a.getGameID() == gameid) {
                g = a;
            }
        }

        if (g == null) {
            return;
        }

        activeGames.remove(g);

    }

    @Override
    public ArrayList<String> loadChat() throws RemoteException {
        return lobbyChat;
    }

    @Override
    public void addToChat(String chat) throws RemoteException {
        lobbyChat.add(chat);
    }
}
