package se.miun.chrfin.foxgame.logic;

/**
 * @author Christoffer Fink
 */
public class Position {

	public int x;
	public int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Copy constructor
	 * Creates a Position object with the same values as another one
	 * 
	 * @param position The Positions object whose values to use
	 */
	public Position(Position position) {
		
		this.x = position.x;
		this.y = position.y;
	}

	public static Position pos(int x, int y) {
		return new Position(x, y);
	}

	public static Position valueOf(String s) {
		String[] coord = s.split(",");
		return pos(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		// Return false if obj is null
		if (obj == null) {
	        return false;
	    }
		
		// Return false if obj isn't a Position object
	    if (getClass() != obj.getClass()) {
	        return false;
	    }
		
	    // Cast obj to Position
		Position position = (Position) obj;
		
		// Check if x and y is the same, if so, return true
		if(position.x == this.x && position.y == this.y){
			return true;
		}
		
		// Return false
		return false;
	}
}
