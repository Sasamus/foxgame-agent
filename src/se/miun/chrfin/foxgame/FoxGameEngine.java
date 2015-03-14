package se.miun.chrfin.foxgame;

import se.miun.chrfin.foxgame.com.GameStatus;
import se.miun.chrfin.foxgame.setup.PlayerSetup;

/**
 * @author Christoffer Fink
 */
public class FoxGameEngine implements AiGameEngine {

  private final PlayerSetup setup;

  public FoxGameEngine(PlayerSetup setup) {
    this.setup = setup;
  }

  /**
   * Return a move of the form "x1,y1 x2,y2".
   */
  @Override
  public String getMove(GameStatus status) {
    return null; // Your code here.
  }

  @Override
  public void updateState(String move) {
    // Your code here.
  }

  @Override
  public String getPlayerName() {
    return setup.playerName;
  }
}
