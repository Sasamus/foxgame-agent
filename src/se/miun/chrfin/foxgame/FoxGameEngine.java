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

	// TODO: Possibly separate this into more classes

	// TODO: General clean up of code, it's rather messy

	// TODO: Implement minimax, using the getUtility method

	// TODO: minimax depth limit, bigger if time

	// TODO: Account for time
	
	// TODO: Check for unnecessary newing of objects

	// TODO: Get a move

	// TODO: Make sure Successors are generated properly --
	// and that all that can be are

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

		// Holds the Successors
		ArrayList<Board> successors = new ArrayList<Board>();

		// Holds foxes that have moves they can do
		ArrayList<Position> foxesThatHaveMoves = new ArrayList<Position>();

		// Holds the best Successor
		Board bestSuccessor;

		// Acts depending on if status.playerRole equals FOX or SHEEP
		if (status.playerRole.equals("FOX")) {
			getFoxSuccessors(successors, new Board(board), null,
					foxesThatHaveMoves, null, false);
			getFoxSuccessors(successors, new Board(board), null,
					foxesThatHaveMoves, null, true);
			
			// Remove elements in successors if they are the same as an previous state
			for(Board tmpBoard : previousStates){
				if(successors.contains(tmpBoard)){
					System.out.println("Boo");
					successors.remove(tmpBoard);
				}
			}

			bestSuccessor = getBestSuccessor(successors, true);

		} else {
			getSheepSuccessors(successors, new Board(board));
			
			// Remove elements in successors if they are the same as an previous state
			for(Board tmpBoard : previousStates){
				if(successors.contains(tmpBoard)){
					System.out.println("Boo");
					successors.remove(tmpBoard);
				}
			}

			bestSuccessor = getBestSuccessor(successors, false);
		}

		// System.out.println("Sheep: " +
		// bestSuccessor.getSheepPositions().size());

		// Gets the move to create bestSuccessor
		Move move = Move.move(bestSuccessor.getChangedPositions());

		// System.out.println("Move made: " + move.toString());

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
	private Board getBestSuccessorSimple(ArrayList<Board> successors,
			boolean fox) {

		// Holds the best Successor
		Board bestSuccessor = successors.get(0);

		// Iterate through successors, will leave bestSuccessor as the one with
		// the highest utility value
		for (Board successor : successors) {
			if (successor.getUtility() > bestSuccessor.getUtility()) {
				bestSuccessor = successor;
			}
		}

		// Return bestSuccessor
		return bestSuccessor;
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

		// Holds the best value
		double bestValue = Double.NEGATIVE_INFINITY;

		// Holds the depth to search to
		int depth = 3;

		// Holds the best Successor
		Board bestSuccessor = null;

		// Iterate through successors
		for (Board tmpSuccessor : successors) {

			// Get alpha
			double alpha = minMax(tmpSuccessor, depth, bestValue,
					Double.POSITIVE_INFINITY, false);

			// Check if alpha is better than betsValue or that bestSuccessor is
			// null
			if (alpha > bestValue || bestSuccessor == null) {

				// Set bestSuccessor to tmpSuccessor
				bestSuccessor = tmpSuccessor;

				// Set bestValue to alpha
				bestValue = alpha;
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
		if (node.isTerminal() == 1 || node.isTerminal() == 2 || depth == 0) {

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

			// Holds foxes that have moves they can do
			ArrayList<Position> foxesThatHaveMoves = new ArrayList<Position>();

			// Get the Fox Successors
			getFoxSuccessors(successors, new Board(node), null,
					foxesThatHaveMoves, null, false);
			getFoxSuccessors(successors, new Board(node), null,
					foxesThatHaveMoves, null, true);

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

			// Iterate through newPositions
			for (Position newPosition : newPositions) {

				successor.changePostition(sheepPosition, newPosition, false);
				successors.add(successor);
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
	 * @param foxesThatHaveMoves
	 *            A List of foxes that have moves they can do
	 * @param foxToJump
	 *            The fox that are allowed to jump again
	 * @param jump
	 *            If jumps are happening
	 */
	private void getFoxSuccessors(ArrayList<Board> successors, Board node,
			Board alreadyJumpedBoard, ArrayList<Position> foxesThatHaveMoves,
			Position foxToJump, boolean jump) {

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

			// If newPositions is not empty
			if (!newPositions.isEmpty()) {

				// Note that foxPosition have moves it can do
				foxesThatHaveMoves.add(foxPosition);
			}

			// Act depending on if jumping or not
			if (!jump) {

				// Iterate through newPositions
				for (Position newPosition : newPositions) {

					successor.changePostition(new Position(foxPosition),
							new Position(newPosition), false);

					successors.add(successor);
				}
			} else {

				// System.out.println("Positions: " + newPositions.size());

				// Holds a temporary Successor
				Board tmpSuccessor;

				// Iterate through newPositions
				for (Position newPosition : newPositions) {

					// Set tmpSuccessor to match successor
					tmpSuccessor = new Board(successor);

					// System.out.println("The new position: " + newPosition.x
					// + ":" + newPosition.y);
					//
					// System.out.println("Before: "
					// + tmpSuccessor.getSheepPositions().size());

					tmpSuccessor.changePostition(new Position(foxPosition),
							new Position(newPosition), true);

					// System.out.println("After: "
					// + tmpSuccessor.getSheepPositions().size());

					successors.add(tmpSuccessor);

					// Call getFoxMoveSuccessors again, since several jumps
					// are allowed
					getFoxSuccessors(successors, null, new Board(tmpSuccessor),
							foxesThatHaveMoves, new Position(newPosition.x,
									newPosition.y), true);
				}
			}

			// If we are at the second call of the method
			if (step == 2) {

				// If foxPosition isn't in foxesThatHaveMoves
				if (!foxesThatHaveMoves.contains(foxPosition)) {

					// Remove foxPosition from board
					board.removeFox(foxPosition);
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

		// Get nr of moves in move
		int nrOfMoves = move.length() - move.replaceAll(" ", "").length();

		// Holds the x and y values
		int startX = 0;
		int stopX = 0;
		int startY = 0;
		int stopY = 0;

		// Convert move to a char Array
		char[] moveArray = move.toCharArray();

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
				previousStates.add(new Board (board));

				if (nrOfMoves == 1) {

					board.updateBoard(new Position(startX, startY),
							new Position(stopX, stopY), false);

				} else {
					
					board.updateBoard(new Position(startX, startY),
							new Position(stopX, stopY), true);
				}
			}
		}
	}

	@Override
	public String getPlayerName() {
		return playerName;
	}
}
