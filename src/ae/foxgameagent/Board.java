package ae.foxgameagent;

import java.util.ArrayList;

import se.miun.chrfin.foxgame.logic.Position;

/**
 * @author Albin Engström
 */
public class Board {

	/**
	 * Holds the positions of the foxes
	 */
	private ArrayList<Position> foxPositions = new ArrayList<Position>();

	/**
	 * Holds the positions of the sheep
	 */
	private ArrayList<Position> sheepPositions = new ArrayList<Position>();

	/**
	 * Holds the Positions that have changed to create the current state
	 */
	private ArrayList<Position> changedPositions = new ArrayList<Position>();

	/**
	 * Constructor
	 */
	public Board() {

		// Set the starting positions of the foxes
		foxPositions.add(new Position(3, 1));
		foxPositions.add(new Position(5, 1));

		// Set the starting positions of the sheep
		for (int y = 4; y <= 5; y++) {
			for (int x = 1; x <= 7; x++) {
				sheepPositions.add(new Position(x, y));
			}
		}
		for (int y = 6; y <= 7; y++) {
			for (int x = 3; x <= 5; x++) {
				sheepPositions.add(new Position(x, y));
			}
		}
	}

	/**
	 * Copy constructor
	 */
	public Board(Board board) {

		// Fill sheepPositions with new Positions with the same values as the
		// ones in board
		for (Position position : board.getSheepPositions()) {
			sheepPositions.add(new Position(position));
		}

		// Fill foxPositions with new Positions with the same values as the ones
		// in board
		for (Position position : board.getFoxPositions()) {
			foxPositions.add(new Position(position));
		}
	}
	
	/**
	 * Remove a fox from the board
	 * 
	 * @param position The Position of the fox
	 */
	public void removeFox(Position position){
		
		// Remove it if it exists
		if (foxPositions.contains(position)) {
			foxPositions.remove(position);
		}
	}

	/**
	 * Changes the position of an sheep or fox
	 * 
	 * @param oldPosition
	 *            The position it's currently in
	 * @param newPosition
	 *            The position it shall move to
	 */
	public void changePostition(Position oldPosition, Position newPosition,
			boolean jump) {

		// Find oldPostion in sheepPositions, if it exists, and changes it to
		// match newPosition
		if (sheepPositions.contains(oldPosition)) {
			for (Position position : sheepPositions) {
				if (position.equals(oldPosition)) {
					position.x = newPosition.x;
					position.y = newPosition.y;

					// Add Positions to changedPositions
					changedPositions.add(oldPosition);
					changedPositions.add(newPosition);
				}
			}

			return;
		}

		// Find oldPostion in foxPositions, if it exists, and changes it to
		// match newPosition
		if (foxPositions.contains(oldPosition)) {
			for (Position position : foxPositions) {
				if (position.equals(oldPosition)) {
					position.x = newPosition.x;
					position.y = newPosition.y;

					if (!jump) {
						// Add Positions to changedPositions
						changedPositions.add(oldPosition);
						changedPositions.add(newPosition);
					} else {

						// Get the x and y values of the Position jumped over
						int x = (newPosition.x + oldPosition.x) / 2;
						int y = (newPosition.y + oldPosition.y) / 2;

						// Removes the sheep at that Position
						sheepPositions.remove(new Position(x, y));

						// If changedPositions is empty, add both new and old
						// Positions
						if (changedPositions.isEmpty()) {
							changedPositions.add(oldPosition);
							changedPositions.add(newPosition);
						} else {
							// If it isn't add only newPosition, since the old
							// Position is already added

							changedPositions.add(newPosition);
						}
					}
				}
			}
		}
	}

	/**
	 * Update the board
	 */
	public void updateBoard(Position oldPosition, Position newPosition,
			boolean jump) {

		// Call changePosition and clear changedPositions
		changePostition(oldPosition, newPosition, jump);
		changedPositions.clear();
	}

	/**
	 * @return the foxPositions
	 */
	public ArrayList<Position> getFoxPositions() {
		return foxPositions;
	}

	/**
	 * @param foxPositions
	 *            the foxPositions to set
	 */
	public void setFoxPositions(ArrayList<Position> foxPositions) {
		this.foxPositions = foxPositions;
	}

	/**
	 * @return the sheepPositions
	 */
	public ArrayList<Position> getSheepPositions() {
		return sheepPositions;
	}

	/**
	 * @param sheepPositions
	 *            the sheepPositions to set
	 */
	public void setSheepPositions(ArrayList<Position> sheepPositions) {
		this.sheepPositions = sheepPositions;
	}

	/**
	 * @return the changedPositions
	 */
	public ArrayList<Position> getChangedPositions() {
		return changedPositions;
	}

	/**
	 * @param changedPositions
	 *            the changedPositions to set
	 */
	public void setChangedPositions(ArrayList<Position> changedPositions) {
		this.changedPositions = changedPositions;
	}

}
