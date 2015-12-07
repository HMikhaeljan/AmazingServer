/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GamePackage;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Hovsep
 */
public class GameState implements Serializable{
    List<Player> player;
    List<Used> abilities;
    List<String> messages;
    
    
    
    public GameState(List<Player> player, List<Used> abilities, List<String> messages ){
        this.player = player;
        this.abilities = abilities;
        this.messages = messages;
    }
}
