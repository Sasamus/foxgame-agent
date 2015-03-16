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
	 * Holds the new Positions
	 */
	ArrayList<Position> newPositions = new ArrayList<Position>();

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
					newPositionRight(x, y);

					if (y == 2) {
						newPositionUp(x, y);
					}

				} else if (x == 5) {
					newPositionLeft(x, y);

					if (y == 2) {
						newPositionUp(x, y);
					}

				} else {
					newPositionRight(x, y);
					newPositionLeft(x, y);

					if (y == 2) {
						newPositionUp(x, y);
						newPositionUpLeft(x, y);
						newPositionUpRight(x, y);
					}

				}
			} else if (y >= 6) {

				// The sheep is in the bottom 6 positions

				if (x == 3) {
					newPositionRight(x, y);
					newPositionUp(x, y);

					if (y == 7) {
						newPositionUpRight(x, y);
					}

				} else if (x == 5) {
					newPositionLeft(x, y);
					newPositionUp(x, y);

					if (y == 7) {
						newPositionUpLeft(x, y);
					}

				} else {
					newPositionRight(x, y);
					newPositionLeft(x, y);
					newPositionUp(x, y);

					if (y == 6) {
						newPositionUpRight(x, y);
						newPositionUpLeft(x, y);
					}
				}
			} else {

				// The sheep is in the middle 21 positions

				// If the sheep can move upwards
				if (!(y == 3 && (x <= 2 || x >= 6))) {
					newPositionUp(x, y);
				}

				// Sideways moves
				if (x == 1) {
					newPositionRight(x, y);
				} else if (x == 7) {
					newPositionLeft(x, y);
				} else {
					newPositionRight(x, y);
					newPositionLeft(x, y);
				}

				// Diagonal moves
				if (y == 5) {
					if (x == 1) {
						newPositionUpRight(x, y);
					} else if (x == 7) {
						newPositionUpLeft(x, y);
					} else if (x == 3 || x == 5) {
						newPositionUpRight(x, y);
						newPositionUpLeft(x, y);
					}
				} else if (y == 4) {
					if (x == 2 || x == 4 || x == 6) {
						newPositionUpRight(x, y);
						newPositionUpLeft(x, y);
					}
				} else if (y == 3) {
					if (x == 3) {
						newPositionUpRight(x, y);
					} else if (x == 5) {
						newPositionUpLeft(x, y);
					}
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
	 * Creates and ads a new Position object to newPositions
	 * 
	 * @param x
	 *            The x value of the current Position
	 * @param y
	 *            The y value of the current Position
	 */
	private void newPositionUp(int x, int y) {
		newPositions.add(new Position(x, y - 1));
	}

	/**
	 * Creates and ads a new Position object to newPositions
	 * 
	 * @param x
	 *            The x value of the current Position
	 * @param y
	 *            The y value of the current Position
	 */
	private void newPositionLeft(int x, int y) {
		newPositions.add(new Position(x - 1, y));
	}

	/**
	 * Creates and ads a new Position object to newPositions
	 * 
	 * @param x
	 *            The x value of the current Position
	 * @param y
	 *            The y value of the current Position
	 */
	private void newPositionRight(int x, int y) {
		newPositions.add(new Position(x + 1, y));
	}

	/**
	 * Creates and ads a new Position object to newPositions
	 * 
	 * @param x
	 *            The x value of the current Position
	 * @param y
	 *            The y value of the current Position
	 */
	private void newPositionUpRight(int x, int y) {
		newPositions.add(new Position(x + 1, y - 1));
	}

	/**
	 * Creates and ads a new Position object to newPositions
	 * 
	 * @param x
	 *            The x value of the current Position
	 * @param y
	 *            The y value of the current Position
	 */
	private void newPositionUpLeft(int x, int y) {
		newPositions.add(new Position(x - 1, y - 1));
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
