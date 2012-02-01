/*
 * Strat-game Android-Client :
 * 
 * Code : Frédéric Meslin, Florent Touchard, Benjamin Blois
 * Released on 29/01/12 at 15h00 for the 4th GGJ
 * Corrections added for public presentation after the event
 * 
 * This program and all its resources included is
 * published under the Creative common license By-NC 3.0.
 * 
 * For more informations :
 * http://creativecommons.org/licenses/by-nc/3.0/
 */

package org.globalgamejam.strat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.badlogic.gdx.Gdx;

public class Communication {
	// Communication objects
	private Socket socket;
	private OutputStream ostream;
	private StreamAnalyzer sa;

	// Blackboard data
	private int iD;
	private int totalId;
	private int stones;
	private int actions;
	private int bonus;
	private int status;
	private boolean[] alives;

	// Global client -> server protocol
	private static final int STEAL_STONE = 0;
	private static final int GIVE_STONE = 1;
	private static final int USE_BONUS = 2;

	// Global server -> client protocol
	private static final int START_GAME = 0;
	private static final int STONE_QUANTITY = 1;
	private static final int ACTION_GAUGE = 2;
	private static final int OBTAIN_BONUS = 3;
	private static final int DISCONNECT = 4;
	private static final int END_GAME = 5;

	// Global public constants
	public static final int STATUS_IN_PROGRESS = 0;
	public static final int STATUS_WIN = 1;
	public static final int STATUS_LOST = 2;
	public static final int STATUS_DECO = 3;
	public static final int MAX_CLIENTS = 6;

	public Communication(String host, int port) throws IOException {
		// Socket creation (throws exception on error)
		this.socket = new Socket(host, port);

		// Get output stream
		ostream = socket.getOutputStream();

		// Initialize internal data
		alives = new boolean[MAX_CLIENTS];
		for (int i = 0; i < MAX_CLIENTS; i++)
			alives[i] = false;
		iD = -1;
		totalId = 0;
		stones = 0;
		actions = 0;
		bonus = -1;
		status = 0;
	}

	public void stealStone(int who) {
		byte[] msg = new byte[2];
		msg[0] = STEAL_STONE;
		msg[1] = (byte) who;
		try {
			ostream.write(msg);
		} catch (IOException e) {
			Gdx.app.error("Communication",
					"stealStone : IOException (" + e.getMessage() + ")");
		}
	}

	public void giveStone(int who) {
		byte[] msg = new byte[2];
		msg[0] = GIVE_STONE;
		msg[1] = (byte) who;
		try {
			ostream.write(msg);
		} catch (IOException e) {
			Gdx.app.error("Communication",
					"giveStone : IOException (" + e.getMessage() + ")");
		}
	}

	public void useBonus(int bonus, int from, int to) {
		byte[] msg = new byte[4];
		msg[0] = USE_BONUS;
		msg[1] = (byte) bonus;
		msg[2] = (byte) from;
		msg[3] = (byte) to;
		try {
			ostream.write(msg);
		} catch (IOException e) {
			Gdx.app.error("Communication",
					"useBonus : IOException (" + e.getMessage() + ")");
		}
	}

	public int getId() {
		return iD;
	}

	public int getTotalId() {
		return totalId;
	}

	public int getStones() {
		return stones;
	}

	public int getActions() {
		return actions;
	}

	public int getBonus() {
		return bonus;
	}

	// return one of the STATUS_* value
	public int getStatus() {
		return status;
	}

	public boolean isConnected() {
		return (iD != -1);
	}

	public boolean isAlive(int id) {
		if (id >= 0 && id < alives.length)
			return alives[id];
		return false;
	}

	public void start() {
		sa = new StreamAnalyzer();
		sa.start();
	}

	public void stop() {
		sa.end();
	}

	/**************************************************************************/
	private class StreamAnalyzer extends Thread {
		private boolean running;

		public void start() {
			running = true;
			super.start();
		}

		public void end() {
			if (!running)
				return;
			running = false;
			try {
				this.join();
			} catch (Exception ex) {
			}
		}

		public void run() {
			// Open the input stream
			InputStream istream;
			try {
				istream = socket.getInputStream();
				while (running) {
					// Get the message
					int cmd = istream.read();
					if (cmd < 0)
						break;

					// Interpret the message
					int tmp;
					switch (cmd) {
					case START_GAME:
						iD = istream.read();
						totalId = istream.read();
						for (int i = 0; i < totalId; i++)
							alives[i] = true;
						Gdx.app.log("Communication", "Start game : " + iD + "/"
								+ totalId);
						break;
					case STONE_QUANTITY:
						stones = istream.read();
						Gdx.app.log("Communication", "Stones update : "
								+ stones);
						break;
					case ACTION_GAUGE:
						actions = istream.read();
						Gdx.app.log("Communication", "Action power update : "
								+ actions);
						break;
					case OBTAIN_BONUS:
						bonus = istream.read();
						Gdx.app.log("Communication", "Try to catch bonus : "
								+ bonus);
						break;
					case DISCONNECT:
						tmp = istream.read();
						if (tmp > 0 && tmp < alives.length)
							alives[tmp] = false;
						Gdx.app.log("Communication", "Player " + tmp
								+ " is gone away");
						break;
					case END_GAME:
						tmp = istream.read();
						if (tmp == 1)
							status = 1;
						else if (tmp == 0)
							status = 2;
						break;
					}
				}
			} catch (IOException io) {
				Gdx.app.error("Communication",
						"StreamAnalyzer : " + io.getMessage());
				running = false;
				status = 3;
				iD = -1;
			}
		}
	}
}
