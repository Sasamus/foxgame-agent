package se.miun.chrfin.foxgame;

import se.miun.chrfin.foxgame.com.GameStatus;
import se.miun.chrfin.foxgame.setup.PlayerSetup;

/**
 * @author Christoffer Fink
 */
public class FoxGameEngine implements AiGameEngine {

	/**
	 * Holds a PlayerSetup object
	 */
	private final PlayerSetup setup;
	
	/**
	 * Holds a String representing the players role
	 */
	private final String playerRole;

	/**
	 * Constructor
	 * 
	 * @param setup A PlayerSetuo object holding the setup information
	 */
	public FoxGameEngine(PlayerSetup setup) {
		
		// Set setup
		this.setup = setup;
		
		// Set playerRole
		playerRole = setup.playerRole;
	}

	/**
	 * Return a move of the form "x1,y1 x2,y2".
	 */
	@Override
	public String getMove(GameStatus status) {
		
		// Acts depending on if playerRole equals FOX or SHEEP
		if(playerRole.equals("FOX")){
			
		} else {
			
		}
		
		return null;
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
