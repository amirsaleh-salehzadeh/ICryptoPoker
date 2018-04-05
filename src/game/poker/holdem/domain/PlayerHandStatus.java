package game.poker.holdem.domain;

/**
 * Enum type to track the player's current status in the game/hand
 * 
 * @author jacobhyphenated
 */
public class PlayerHandStatus {
	public static final int NORMAL = 0;
	public static final int CALLED = 1;
	public static final int CHECKED = 2;
	public static final int FOLDED = 3;
	public static final int RAISED = 4;
	public static final int BET = 5;
	public static final int PREFLOPEND = 6;
	public static final int FLOPEND = 7;
	public static final int TURNEND = 8;
	public static final int RIVEREND = 9;
	public static final int HANDEND = 10;
	public static final int SITOUT = 11;
}
