/*
 * Strat-game PC-Server :
 * 
 * Code : Fr�d�ric Meslin, Florent Touchard, Benjamin Blois
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
	private List<GreekColumn> columns = null;
	private List<Player> players = null;
	private List<Client> clients = null;
	private Sounds sound = null;
	
	// Interface objects
	private ReentrantLock mutex = null;

	// Clock system
	private boolean partying = false;
	
	// Game global constants
	private static final int WAIT_TIME = 100;
	private static final int UPDATE_TIME = 60;

	private static final int STONEDEC_PERIOD = 20000 / UPDATE_TIME;
	private static final int ACTIONINC_PERIOD = 6000 / UPDATE_TIME;
	private static final int BONUS_PERIOD = 12000 / UPDATE_TIME;
	private static final int NORMAL_CHANCE = 50;
	private static final int SPECIAL_CHANCE = 20;
	private static final int NB_BONUS = 5;

	// Game bonus
	private static final int EMPTYACTION_BONUS = 0;
	private static final int MINUS_BONUS = 1;
	private static final int FILLACTION_BONUS = 2;
	private static final int PLUS_BONUS = 3;
	private static final int SWITCH_BONUS = 4;

	private static final int EMPTYACTION_COST = 4;
	private static final int MINUS_COST = 4;
	private static final int FILLACTION_COST = 2;
	private static final int PLUS_COST = 2;	
	private static final int SWITCH_COST = 6;

	// Game method
	public Game() {
		sound = new Sounds();
		mutex = new ReentrantLock();
		clients = new ArrayList<Client>();
	}

	/**************************************************************************/
	public void run() {
		while (connectionStep()) {
			constructionStep();
			gameStep();
		}
		System.out.println("Game : end game ");
	}

	public void stop() {
		partying = false;
	}
	
	/**************************************************************************/
	private boolean connectionStep() {
		// Open the connection window
		System.out.println("Game : starting olympe ... ");
		sound.startMenu();
		Olympe olympe = new Olympe(clients);
		olympe.start();

		// Wait for olympe to close
		while (olympe.isRunning()) {
			try {
				wait(WAIT_TIME);
			} catch (Exception ex) {
			}
		}

		// Check for closing
		if (olympe.isQuitted()) {
			olympe = null;
			sound.stopMenu();
			System.out.println("Game : ... olympe quitted ");
			return false;
		}
		
		// Close the connection window
		olympe = null;
		sound.stopMenu();
		System.out.println("Game : ... olympe closed ");
		return true;
	}

	private void constructionStep() {
		// Create the columns and players
		columns = new ArrayList<GreekColumn>();
		players = new ArrayList<Player>();
		for (int i = 0; i < clients.size(); i ++) {
			GreekColumn column = new GreekColumn(i);
			columns.add(column);
			Player player = new Player(i, column);
			players.add(player);
		}
	}
	
	private void gameStep() {
		// Open the game window
		System.out.println("Game : launching game ... ");
		sound.startMix();
		GameScreen screen = new GameScreen(this);
		screen.setVisible(true);
		
		// Start the server
		Server server = new Server(this, clients);
		server.startGame();
		
		// Start the game clock
		Timer timer = new Timer();
		timer.schedule(new GameClock(server, screen), 0, UPDATE_TIME);
		
		// Game main loop
		partying = true;
		while (partying) {
			try {
				wait(WAIT_TIME);
			} catch (Exception ex) {
			}
		}
		
		// Release the game clock
		timer.cancel();
		timer = null;
		
		// Close the server
		for (int i = 0; i < players.size(); i ++) {
			server.stopGame(i, players.get(i).getColumn().isStonesEmpty());
		}
		
		// Close the server
		sound.stopMix();
		
		// Close the window
		screen.setVisible(false);
		screen = null;
	}

	/**************************************************************************/
	public void stealStone(int iD, int to) {
		// Check general conditions
		if (iD >= players.size()) return;
		if (to >= players.size()) return;
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
		if (!pTo.getColumn().isStonesEmpty()) {
			pFrom.getColumn().addStones(1);
			pTo.getColumn().addStones(-1);
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
		// Check general conditions
		if (iD >= players.size()) return;
		if (to >= players.size()) return;
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
		if (!pFrom.getColumn().isStonesEmpty()) {
			pTo.getColumn().addStones(1);
			pFrom.getColumn().addStones(-1);
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
		// Check general conditions
		if (iD >= players.size()) return;
		if (bonus >= NB_BONUS) return;
		if (to >= players.size()) return;
		if (from >= players.size()) return;
		// Lock the mutex
		mutex.lock();
		// Get the objects
		Player pUser = players.get(iD);
		Player pFrom = players.get(from);
		Player pTo = players.get(to);
		// Switch depending on the bonus
		switch (bonus) {
		case PLUS_BONUS:
			// Get one free stone
			pTo.getColumn().addStones(3);
			sound.playNormalBonus();
			System.out.println("Game : player " + Player.names[iD]
					+ " use bonus : player " + Player.names[to]
					+ " get four blocks");
			break;
		case MINUS_BONUS:
			// Suppress one stone
			pTo.getColumn().addStones(-3);
			sound.playNormalBonus();
			System.out.println("Game : player " + Player.names[iD]
					+ " use bonus : player " + Player.names[to]
					+ " lost four blocks");
			break;
		case FILLACTION_BONUS:
			// Fill the action bar
			pTo.setActions(Player.MAX_ACTIONS);
			sound.playNormalBonus();
			System.out.println("Game : player " + Player.names[iD]
					+ " use bonus : player " + Player.names[to]
					+ " get full action bar");
			break;
		case EMPTYACTION_BONUS:
			// Empty one action bar
			pTo.setActions(0);
			sound.playNormalBonus();
			System.out.println("Game : player " + Player.names[iD]
					+ " use bonus : player " + Player.names[to]
					+ " lost is full action bar");
			break;
		case SWITCH_BONUS:
			// Exchange the columns
			GreekColumn temp = pTo.getColumn();
			pTo.setColumn(pFrom.getColumn());
			pFrom.setColumn(temp);
			// Update action points
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
	
	public GreekColumn getColumn(int iD) {
		return columns.get(iD);
	}
	
	public int getNbPlayers() {
		return players.size();
	}

	public int getNbColumns() {
		return columns.size();
	}
	
	/**************************************************************************/
	public boolean getWeakness(int iD) {
		int n1 = (iD + 1) % players.size();
		int n2 = (iD + players.size() - 1) % players.size();
		return (players.get(n1).getColumn().getStones()	+ players.get(n2).getColumn().getStones()) < GreekColumn.NB_STONES;
	}

	/**************************************************************************/
	private class GameClock extends TimerTask {

		// Game clock attributes
		private int stonesCounter;
		private int actionsCounter;
		private int bonusCounter;
		private Server server;
		private GameScreen screen;

		/**************************************************************************/
		public GameClock(Server server, GameScreen screen) {
			// Initialize the counters
			stonesCounter = STONEDEC_PERIOD;
			actionsCounter = ACTIONINC_PERIOD;
			bonusCounter = BONUS_PERIOD;

			// Store the objects
			this.server = server;
			this.screen = screen;
		}
		
		/**************************************************************************/
		public void run() {
			// Lock the mutex
			mutex.lock();
			
			// Update the whole game
			gameUpdateStonesActions();
			gameUpdateBonus();
			if (screen instanceof GameScreen) screen.repaint();
			
			// Send update to each client
			for (int i = 0; i < players.size(); i++) {
				Player player = players.get(i);
				server.updateClient(i, player.getColumn().getStones(), player.getActions());
			}
			
			// Check the game state
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).getColumn().isStonesEmpty())
					partying = false;
			}
			// Unlock the mutex
			mutex.unlock();
		}
		
		private void gameUpdateStonesActions() {
			// Actualize column heights
			if (--stonesCounter == 0) {
				for (int i = 0; i < players.size(); i++) {
					// Check neighbors
					if (!getWeakness(i)) players.get(i).getColumn().addStones(-1);
					else {
						GreekColumn column = players.get(i).getColumn();
						column.addStones(-2);
						column.makeVibrations();
					}
				}
				// Play the horrible sound
				sound.playStonedown();
				stonesCounter = STONEDEC_PERIOD;
			}
			// Actualize action points
			if (--actionsCounter == 0) {
				for (int i = 0; i < players.size(); i++) {
					players.get(i).addActions(1);
				}
				actionsCounter = ACTIONINC_PERIOD;
			}
		}
		
		private void gameUpdateBonus() {
		// Send bonus depending on time
			if (--bonusCounter == 0) {
				for (int i = 0; i < players.size(); i++) {
					double rnd = Math.random() * 100;
					if (!getWeakness(i)) {						
						int bonus = (int) (Math.random() * (NB_BONUS - 2) + 0.5);
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
