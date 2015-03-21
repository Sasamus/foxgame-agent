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

	// TODO: Look for ways to speed things up

	// TODO: Generate successors once --
	// possibly in the constructor, and store
	// them instead of making new ones every time.

	// TODO: Think while waiting for opponents move, possibly by using saved
	// successors

	// TODO: Remove unnecessary comments

	// TODO: Possibly separate this into more classes

	// TODO: Compilation performance flags

	// TODO: General clean up of code, it's rather messy

	// TODO: Playing sheep and a fox is removed --
	// not handled. Shouldn't cause errors but --
	// moving to that fox's position wouldn't be considered
	// Are we notified of removed foxes (and sheep), in getMove or uptadeState?

	// TODO: Check for unnecessary newing of objects

	// TODO: Make sure Successors are generated properly --
	// and that all that can be are

	// TODO: Fox may exede it's timeslice, almost certain that it's when the --
	// only moves it can make cause a deadlock

	// TODO: Better player name?
	/**
	 * Holds the player name
	 */
	private final String playerName = "alen1200";

	/**
	 * Holds the Board
	 */
	private Board board = new Board();

	/**
	 * Holds the previous board states
	 */
	private ArrayList<Board> previousStates = new ArrayList<Board>();

	/**
	 * Holds the time the agent must return a move by
	 */
	private long finnishBy;

	/**
	 * Constructor
	 * 
	 * @param setup
	 *            A PlayerSetuo object holding the setup information
	 */
	public FoxGameEngine(PlayerSetup setup) {

	}

	/**
	 * Return a move of the form "x1,y1 x2,y2".
	 */
	@Override
	public String getMove(GameStatus status) {

		// Set finnishBy
		finnishBy = System.currentTimeMillis() + status.timeSlice;

		// Holds the Successors
		ArrayList<Board> successors = new ArrayList<Board>();

		// Holds the best Successor
		Board bestSuccessor;

		// Acts depending on if status.playerRole equals FOX or SHEEP
		if (status.playerRole.equals("FOX")) {

			// System.out.println("Successors before: " + successors.size());

			// System.out.println("Foxes: " + board.getFoxPositions().size());

			// Get the fox Successors
			getFoxSuccessors(successors, new Board(board), null, null, false);

			// System.out.println("Successors middle: " + successors.size());

			getFoxSuccessors(successors, new Board(board), null, null, true);

			// System.out.println("Successors after: " + successors.size());

			// Holds foxes to remove from the board
			ArrayList<Position> toRemove = new ArrayList<Position>();

			// Iterate through boards foxPositions
			for (Position tmpPosition : board.getFoxPositions()) {

				// Holds of tmpPosition can move
				boolean canMove = false;

				// Iterate through successors, if tmpPosition moves in one of
				// them
				// set canMove to true
				for (Board tmpBoard : successors) {
					if (tmpBoard.getChangedPositions().contains(tmpPosition)) {
						canMove = true;
					}
				}

				// If tmpPosition can't move, set it to be removed from the
				// board
				if (!canMove) {

					// Add tmpPosition to toRemove
					toRemove.add(tmpPosition);
				}
			}

			// Remove the fox(es) in toRemove from board
			for (Position tmpPosition : toRemove) {
				board.removeFox(tmpPosition);
			}

			// Get the best successor
			bestSuccessor = getBestSuccessor(successors, true);

		} else {

			// Get the sheep Successors
			getSheepSuccessors(successors, new Board(board));

			// Get the best Successor
			bestSuccessor = getBestSuccessor(successors, false);
		}

		// Gets the move to create bestSuccessor
		Move move = Move.move(bestSuccessor.getChangedPositions());

		// Return the string version of move
		return move.toString();
	}

	/**
	 * Gets the best Successor
	 * 
	 * @param successors
	 *            The Successors available
	 * @param fox
	 *            True if a fox move, false if a sheep move
	 */
	public Board getBestSuccessor(ArrayList<Board> successors, boolean fox) {

		// TODO: This should be able to handle being a sheep

		// Holds the best Successor
		Board bestSuccessor = null;

		// Holds the depth to search to
		int depth = 10;

		if (fox) {

			// Holds the best value
			double bestValue = Double.NEGATIVE_INFINITY;

			// Iterate through successors
			for (Board tmpSuccessor : successors) {

				// Get alpha
				double alpha = minMax(tmpSuccessor, depth, bestValue,
						Double.POSITIVE_INFINITY, false);

				// Check if alpha is better than betsValue or that bestSuccessor
				// is null
				if (alpha > bestValue || bestSuccessor == null) {

					// Set bestSuccessor to tmpSuccessor
					bestSuccessor = tmpSuccessor;

					// Set bestValue to alpha
					bestValue = alpha;
				}
			}
		} else {

			// Holds the best value
			double bestValue = Double.POSITIVE_INFINITY;

			// Iterate through successors
			for (Board tmpSuccessor : successors) {

				// Get beta
				double beta = minMax(tmpSuccessor, depth,
						Double.NEGATIVE_INFINITY, bestValue, true);

				// Check if beta is better than betsValue or that bestSuccessor
				// is null
				if (beta < bestValue || bestSuccessor == null) {

					// Set bestSuccessor to tmpSuccessor
					bestSuccessor = tmpSuccessor;

					// Set bestValue to beta
					bestValue = beta;
				}
			}
		}

		// Return bestSuccessor
		return bestSuccessor;
	}

	/**
	 * Returns the best utility value
	 * 
	 * @param node
	 *            The node to originate from
	 * @param alpha
	 *            The current best Max value
	 * @param beta
	 *            The current best Min value
	 * @param max
	 *            True if looking for Max else false
	 * @return The utility value of the best Successor
	 */
	double minMax(Board node, int depth, double alpha, double beta, boolean max) {

		// Check if nodes state is terminal or at max depth
		if (node.isTerminal() == 1 || node.isTerminal() == 2 || depth == 0
				|| (finnishBy - System.currentTimeMillis()) < 200) {

			// System.out.println("Utility: " + node.getUtility());

			// If so, return it's utility
			return node.getUtility();
		}

		// Check if max is true
		if (max) {

			// Create and set newAlpha
			double newAlpha = Double.NEGATIVE_INFINITY;

			// Holds the Successors
			ArrayList<Board> successors = new ArrayList<Board>();

			// Get the Fox Successors
			getFoxSuccessors(successors, new Board(node), null, null, false);
			getFoxSuccessors(successors, new Board(node), null, null, true);

			// Iterate through successors
			for (Board tmpSuccessor : successors) {

				// Set newAlpha with the max of newAlpha and minMax with false
				newAlpha = Math.max(newAlpha,
						minMax(tmpSuccessor, depth - 1, alpha, beta, false));

				// Set alpha with the max of newAlpha and alpha
				alpha = Math.max(newAlpha, alpha);

				// Check if alpha is better than beta
				if (alpha >= beta) {

					// Return alpha
					return alpha;
				}
			}

			// Return newAlpha
			return newAlpha;

		} else {

			// Create and set newBeta
			double newBeta = Double.POSITIVE_INFINITY;

			// Holds the Successors
			ArrayList<Board> successors = new ArrayList<Board>();

			// Get the Sheep Successors
			getSheepSuccessors(successors, new Board(node));

			// Iterate through successors
			for (Board tmpSuccessor : successors) {

				// Set newBeta with the min of newBeta and minMax with true
				newBeta = Math.min(newBeta,
						minMax(tmpSuccessor, depth - 1, alpha, beta, true));

				// Set beta with the min of newBeta and beta
				beta = Math.min(newBeta, beta);

				// Check if alpha is better than beta
				if (alpha >= beta) {

					// Return beta
					return beta;
				}
			}

			// Return newBeta
			return newBeta;
		}
	}

	/**
	 * get a list of Successors for a sheep move
	 * 
	 * @param successors
	 *            The List to add Successors to
	 * 
	 * @param node
	 *            The node to get Successors from
	 * 
	 * @return An ArrayList holding Board objects representing the Successors
	 */
	private void getSheepSuccessors(ArrayList<Board> successors, Board node) {

		// Holds a successor
		Board successor;

		// Holds the new Positions
		ArrayList<Position> newPositions = new ArrayList<Position>();

		// Iterate through sheepPositions
		for (Position sheepPosition : node.getSheepPositions()) {

			// Set successor to node
			successor = new Board(node);

			// Clear newPositions
			newPositions.clear();

			// Clear changedPositions
			successor.clearChangedPositions();

			// Get the x and y values of foxPosition
			int x = sheepPosition.getX();
			int y = sheepPosition.getY();

			// Attempt to add new Positions based on horizontal and vertical
			// moves to newPositions
			tryAddAllHorizontalAndVerticalNewPositions(successor, newPositions,
					x, y, 1, true);

			// Attempt to add new Positions based on diagonal moves, when
			// they can be made, to newPositions
			if (y == 2 || y == 6) {
				if (x == 4) {
					tryAddAllDiagonalNewPositions(successor, newPositions, x,
							y, 1, true);
				}
			} else if (y == 3) {
				if (x == 3 || x == 5) {
					tryAddAllDiagonalNewPositions(successor, newPositions, x,
							y, 1, true);
				}
			} else if (y == 4) {
				if (x == 2 || x == 4 || x == 6) {
					tryAddAllDiagonalNewPositions(successor, newPositions, x,
							y, 1, true);
				}
			} else if (y == 5) {
				if (x == 1 || x == 3 || x == 5 || x == 7) {
					tryAddAllDiagonalNewPositions(successor, newPositions, x,
							y, 1, true);
				}
			} else if (y == 7) {
				if (x == 3 || x == 5) {
					tryAddAllDiagonalNewPositions(successor, newPositions, x,
							y, 1, true);
				}
			}

			// Holds a temporary version of successor
			Board tmpSuccessor;

			// Iterate through newPositions
			for (Position newPosition : newPositions) {

				// Set tmpSuccessor to match successor
				tmpSuccessor = new Board(successor);

				// Make the move
				tmpSuccessor.changePostition(new Position(sheepPosition),
						new Position(newPosition), false);

				// Add successor to successors, if it doesn't cause a deadlock
				if (!previousStates.contains(tmpSuccessor)) {
					successors.add(tmpSuccessor);
				}
			}
		}
	}

	/**
	 * Generate Successors based on fox moves
	 * 
	 * @param successors
	 *            The List to add Successors to
	 * @param node
	 *            The node to get Successors from
	 * @param alreadyJumpedBoard
	 *            The board state if one or more jumps have already happened
	 * @param foxToJump
	 *            The fox that are allowed to jump again
	 * @param jump
	 *            If jumps are happening
	 */
	private void getFoxSuccessors(ArrayList<Board> successors, Board node,
			Board alreadyJumpedBoard, Position foxToJump, boolean jump) {

		// System.out.println("New call----------------------------");

		// The nr of steps to move
		int step = 1;

		// Set step to 2 if jump is true
		if (jump) {
			step = 2;
		}

		// Holds the new Positions
		ArrayList<Position> newPositions = new ArrayList<Position>();

		// Holds a successor
		Board successor;

		// Holds the fox(es) that are allowed move
		ArrayList<Position> foxesToMove = new ArrayList<Position>();

		// Acts depending on if jumps have happened
		if (alreadyJumpedBoard == null) {
			// Set successor to node
			successor = new Board(node);

			// Clear changedPositions
			successor.clearChangedPositions();

			// Set foxesToJump to successors foxPositions
			foxesToMove = successor.getFoxPositions();
		} else {
			// Set successor to match alreadyJumpedBoard
			successor = alreadyJumpedBoard;

			// Set foxesToJump to foxToJump
			foxesToMove.add(foxToJump);
		}

		// Iterate through foxPositions
		for (Position foxPosition : foxesToMove) {

			// Clear newPositions
			newPositions.clear();

			// Acts depending on if jumps have happened
			if (alreadyJumpedBoard == null) {
				// Set successor to node
				successor = new Board(node);

				// Clear changedPositions
				successor.clearChangedPositions();
			} else {
				// Set successor to match alreadyJumpedBoard
				successor = alreadyJumpedBoard;
			}

			// Get the x and y values of foxPosition
			int x = foxPosition.getX();
			int y = foxPosition.getY();

			// Attempt to add new Positions based on horizontal and vertical
			// moves to newPositions
			tryAddAllHorizontalAndVerticalNewPositions(successor, newPositions,
					x, y, step, false);

			// Attempt to add new Positions based on diagonal moves, when they
			// can be made, to newPositions

			if (y == 1) {
				if (x == 3 || x == 5) {
					newPosition(successor, newPositions, x, y, step, "downleft");
					newPosition(successor, newPositions, x, y, step,
							"downright");
				}
			} else if (y == 2 || y == 6) {
				if (x == 4) {
					tryAddAllDiagonalNewPositions(successor, newPositions, x,
							y, step, false);
				}
			} else if (y == 3) {
				if (x == 1 || x == 3 || x == 5 || x == 7) {
					tryAddAllDiagonalNewPositions(successor, newPositions, x,
							y, step, false);
				}
			} else if (y == 4) {
				if (x == 2 || x == 4 || x == 6) {
					tryAddAllDiagonalNewPositions(successor, newPositions, x,
							y, step, false);
				}
			} else if (y == 5) {
				if (x == 1 || x == 3 || x == 5 || x == 7) {
					tryAddAllDiagonalNewPositions(successor, newPositions, x,
							y, step, false);
				}
			} else if (y == 7) {
				if (x == 3 || x == 5) {
					newPosition(successor, newPositions, x, y, step, "upleft");
					newPosition(successor, newPositions, x, y, step, "downleft");
				}
			}

			// Holds a temporary version of successor
			Board tmpSuccessor;

			// Act depending on if jumping or not
			if (!jump) {

				// Iterate through newPositions
				for (Position newPosition : newPositions) {

					// Set tmpSuccessor to match successor
					tmpSuccessor = new Board(successor);

					// Apply the move
					tmpSuccessor.changePostition(new Position(foxPosition),
							new Position(newPosition), false);

					// Add tmpSuccessor to successors, if it doesn't cause a
					// deadlock
					if (!previousStates.contains(tmpSuccessor)) {
						successors.add(tmpSuccessor);
					}

				}
			} else {

				// System.out.println("Positions: " + newPositions.size());

				// Iterate through newPositions
				for (Position newPosition : newPositions) {

					// Set tmpSuccessor to match successor
					tmpSuccessor = new Board(successor);

					// System.out.println("The new position: " + newPosition.x
					// + ":" + newPosition.y);
					//
					// System.out.println("Before: "
					// + tmpSuccessor.getSheepPositions().size());

					// Apply the move
					tmpSuccessor.changePostition(new Position(foxPosition),
							new Position(newPosition), true);

					// System.out.println("After: "
					// + tmpSuccessor.getSheepPositions().size());

					// Add tmpSuccessor to successors, if it doesn't cause a
					// deadlock
					if (!previousStates.contains(tmpSuccessor)) {
						successors.add(tmpSuccessor);
					}

					// Call getFoxMoveSuccessors again, since several jumps
					// are allowed
					getFoxSuccessors(successors, null, new Board(tmpSuccessor),
							new Position(newPosition.x, newPosition.y), true);
				}
			}
		}
	}

	/**
	 * Calls all newPosition methods for diagonal moves
	 * 
	 * @param theBoard
	 *            The Board to work with
	 * @param newPositions
	 *            The ArrayList to add new Positions to
	 * @param x
	 *            The x value of the current Position
	 * @param y
	 *            The y value of the current Position
	 * 
	 * @param step
	 *            The nr of steps to move in a direction
	 * 
	 */
	private void tryAddAllDiagonalNewPositions(Board theBoard,
			ArrayList<Position> newPositions, int x, int y, int step,
			boolean onlyUpwards) {

		newPosition(theBoard, newPositions, x, y, step, "upleft");
		newPosition(theBoard, newPositions, x, y, step, "upright");

		if (!onlyUpwards) {
			newPosition(theBoard, newPositions, x, y, step, "downleft");
			newPosition(theBoard, newPositions, x, y, step, "downright");
		}

	}

	/**
	 * Calls all newPosition methods for horizontal and vertical moves
	 * 
	 * @param theBoard
	 *            The Board to work with
	 * @param newPositions
	 *            The ArrayList to add new Positions to
	 * @param x
	 *            The x value of the current Position
	 * @param y
	 *            The y value of the current Position
	 * 
	 * @param step
	 *            The nr of steps to move in a direction
	 */
	private void tryAddAllHorizontalAndVerticalNewPositions(Board theBoard,
			ArrayList<Position> newPositions, int x, int y, int step,
			boolean onlyUpwards) {

		newPosition(theBoard, newPositions, x, y, step, "up");

		if (!onlyUpwards) {
			newPosition(theBoard, newPositions, x, y, step, "down");
		}

		newPosition(theBoard, newPositions, x, y, step, "left");
		newPosition(theBoard, newPositions, x, y, step, "right");
	}

	/**
	 * Adds a new Position if it's valid
	 * 
	 * @param theBoard
	 *            The Board to work with
	 * @param newPositions
	 *            The ArrayList to add new Positions to
	 * @param x
	 *            The current x value
	 * @param y
	 *            The current y value
	 * @param step
	 *            The steps to move
	 * @param move
	 *            The move to make
	 */
	private void newPosition(Board theBoard, ArrayList<Position> newPositions,
			int x, int y, int step, String move) {

		int xOffset = 0;
		int yOffset = 0;

		// Change the offsets to match move
		if (move.equals("up")) {
			yOffset = -step;
		} else if (move.equals("down")) {
			yOffset = +step;
		} else if (move.equals("left")) {
			xOffset = -step;
		} else if (move.equals("right")) {
			xOffset = +step;
		} else if (move.equals("upleft")) {
			xOffset = -step;
			yOffset = -step;
		} else if (move.equals("upright")) {
			xOffset = +step;
			yOffset = -step;
		} else if (move.equals("downleft")) {
			xOffset = -step;
			yOffset = +step;
		} else if (move.equals("downright")) {
			xOffset = +step;
			yOffset = +step;
		}

		if (board.isPositionValid(x + xOffset, y + yOffset)
				&& (theBoard.isOccupied(new Position(x + xOffset, y + yOffset)) == 0)) {
			if (step == 2) {

				// System.out.println("isOccupied Org: " + x + ":" + y + ":"
				// + isOccupied(theBoard, new Position(x, y)));
				// System.out.println("isOccupied Middle: "
				// + (x + (xOffset / 2))
				// + ":"
				// + (y + (yOffset / 2))
				// + ":"
				// + isOccupied(theBoard, new Position(x + (xOffset / 2),
				// y + (yOffset / 2))));
				// System.out.println("isOccupied New: "
				// + (x + xOffset)
				// + ":"
				// + (y + yOffset)
				// + ":"
				// + isOccupied(theBoard, new Position(x + xOffset, y
				// + yOffset)));

				if (theBoard.isOccupied(new Position(x + (xOffset / 2), y
						+ (yOffset / 2))) == 1) {
					newPositions.add(new Position(x + xOffset, y + yOffset));
				}
			} else {
				newPositions.add(new Position(x + xOffset, y + yOffset));
			}
		}
	}

	@Override
	public void updateState(String move) {

		// System.out.println("Move made: " + move);

		// Convert move to a char Array
		char[] moveArray = move.toCharArray();

		// Holds the x and y values
		int startX = 0;
		int stopX = 0;
		int startY = 0;
		int stopY = 0;

		// Iterate through the moves
		for (int i = 0; i < moveArray.length; i = i + 4) {
			if (i == 0) {
				stopX = Character.getNumericValue(moveArray[i]);
				stopY = Character.getNumericValue(moveArray[i + 2]);
			} else {
				startX = stopX;
				startY = stopY;

				stopX = Character.getNumericValue(moveArray[i]);
				stopY = Character.getNumericValue(moveArray[i + 2]);

				// Add board to previousStates
				previousStates.add(new Board(board));

				// Get the difference between the x values and the y values
				int xDiff = Math.abs(startX - stopX);
				int yDiff = Math.abs(startY - stopY);

				// Was a jump made
				if (xDiff > 1 || yDiff > 1) {

					// It was

					// Update board, jumo
					board.updateBoard(new Position(startX, startY),
							new Position(stopX, stopY), true);

				} else {

					// It was not

					// Update board, not jump
					board.updateBoard(new Position(startX, startY),
							new Position(stopX, stopY), false);

				}
			}
		}
	}

	@Override
	public String getPlayerName() {
		return playerName;
	}
}
