/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contesting;

import GamePackage.GameManager;
import amazingsharedproject.Block;
import amazingsharedproject.Interfaces.IGame;
import amazingsharedproject.Interfaces.IGameManager;
import amazingsharedproject.Interfaces.ILogin;
import amazingsharedproject.Player;
import amazingsharedproject.User;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

/**
 *
 * @author Robin
 */
public class ConTest {
    
    private static final int port = 1099;
    private static Registry registry;
    private static String ip = "192.168.9.1";
    private static GameManager gmanager;
    
    public static void main(String[] args) throws RemoteException, SQLException {
        ILogin umanager= null;
        IGameManager gmanager= null;
        
        try {
            registry = LocateRegistry.getRegistry(ip, port);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
        
        
        try {
            umanager = (ILogin) registry.lookup("UserManager");
            gmanager = (IGameManager) registry.lookup("GameManager");
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        } catch(NotBoundException e) {
            System.out.println(e.getMessage());
        }
        //System.out.println(umanager);
        User testuser = umanager.Login("jeroen", "proftaak1");
        System.out.println("User found: " + testuser.getName() + " ID:" + testuser.getUserID());
        
        IGame game = gmanager.newLobby(testuser.getUserID());
        //System.out.println(game);
        System.out.println("Game retrieved. UserID:"+ testuser.getUserID() /*+ " -> playerid: " + game.getPlayer(testuser.getUserID()).getID()*/);
        Player p= game.getPlayer(testuser.getUserID(), "");
        System.out.println("Player retrieved. PlayerID: " + p.getID());
        
        //If you getplayer again with the same id it should return the same player:
        p = game.getPlayer(testuser.getUserID(), "");
        System.out.println("Retrieving again, PlayerID: " + p.getID());
        
        User nephovsep = umanager.Login("hovsep", "proftaak");
        System.out.println("User found: " + nephovsep.getName() + " ID:" + nephovsep.getUserID() + " attempting to join lobby..");
        IGame gamehovsep = gmanager.joinLobby(game.getGameID(), nephovsep.getUserID()); // IS THE SAME REFERENCE AS THE FIRST GAME!!
        System.out.println("GameLoby joined. ID: " + gamehovsep.getGameID());
        Player phovsep = gamehovsep.getPlayer(nephovsep.getUserID(), "");
        System.out.println("Player retrieved. PlayerID: " + phovsep.getID());
        
        //Testing the ready system
        System.out.println("Testing the ready system: ");
        game.setReady(p.getID(), true);
        game.setReady(p.getID(), false);
        
        //Start game (both ready, check server log)
        game.setReady(phovsep.getID(), true);
        game.setReady(p.getID(), true);
        
        System.out.println("Game started! (check server log)");
        
        Block[][] grid = game.getGrid();
        System.out.println(grid.length);
        //printMaze(grid);
        //gmanager.newLobby(-1);
    }
    
    private static void printMaze(Block[][] grid) //Copied fron Maze
    {
        int gridSize = grid.length;
        for(int y=0; y<gridSize; y++)
        {
            String line = "";
            for(int x=0; x<gridSize; x++)
            {
                if(grid[y][x] == Block.SOLID){
                    line += " \u25A0 ";
                }
                if(grid[y][x] == Block.OPEN){
                    line += " \u25A1 ";
                }
                if(grid[y][x] == Block.SPAWNPOINT){
                    line += " S ";
                }
                if(grid[y][x] == Block.EDGE) {
                    line += " X ";
                }
            }
            System.out.println(line);
        }
        
    }
}
