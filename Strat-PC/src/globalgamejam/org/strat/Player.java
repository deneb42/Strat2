package globalgamejam.org.strat;

public class Player {
	
	// Player constants
	private static final int START_STONES = 10;
	private static final int START_ACTIONS = 10;
	private static final int MAX_STONES = 15;
	private static final int MAX_ACTIONS = 10;
	
	// Player names
	public static final String names[] = {"Alexandros", "Basileios", "Gouidon", "Demokritos", "Elenos", "Klearchos"};
	
	// Internal objects
	private int stones;
	private int actions;

	public Player() {
		this.stones = START_STONES;
		this.actions = START_ACTIONS;
	}
	
	public void addStones(int stones) {
		this.stones += stones;
		if (this.stones < 0) this.stones = 0;
		else if (this.stones > MAX_STONES) this.stones = MAX_STONES;
	}
	
	public void addActions(int actions) {
		this.actions += actions;
		if (this.actions < 0) this.actions = 0;
		else if (this.actions > MAX_ACTIONS) this.actions = MAX_ACTIONS;
	}
	
	public int getStones() { return stones; }
	public boolean isStonesEmpty() { return stones == 0; }
	public boolean isStonesFull() { return stones == MAX_STONES; }
	
	public int getActions() { return actions; }
	public boolean isActionsEmpty() { return actions == 0; }
	public boolean isActionsFull() { return actions == MAX_ACTIONS; }
}
