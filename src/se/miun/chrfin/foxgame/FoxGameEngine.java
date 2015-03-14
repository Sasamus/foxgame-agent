package se.miun.chrfin.foxgame;

import java.util.ArrayList;

import se.miun.chrfin.foxgame.com.GameStatus;
import se.miun.chrfin.foxgame.logic.Move;
import se.miun.chrfin.foxgame.logic.Position;
import se.miun.chrfin.foxgame.setup.PlayerSetup;

/**
 * @author Christoffer Fink
 */
public class FoxGameEngine implements AiGameEngine {
	
	// TODO: See if there are faster variables/collections to use.
	
	// TODO: When are foxed removed exactly?

	// TODO: Better player name?
	/**
	 * Holds the player name
	 */
	private final String playerName = "alen1200";

	/**
	 * Holds a String representing the players role
	 */
	private final String playerRole;
	
	/**
	 * Holds the positions of the foxes
	 */
	private ArrayList<Position> foxPositions = new ArrayList<Position>();
	
	/**
	 * Holds the positions of the sheep
	 */
	private ArrayList<Position> sheepPositions = new ArrayList<Position>();

	/**
	 * Constructor
	 * 
	 * @param setup
	 *            A PlayerSetuo object holding the setup information
	 */
	public FoxGameEngine(PlayerSetup setup) {

		// Set playerRole
		playerRole = setup.playerRole;
		
		// Set the starting positions of the foxes
		foxPositions.add(new Position(3, 1));
		foxPositions.add(new Position(5, 1));
		
		// Set the starting positions of the sheep
		for(int y = 4; y <= 5; y++){
			for(int x = 1; x <= 7; x++){	
				sheepPositions.add(new Position(x, y));
			}
		}
		for(int y = 6; y <= 7; y++){
			for(int x = 3; x <= 5; x++){	
				sheepPositions.add(new Position(x, y));
			}
		}
	}

	/**
	 * Return a move of the form "x1,y1 x2,y2".
	 */
	@Override
	public String getMove(GameStatus status) {
		
		// Holds the possible moves to make
		ArrayList<Move> possibleMoves = new ArrayList<Move>();

		// Acts depending on if playerRole equals FOX or SHEEP
		if (playerRole.equals("FOX")) {
			
			for(Position foxPosition : foxPositions){
				
				// Check the horizontal moves
				
				int x = foxPosition.x;
				int y = foxPosition.y;
				
				
			}
			
		
			

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
		return playerName;
	}
}
