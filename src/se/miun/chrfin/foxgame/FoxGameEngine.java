package se.miun.chrfin.foxgame;

import java.util.ArrayList;

import ae.foxgameagent.Board;
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
	
	// TODO: Generate successors once, possibly in the constructor, and store them instead of making new ones every time.

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
	 * Holds the Board
	 */
	private Board board = new Board();
	
	/**
	 * Constructor
	 * 
	 * @param setup
	 *            A PlayerSetuo object holding the setup information
	 */
	public FoxGameEngine(PlayerSetup setup) {

		// Set playerRole
		playerRole = setup.playerRole;

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

		} else {

		}

		return null;
	}

	/**
	 * get a list of Successors
	 * 
	 * @return An ArrayList with two ArrayLists holding the positions if the
	 *         foxes and sheep
	 */
	private ArrayList<ArrayList<Position>> getSuccessors() {
		
		// Holds the Successors
		ArrayList<ArrayList<Position>> successors = new ArrayList<ArrayList<Position>>();
		
		// Add the current positions to successors
		successors.add(foxPositions.);
		successors.add(sheepPositions);

		// Iterate through sheepPositions
		for (Position sheepPosition : sheepPositions) {

			// Get the x and y values of foxPosition
			int x = sheepPosition.x;
			int y = sheepPosition.y;

			// Look for possible horizontal moves
			
			// If the sheep is in the 6 top positions
			if(y == 1 || y == 2){
				
				// If the sheep is at the left edge of those positions 
				if(x == 3){
					Position position = new Position(x + 1, y);
					switch (isOccupied(position)) {
					case 0:
						successors.
						break;

					default:
						break;
					}
				}
			}

		}

		return null;
	}
	
	/**
	 * Check if a position is occupied, and by what
	 * 
	 * @param position The Position to check
	 * @return 0 if free, 1 if sheep, 2 if fox
	 */
	private int isOccupied(Position position){
		
		// Check if something is in position and return an int depending on what
		if(board.getFoxPositions().contains(position)){
			return 2;
		} else if (board.getSheepPositions().contains(position)){
			return 1;
		}
		return 0;
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
