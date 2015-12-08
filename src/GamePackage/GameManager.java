/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GamePackage;

import Interfaces.IGame;
import Interfaces.IGameManager;
import UserPackage.User;
import UserPackage.UserManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import jdk.nashorn.internal.ir.Block;

/**
 *
 * @author Hovsep
 */
public class GameManager implements IGameManager{
    
    private ArrayList<Game> activeGames;
    private UserManager umanager;
    
    public GameManager(UserManager umanager) {
        this.activeGames = new ArrayList<Game> ();
        this.umanager = umanager;
        
    }

    @Override
    public IGame newLobby(int userid) {
        User user = null;
        for(User u: umanager.getOnlineUsers()) {
            if(u.getUserID() == userid) {
                user = u;
                break;
            }
        }
        
        if(user == null) { //No logged in user
            System.out.println("User: " + userid + " not logged in!");
            return null;
        }
        
        Game game = null;
        try{
            game = new Game(activeGames.size());
        } catch(RemoteException e) {
            System.out.println(e.getMessage());
        }
        
        return (IGame) game;
    }

    public IGame joinLobby(int gameid, int userid) {
        Game game = null;
        for(Game g : activeGames) {
            if(g.getGameID() == gameid) {
                game = g;
                break;
            }
        }
        
        if(game == null) {
            System.out.println("Game with id: "+gameid+" couldn't be found!");
        }
        
        return (IGame) game;
    }
}
