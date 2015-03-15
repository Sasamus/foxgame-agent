package se.miun.chrfin.foxgame;

import java.util.ArrayList;

import se.miun.chrfin.foxgame.com.GameStatus;
import se.miun.chrfin.foxgame.logic.Move;
import se.miun.chrfin.foxgame.logic.Position;
import se.miun.chrfin.foxgame.setup.PlayerSetup;
import ae.foxgameagent.Board;

/**
 * @author Christoffer Fink
 */
public class FoxGameEngine implements AiGameEngine {

	// TODO: See if there are faster variables/collections to use.

	// TODO: When are foxed removed exactly?

	// TODO: Generate successors once, possibly in the constructor, and store
	// them instead of making new ones every time.

	// TODO: Account for time

	// TODO: Get a move

	// TODO: Generate Successors

	// TODO: Make sure Successors are generated properly, and that all that can
	// be are

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
		ArrayList<Board> successors = new ArrayList<Board>();

		// Holds a successor
		Board successor;

		// Holds the new Positions
		ArrayList<Position> newPositions = new ArrayList<Position>();

		// Iterate through sheepPositions
		for (Position sheepPosition : board.getSheepPositions()) {

			// Set successor to match board
			successor = new Board(board);

			// Get the x and y values of foxPosition
			int x = sheepPosition.getX();
			int y = sheepPosition.getY();

			// Look for possible moves

			if (y <= 2) {

				// The sheep is in the top 6 positions

				// Try to add positions depending on where the current position
				// is
				if (x == 3) {

					// Add new Positions to newPositions
					newPositions.add(new Position(x + 1, y));

					if (y == 2) {
						newPositions.add(new Position(x, y - 1));
					}

				} else if (x == 5) {

					newPositions.add(new Position(x - 1, y));

					if (y == 2) {
						newPositions.add(new Position(x, y - 1));
					}

				} else {

					newPositions.add(new Position(x + 1, y));
					newPositions.add(new Position(x - 1, y));

					if (y == 2) {
						newPositions.add(new Position(x, y - 1));
						newPositions.add(new Position(x - 1, y - 1));
						newPositions.add(new Position(x + 1, y - 1));
					}

				}
			} else if (y >= 6) {

				// The sheep is in the bottom 6 positions

				if (x == 3) {

					newPositions.add(new Position(x + 1, y));
					newPositions.add(new Position(x, y - 1));

					if (y == 7) {
						newPositions.add(new Position(x + 1, y - 1));
					}

				} else if (x == 5) {

					newPositions.add(new Position(x - 1, y));
					newPositions.add(new Position(x, y - 1));

					if (y == 7) {
						newPositions.add(new Position(x - 1, y - 1));
					}

				} else {

					newPositions.add(new Position(x + 1, y));
					newPositions.add(new Position(x - 1, y));
					newPositions.add(new Position(x, y - 1));

					if (y == 6) {
						newPositions.add(new Position(x + 1, y - 1));
						newPositions.add(new Position(x - 1, y - 1));
					}
				}
			} else {

				// TODO: Generate successors for these positions too

				// The sheep is in the middle 21 positions

				if (x == 1) {

					newPositions.add(new Position(x + 1, y));

				} else if (x == 7) {

					newPositions.add(new Position(x - 1, y));

				} else {

					newPositions.add(new Position(x + 1, y));
					newPositions.add(new Position(x - 1, y));

				}
			}

			// Iterate through newPositions
			for (Position newPosition : newPositions) {

				// Add a new Successor if the new Positions is empty
				if (isOccupied(newPosition) == 0) {

					successor.changePostition(sheepPosition, newPosition);
					successors.add(successor);
				}
			}

		}

		return null;
	}

	/**
	 * Check if a position is occupied, and by what
	 * 
	 * @param position
	 *            The Position to check
	 * @return 0 if free, 1 if sheep, 2 if fox
	 */
	private int isOccupied(Position position) {

		// Check if something is in position and return an int depending on what
		if (board.getFoxPositions().contains(position)) {
			return 2;
		} else if (board.getSheepPositions().contains(position)) {
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
