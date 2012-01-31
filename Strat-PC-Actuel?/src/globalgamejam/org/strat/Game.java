/*
 * Strat-game PC-Server :
 * 
 * Code : Frédéric Meslin, Florent Touchard, Benjamin Blois
 * Released on 29/01/12 at 15h00 for the 4th GGJ
 * 
 * This program and all its resources included is
 * published under the Creative common license By-NC 3.0.
 * 
 * For more informations :
 * http://creativecommons.org/licenses/by-nc/3.0/
 */

package globalgamejam.org.strat;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

public class Game {

	// Internal objects
	private List<Player> players;
	private Server server;
	private Sounds sound;
	
	// Interface objects
	private ConnectionScreen conScreen;
	private GameScreen gameScreen;
	private ReentrantLock mutex;

	// Clock system
	private Timer timer;

	// Game global constants
	private static final int MAX_PLAYER = 6;
	private static final int WAIT_TIME = 100;
	private static final int UPDATE_TIME = 100;

	private static final int STONEDEC_PERIOD = 6000 / UPDATE_TIME;
	private static final int ACTIONINC_PERIOD = 1000 / UPDATE_TIME;
	private static final int BONUS_PERIOD = 8000 / UPDATE_TIME;
	private static final int NORMAL_CHANCE = 50;
	private static final int SPECIAL_CHANCE = 25;
	private static final int NB_BONUS_NORMAUX = 4;

	// Game bonus
	private static final int PLUSONE_BONUS = 0;
	private static final int MINUSONE_BONUS = 1;
	private static final int FILLACTION_BONUS = 2;
	private static final int EMPTYACTION_BONUS = 3;
	private static final int SWITCH_BONUS = 4;

	private static final int PLUSONE_ACTION = 2;
	private static final int MINUSONE_ACTION = 2;
	private static final int FILL_ACTION = 3;
	private static final int EMPTY_ACTION = 4;
	private static final int SWITCH_ACTION = 5;

	// Game current state
	private static final int UNINIT = 0;
	private static final int CONNECTION = 1;
	private static final int INGAME = 2;
	private static final int ENDING = 3;

	private int gameState = 0;

	// Game method
	public Game() {
		gameState = UNINIT;
		
		conScreen = new ConnectionScreen(this);
		gameScreen = new GameScreen(this);
		sound = new Sounds();
		
		mutex = new ReentrantLock();
	}

	/**************************************************************************/
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
		// Game main loop
		server.startGame(players.size());
		while (gameState == INGAME) {
			try {
				wait(WAIT_TIME);
			} catch (Exception ex) {
			}
		}
		// Release the game clock
		timer.cancel();
		timer = null;
		// Close the server
		server.endGame(0);
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
		// Lock the mutex
		mutex.lock();
		// Get the objects
		Player pFrom = players.get(iD);
		Player pTo = players.get(to);
		// Check if steal is possible
		if (pFrom.isActionsEmpty()) {
			mutex.unlock();
			return;
		}
		// Perform the steal
		if (!pTo.isStonesEmpty()) {
			pFrom.addStones(1);
			pTo.addStones(-1);
			sound.playStonedown();
		}
		// Actualize the action points
		pFrom.addActions(-1);
		System.out.println("Game : " + Player.names[iD] + " steal stone from "
				+ Player.names[to]);
		// Unlock the mutex
		mutex.unlock();
	}

	public void giveStone(int iD, int to) {
		// Lock the mutex
		mutex.lock();
		// Get the objects
		Player pFrom = players.get(iD);
		Player pTo = players.get(to);
		// Check if gift is possible
		if (pFrom.isActionsEmpty()) {
			mutex.unlock();
			return;
		}
		// Perform the gift
		if (!pFrom.isStonesEmpty()) {
			pTo.addStones(1);
			pFrom.addStones(-1);
			sound.playStonedown();
		}
		// Actualize the action points
		pFrom.addActions(-1);
		System.out.println("Game : " + Player.names[iD] + " give stone to "
				+ Player.names[to]);
		// Unlock the mutex
		mutex.unlock();
	}

	public void useBonus(int iD, int bonus, int from, int to) {
		// Lock the mutex
		mutex.lock();
		// Get the objects
		Player pUser = players.get(iD);
		Player pFrom = players.get(from);
		Player pTo = players.get(to);
		// Switch depending on the bonus
		switch (bonus) {
		case PLUSONE_BONUS:
			// Check if gift is possible
			if (pUser.getActions() < PLUSONE_ACTION) {
				mutex.unlock();
				return;
			}
			// Get one free stone
			pTo.addStones(1);
			pUser.addActions(- PLUSONE_ACTION);
			sound.playNormalBonus();
			System.out.println("Game : player " + Player.names[iD]
					+ " use bonus : player " + Player.names[to]
					+ " get one block");
			break;
		case MINUSONE_BONUS:
			// Check if gift is possible
			if (pUser.getActions() < MINUSONE_ACTION) {
				mutex.unlock();
				return;
			}
			// Suppress one stone
			pTo.addStones(-1);
			pUser.addActions(- MINUSONE_ACTION);
			sound.playNormalBonus();
			System.out.println("Game : player " + Player.names[iD]
					+ " use bonus : player " + Player.names[to]
					+ " lost one block");
			break;
		case FILLACTION_BONUS:
			// Check if gift is possible
			if (pUser.getActions() < FILL_ACTION) {
				mutex.unlock();
				return;
			}
			// Fill the action bar
			pTo.setActions(Player.MAX_ACTIONS);
			pUser.addActions(- FILL_ACTION);
			sound.playNormalBonus();
			System.out.println("Game : player " + Player.names[iD]
					+ " use bonus : player " + Player.names[to]
					+ " get full action bar");
			break;
		case EMPTYACTION_BONUS:
			// Check if gift is possible
			if (pUser.getActions() < EMPTY_ACTION) {
				mutex.unlock();
				return;
			}
			// Empty one action bar
			pTo.setActions(0);
			pUser.addActions(- EMPTY_ACTION);
			sound.playNormalBonus();
			System.out.println("Game : player " + Player.names[iD]
					+ " use bonus : player " + Player.names[to]
					+ " lost is full action bar");
			break;
		case SWITCH_BONUS:
			// Check if gift is possible
			if (pUser.getActions() < SWITCH_ACTION) {
				mutex.unlock();
				return;
			}
			// Exchange the stones
			int tmp = pTo.getActions();
			pTo.setActions(pFrom.getActions());
			pFrom.setActions(tmp);
			pUser.addActions(- SWITCH_ACTION);
			sound.playSpecialBonus();
			System.out.println("Game : player " + Player.names[iD]
					+ " use bonus : player " + Player.names[from]
					+ " exchange stones with player " + Player.names[to]);
			break;
		}
		// Unlock the mutex
		mutex.unlock();
	}

	/**************************************************************************/
	public Player getPlayer(int iD) {
		return players.get(iD);
	}

	public int getNbPlayers() {
		return players.size();
	}
	
	public boolean getWeakness(int iD) {
		int n1 = (iD + 1) % players.size();
		int n2 = (iD + players.size() - 1) % players.size();
		return (players.get(n1).getStones()	+ players.get(n2).getStones()) < (GreekColumn.NB_STONES / 2);
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

		private int stonesCounter;
		private int actionsCounter;
		private int bonusCounter;

		public GameClock() {
			// Initialize the counters
			stonesCounter = STONEDEC_PERIOD;
			actionsCounter = ACTIONINC_PERIOD;
			bonusCounter = BONUS_PERIOD;
		}

		public void run() {
			// Update the whole game
			gameUpdateStonesActions();
			gameUpdateBonus();
			gameScreen.repaint();
			// Send update to each client
			for (int i = 0; i < players.size(); i++) {
				Player player = players.get(i);
				server.updateClient(i, player.getStones(), player.getActions());
			}
			// Check the game state
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).isStonesEmpty())
					gameState = ENDING;
			}
		}
		
		private void gameUpdateStonesActions() {
			// Actualize column heights
			if (--stonesCounter == 0) {
				for (int i = 0; i < players.size(); i++) {
					// Check neighbours
					players.get(i).addStones(-1);
					if (!getWeakness(i)) players.get(i).addStones(-1);
					else players.get(i).addStones(-2);
				}
				sound.playStonedown();
				stonesCounter = STONEDEC_PERIOD;
			}
			// Actualize action points
			if (--actionsCounter == 0) {
				for (int i = 0; i < players.size(); i++) {
					players.get(i).addActions(1);
				}
				stonesCounter = ACTIONINC_PERIOD;
			}
		}
		
		private void gameUpdateBonus() {
		// Send bonus depending on time
			if (--bonusCounter == 0) {
				for (int i = 0; i < players.size(); i++) {
					double rnd = Math.random() * 100;
					if (!getWeakness(i)) {						
						int bonus = (int) (Math.random() * (NB_BONUS_NORMAUX - 1) + 0.5);
						if (rnd < NORMAL_CHANCE) {
							server.bonusClient(i, bonus);
							System.out.println("Game : send " + bonus + " bonus to player " + Player.names[i]);
						}else server.bonusClient(i, -1);
					}else {
						if (rnd < SPECIAL_CHANCE) {
							server.bonusClient(i, SWITCH_BONUS);
							System.out.println("Game : send ! MEGA-SWITCH ! bonus to player " + Player.names[i]);
						}else server.bonusClient(i, -1);
					}
				}
				bonusCounter = BONUS_PERIOD;
			}
		}
		
	}

	/**************************************************************************/
	public static void main(String[] args) {
		// Start the game
		Game game = new Game();
		game.run();
		// Quit the application
		System.exit(0);
	}

}
