package ca.mcgill.ecse223.block.application;

import ca.mcgill.ecse223.block.model.Block223;
import ca.mcgill.ecse223.block.model.Game;
import ca.mcgill.ecse223.block.model.PlayedGame;
import ca.mcgill.ecse223.block.model.UserRole;
import ca.mcgill.ecse223.block.persistence.Block223Persistence;
import ca.mcgill.ecse223.block.view.Block223Page;

public class Block223Application{

  private static Block223 block223;
  private static UserRole userRole;
  private static Game game;
  private static PlayedGame playedGame;
  public static void main(String[] args) {

    // start UI
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
          new Block223Page().setVisible(true);
        }
    });

  }

  public static Block223 getBlock223() {
    if (block223 == null) {
      // load model
      block223 = Block223Persistence.load();
    }
    return block223;
  }

  public static Block223 resetBlock223(){
    block223 = Block223Persistence.load();
    return block223;
  }
  public static void setBlock223(Block223 aBlock223) {
	  block223 = aBlock223;
  }
  public static void setCurrentUserRole(UserRole aUserRole){
    userRole = aUserRole;
  }

  public static UserRole getCurrentUserRole(){
    return userRole;
  }

  public static void setCurrentGame(Game aGame){
    game = aGame;
  }
  public static Game getCurrentGame(){
    return game;
  }

  public static void setCurrentPlayableGame(PlayedGame aPlayedGame){
    playedGame = aPlayedGame;
  }
  public static PlayedGame getCurrentPlayableGame(){
    return playedGame;
  }

}
