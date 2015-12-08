/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GamePackage;

import Interfaces.IGame;
import Interfaces.IPlayer;
import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import jdk.nashorn.internal.ir.Block;

/**
 *
 * @author Hovsep
 */
public class Game extends UnicastRemoteObject implements IGame {
    private int gameID;
    private List<Player> players;

    /**
     *
     * @param gameID
     * @param gameState
     */
    public Game(int gameID) throws RemoteException {
        this.gameID = gameID;
    }
    
    public int getGameID() {
        return gameID;
    }


    /**
     *
     * @return players
     */


    /**
     * Adds a player to the game
     *
     * @param player
     */
    public void addPlayer(Player player) {

        players.add(player);
    }

    /**
     * Check if the players in the game are ready to start. if all players are
     * ready return true else return false.
     *
     * @param players
     */
    public boolean checkReady(List<Player> players) {
        // TODO - implement Game.checkReady
        throw new UnsupportedOperationException();
    }

    @Override
    public Block[][] getGrid() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Player> getPlayers() throws RemoteException {
        return players;
    }
}
