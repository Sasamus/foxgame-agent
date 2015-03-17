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

	// TODO: Generate successors once --
	// possibly in the constructor, and store
	// them instead of making new ones every time.

	// TODO: Account for time

	// TODO: Get a move

	// TODO: Make sure Successors are generated properly --
	// and that all that can be are

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

		// Holds the Successors
		ArrayList<Board> successors = new ArrayList<Board>();

		// Acts depending on if playerRole equals FOX or SHEEP
		if (playerRole.equals("FOX")) {
			getFoxSuccessors(successors, false);
			getFoxSuccessors(successors, true);
		} else {
			getSheepSuccessors(successors);
		}
		
		Move move = Move.move(getBestSuccessor(successors, true).getChangedPositions());

		return move.toString();
	}
	
	/**
	 * Gets the best Successor
	 * 
	 * @param successors The Successors available
	 * @param fox True if a fox move, false if a sheep move
	 */
	private Board getBestSuccessor(ArrayList<Board> successors, boolean fox){
		
		// TODO: Implement this
		
		return successors.get(0);
	}

	/**
	 * get a list of Successors for a sheep move
	 * 
	 * @return An ArrayList holding Board objects representing the Successors
	 */
	private void getSheepSuccessors(ArrayList<Board> successors) {

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

			// Attempt to add new Positions based on horizontal and vertical
			// moves to newPositions
			newPositionRight(x, y, 1);
			newPositionUp(x, y, 1);
			newPositionLeft(x, y, 1);

			// Attempt to add new Positions based on diagonal moves, when they
			// can be made, to newPositions
			if (y == 2 || y == 6) {
				if (x == 4) {
					newPositionUpLeft(x, y, 1);
					newPositionUpRight(x, y, 1);
				}
			} else if (y == 3) {
				if (x == 3 || x == 5) {
					newPositionUpLeft(x, y, 1);
					newPositionUpRight(x, y, 1);
				}
			} else if (y == 4) {
				if (x == 2 || x == 4 || x == 6) {
					newPositionUpLeft(x, y, 1);
					newPositionUpRight(x, y, 1);
				}
			} else if (y == 5) {
				if (x == 1 || x == 3 || x == 5 || x == 7) {
					newPositionUpLeft(x, y, 1);
					newPositionUpRight(x, y, 1);
				}
			} else if (y == 7) {
				if (x == 3 || x == 5) {
					newPositionUpLeft(x, y, 1);
					newPositionUpRight(x, y, 1);
				}
			}

			// Iterate through newPositions
			for (Position newPosition : newPositions) {

				// Add a new Successor if the new Positions is empty
				if (isOccupied(newPosition) == 0) {

					successor
							.changePostition(sheepPosition, newPosition, false);
					successors.add(successor);
				}
			}
		}
	}

	/**
	 * Generates successors based on fox moves
	 * 
	 * @param successors
	 *            An ArrayList with Board objects
	 * @param jump
	 *            True if generating successors for jumps, else false
	 */
	private void getFoxSuccessors(ArrayList<Board> successors, boolean jump) {

		int step = 1;

		if (jump) {
			step = 2;
		}

		// Holds a successor
		Board successor;

		// Iterate through foxPositions
		for (Position foxPosition : board.getFoxPositions()) {

			// Set successor to match board
			successor = new Board(board);

			// Get the x and y values of foxPosition
			int x = foxPosition.getX();
			int y = foxPosition.getY();

			// Look for possible moves

			// Attempt to add new Positions based on horizontal and vertical
			// moves to newPositions
			tryAddAllHorizontalAndVerticalNewPositions(x, y, step);

			// Attempt to add new Positions based on diagonal moves, when they
			// can be made, to newPositions
			if (y == 1) {
				if (x == 3 || x == 5) {
					newPositionDownLeft(x, y, step);
					newPositionDownRight(x, y, step);
				}
			} else if (y == 2 || y == 6) {
				if (x == 4) {
					tryAddAllDiagonalNewPositions(x, y, step);
				}
			} else if (y == 3) {
				if (x == 1 || x == 3 || x == 5 || x == 7) {
					tryAddAllDiagonalNewPositions(x, y, step);
				}
			} else if (y == 4) {
				if (x == 2 || x == 4 || x == 6) {
					tryAddAllDiagonalNewPositions(x, y, step);
				}
			} else if (y == 5) {
				if (x == 1 || x == 3 || x == 5 || x == 7) {
					tryAddAllDiagonalNewPositions(x, y, step);
				}
			} else if (y == 7) {
				if (x == 3 || x == 5) {
					newPositionUpLeft(x, y, step);
					newPositionUpRight(x, y, step);
				}
			}
			if (!jump) {

				// Iterate through newPositions
				for (Position newPosition : newPositions) {

					// Add a new Successor if the new Positions is empty
					if (isOccupied(newPosition) == 0) {

						successor.changePostition(foxPosition, newPosition,
								false);
						successors.add(successor);
					}
				}
			} else {

				// Iterate through newPositions
				for (Position newPosition : newPositions) {

					// Add a new Successor if the new Positions is empty
					if (isOccupied(newPosition) == 0) {

						successor.changePostition(foxPosition, newPosition,
								true);
						successors.add(successor);

						// Call getFoxMoveSuccessors again, since a jump alows
						// for more jumps
						getFoxSuccessors(successors, true);
					}
				}
			}
		}
	}

	/**
	 * Check if an x, y position is on the board
	 * 
	 * @param x
	 *            The x value of the position
	 * @param y
	 *            The y value of the position
	 * @return true if the position x, y is on the board, else false
	 */
	private boolean isPositionValid(int x, int y) {

		if (y <= 2 || y >= 6) {
			if (x <= 2 || x >= 6) {
				return false;
			}
		} else {
			if (x < 1 || x > 7) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Calls all newPosition methods for diagonal moves
	 * 
	 * @param x
	 *            The x value of the current Position
	 * @param y
	 *            The y value of the current Position
	 * 
	 * @param step
	 *            The nr of steps to move in a direction
	 * 
	 */
	private void tryAddAllDiagonalNewPositions(int x, int y, int step) {
		newPositionUpLeft(x, y, step);
		newPositionUpRight(x, y, step);
		newPositionDownLeft(x, y, step);
		newPositionDownRight(x, y, step);
	}

	/**
	 * Calls all newPosition methods for horizontal and vertical moves
	 * 
	 * @param x
	 *            The x value of the current Position
	 * @param y
	 *            The y value of the current Position
	 * 
	 * @param step
	 *            The nr of steps to move in a direction
	 */
	private void tryAddAllHorizontalAndVerticalNewPositions(int x, int y,
			int step) {
		newPositionUp(x, y, step);
		newPositionDown(x, y, step);
		newPositionLeft(x, y, step);
		newPositionRight(x, y, step);
	}

	/**
	 * Creates and ads a new Position object to newPositions
	 * 
	 * @param x
	 *            The x value of the current Position
	 * @param y
	 *            The y value of the current Position
	 * @param jump
	 *            true if the move is a jump, else false
	 */
	private void newPositionUp(int x, int y, int step) {
		if (isPositionValid(x, y - step)) {
			if (step == 2) {
				if (isOccupied(new Position(x, y - 1)) == 1) {
					newPositions.add(new Position(x, y - step));
				}
			}
		}
	}

	/**
	 * Creates and ads a new Position object to newPositions
	 * 
	 * @param x
	 *            The x value of the current Position
	 * @param y
	 *            The y value of the current Position
	 * @param step
	 *            The nr of steps to move in a direction
	 */
	private void newPositionDown(int x, int y, int step) {
		if (isPositionValid(x, y + step)) {
			if (step == 2) {
				if (isOccupied(new Position(x, y + 1)) == 1) {
					newPositions.add(new Position(x, y + step));
				}
			}
		}
	}

	/**
	 * Creates and ads a new Position object to newPositions
	 * 
	 * @param x
	 *            The x value of the current Position
	 * @param y
	 *            The y value of the current Position
	 * @param step
	 *            The nr of steps to move in a direction
	 */
	private void newPositionLeft(int x, int y, int step) {
		if (isPositionValid(x - step, y)) {
			if (step == 2) {
				if (isOccupied(new Position(x - 1, y)) == 1) {
					newPositions.add(new Position(x - step, y));
				}
			}
		}
	}

	/**
	 * Creates and ads a new Position object to newPositions
	 * 
	 * @param x
	 *            The x value of the current Position
	 * @param y
	 *            The y value of the current Position
	 * @param step
	 *            The nr of steps to move in a direction
	 */
	private void newPositionRight(int x, int y, int step) {
		if (isPositionValid(x + step, y)) {
			if (step == 2) {
				if (isOccupied(new Position(x + 1, y)) == 1) {
					newPositions.add(new Position(x + step, y));
				}
			}
		}
	}

	/**
	 * Creates and ads a new Position object to newPositions
	 * 
	 * @param x
	 *            The x value of the current Position
	 * @param y
	 *            The y value of the current Position
	 * @param step
	 *            The nr of steps to move in a direction
	 */
	private void newPositionUpRight(int x, int y, int step) {
		if (isPositionValid(x + step, y - step)) {
			if (step == 2) {
				if (isOccupied(new Position(x, y - 1)) == 1) {
					newPositions.add(new Position(x + step, y - step));
				}
			}
		}
	}

	/**
	 * Creates and ads a new Position object to newPositions
	 * 
	 * @param x
	 *            The x value of the current Position
	 * @param y
	 *            The y value of the current Position
	 * @param step
	 *            The nr of steps to move in a direction
	 */
	private void newPositionDownRight(int x, int y, int step) {
		if (isPositionValid(x + step, y + step)) {
			if (step == 2) {
				if (isOccupied(new Position(x + 1, y + 1)) == 1) {
					newPositions.add(new Position(x + step, y + step));
				}
			}
		}
	}

	/**
	 * Creates and ads a new Position object to newPositions
	 * 
	 * @param x
	 *            The x value of the current Position
	 * @param y
	 *            The y value of the current Position
	 * @param step
	 *            The nr of steps to move in a direction
	 */
	private void newPositionUpLeft(int x, int y, int step) {
		if (isPositionValid(x - step, y - step)) {
			if (step == 2) {
				if (isOccupied(new Position(x - 1, y - 1)) == 1) {
					newPositions.add(new Position(x - step, y - step));
				}
			}
		}
	}

	/**
	 * Creates and ads a new Position object to newPositions
	 * 
	 * @param x
	 *            The x value of the current Position
	 * @param y
	 *            The y value of the current Position
	 * @param step
	 *            The nr of steps to move in a direction
	 */
	private void newPositionDownLeft(int x, int y, int step) {
		if (isPositionValid(x - 1, y + 1)) {
			newPositions.add(new Position(x - 1, y + 1));
		}
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
