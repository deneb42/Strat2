package globalgamejam.org.strat;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Game {

	// Internal objects
	private List<Player> players;
	private Server server;
		
	// Interface objects
	private ConnectionScreen conScreen;
	private GameScreen gameScreen;

	// Clock system
	private Timer timer;

	// Game global constants
	private static final int UNINIT = 0;
	private static final int CONNECTION = 1;
	private static final int INGAME = 2;
	private static final int ENDING = 3;

	private static final int MAX_PLAYER = 6;

	private static final int WAIT_TIME = 100;
	private static final int UPDATE_TIME = 60;
	
	// Game state
	private int gameState = 0;

	// Game method
	public Game() {
		gameState = UNINIT;
		conScreen = new ConnectionScreen(this);
		gameScreen = new GameScreen(this);
	}

	public void run() {
	// Step 1 : connection
		connectionStep();
	// Start the game
		if (gameState == INGAME) {
			gameStep();
		}
	// Display the ending screen
		System.out.println("Game : end game ");
	}

	private void connectionStep() {
		// Open the connection window
		System.out.println("Game : create interfaces ");
		conScreen.setVisible(true);
		// Start the connection manager
		System.out.println("Game : open server ");
		players = new ArrayList<Player>();
		server = new Server(this);
		server.startConnection();
		// Wait for end of connection
		gameState = CONNECTION;
		while (gameState == CONNECTION) {
			try {
				wait(WAIT_TIME);
			} catch (Exception ex) {
			}
		}
		server.stopConnection();
		// Close the window
		conScreen.setVisible(false);
		conScreen = null;
	}
	
	private void gameStep() {
		// Open the game window
		System.out.println("Game : launch game ");
		gameScreen.setVisible(true);
		// Start the server
		server.startCommunication();
		// Start the game clock
		timer = new Timer();
		timer.schedule(new GameClock(), 0, UPDATE_TIME);
		// Game loop
		while (gameState == INGAME) {
			try {
				wait(WAIT_TIME);
			} catch (Exception ex) {
			}
		}
		server.stopCommunication();
		// Close the window
		gameScreen.setVisible(false);
		gameScreen = null;
	}

	/**************************************************************************/
	public void start() {
		gameState = INGAME;
	}

	public void end() {
		gameState = ENDING;
	}

	/**************************************************************************/
	public void stealStone(int iD, int to) {
		// Get the objects
		Player pFrom = players.get(iD);
		Player pTo = players.get(to);
		// Perform the steal
		if (!pFrom.isStonesFull() && !pTo.isStonesEmpty()) {
			pFrom.addStones(1);
			pTo.addStones(-1);
		}
		// Actualize the action points
		pFrom.addActions(-1); // TODO depend de la distance au voisin
		System.out.println("Game : " + Player.names[iD] + " steal stone from " + Player.names[to]);
	}

	public void giveStone(int iD, int to) {
		// Get the objects
		Player pFrom = players.get(iD);
		Player pTo = players.get(to);
		// Perform the gift
		if (!pTo.isStonesFull() && !pFrom.isStonesEmpty()) {
			pTo.addStones(1);
			pFrom.addStones(-1);
		}
		// Actualize the action points
		pFrom.addActions(-1); // TODO depend de la distance au voisin
		System.out.println("Game : " + Player.names[iD] + " give stone to " + Player.names[to]);
	}

	public void useBonus(int iD, int bonus, int from, int to) {

	}

	/**************************************************************************/
	public void connect(int iD) {
		players.add(new Player());
		conScreen.addClient(Player.names[iD]);
	}

	public void disconnect(int iD) {
		server.disconnectClient(iD);
	}

	/**************************************************************************/
	private class GameClock extends TimerTask {
		
		public void run() {
			// Update each client periodically
			for (int i = 0; i < players.size(); i ++) {
				Player player = players.get(i);
				server.updateClient(i, player.getStones(), player.getActions());
			}
		}
	}

	/**************************************************************************/
	public static void main(String[] args) {
		Game game = new Game();
		game.run();
	}

}
