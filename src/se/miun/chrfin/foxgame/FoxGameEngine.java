package se.miun.chrfin.foxgame;

import java.util.ArrayList;

import se.miun.chrfin.foxgame.com.GameStatus;
import se.miun.chrfin.foxgame.setup.PlayerSetup;

/**
 * @author Christoffer Fink
 */
public class FoxGameEngine implements AiGameEngine {

	// TODO: Better name?
	/**
	 * Holds the player name
	 */
	private final String playerName = "alen1200";

	/**
	 * Holds a String representing the players role
	 */
	private final String playerRole;

	private ArrayList<ArrayList<Integer>> gameBoard = new ArrayList<ArrayList<Integer>>();

	/**
	 * Constructor
	 * 
	 * @param setup
	 *            A PlayerSetuo object holding the setup information
	 */
	public FoxGameEngine(PlayerSetup setup) {

		// Set playerRole
		playerRole = setup.playerRole;

		// Iterates through and creates the game board and sets the fields to
		// the appropriate values
		// 0 = empty, 1 = sheep, 2 = fox
		for (int x = 1; x <= 7; x++) {

			gameBoard.add(new ArrayList<Integer>());

			for (int y = 1; y <= 7; y++) {

				if (y == 1 && (x == 3 || x == 5)) {
					gameBoard.get(x).set(y, 2);
				} else if (y >= 4) {
					gameBoard.get(x).set(y, 1);
				} else {
					gameBoard.get(x).set(y, 0);
				}
			}
		}
	}

	/**
	 * Return a move of the form "x1,y1 x2,y2".
	 */
	@Override
	public String getMove(GameStatus status) {

		// Acts depending on if playerRole equals FOX or SHEEP
		if (playerRole.equals("FOX")) {

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
