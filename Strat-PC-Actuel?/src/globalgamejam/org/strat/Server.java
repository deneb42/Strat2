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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class Server {
	// Global server constants
	private static final int SERVER_PORT = 50000;
	private static final int MAX_CLIENTS = 6;
	private static final int SERVER_TIMEOUT = 1000;
	
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
	
	// Internal objects
	private Game game = null;
	private List<ClientCom> clients = null;
	private Connection connection = null;
		
	// Server constructor
	public Server(Game game) {
		super();
		this.game = game;
		clients = new ArrayList<ClientCom>();
	}

	/**************************************************************************/
	public void startConnection() {
		System.out.println("Server : awaiting client connections");
		connection = new Connection();
		connection.start();
		
	}

	public void stopConnection() {
		if (connection != null)
		connection.end();
	}

	/**************************************************************************/
	public void startCommunication() {
		System.out.println("Server : starting communication");
		for (int i = 0; i < clients.size(); i++) {
			ClientCom client = clients.get(i);
			client.start();
		}	
	}
	
	public void stopCommunication() {
		for (int i = 0; i < clients.size(); i++) {
			ClientCom client = clients.get(i);
			client.end();
		}
	}
	
	/**************************************************************************/
	public void startGame(int nb) {
		for (int i = 0; i < clients.size(); i++) {
			ClientCom client = clients.get(i);
			client.startGame(nb);
		}
		System.out.println("Server : starting game");
	}
	
	public void endGame(int iD) {
		for (int i = 0; i < clients.size(); i++) {
			ClientCom client = clients.get(i);
			client.endGame(iD);
		}
		System.out.println("Server : ending game");
	}
	
	public void disconnectClient(int iD) {
		for (int i = 0; i < clients.size(); i++) {
			ClientCom client = clients.get(i);
			client.disconnect(iD);
		}
		System.out.println("Server : removing client iD = " + iD);
	}
	
	public void updateClient(int iD, int stones, int actions) {
		ClientCom client = clients.get(iD);
		client.update(stones, actions);
	}
	
	public void bonusClient(int iD, int bonus) {
		ClientCom client = clients.get(iD);
		client.obtainBonus(bonus);
	}
	
	/**************************************************************************/
	private class Connection extends Thread {

		private boolean running;
		
		/**************************************************************************/
		public void start() {
			running = true;
			super.start();
		}
		
		public void end() {
			if (!running) return;
			running = false;
			try {this.join();} catch (Exception ex) {}
		}
		
		/**************************************************************************/
		public void run() {
			// Open the server listener socket
			ServerSocket serverSocket;
			try {
				serverSocket = new ServerSocket(SERVER_PORT);
				serverSocket.setSoTimeout(SERVER_TIMEOUT);
				System.out.println("Server : listening on port = " + SERVER_PORT);
			} catch (IOException ex) {
				System.out.println("Server : unable to open listener socket "
						+ ex.getLocalizedMessage());
				System.exit(-1);
				return;
			}
			// Listen to client connections
			while (running && clients.size() < MAX_CLIENTS) {
				try {
					// Create the client
					Socket socket = serverSocket.accept();
					clients.add(new ClientCom(clients.size(), socket));
					// Add to the game
					game.connect(clients.size() - 1);
					System.out.println("Server : adding client iD = " + (clients.size() - 1));
				} catch (SocketTimeoutException to) {
				} catch (Exception ex) {
					System.out.println("Server : unable to register client "
							+ ex.getLocalizedMessage());
				}
			}
			// Stop the listening
			running = false;
		}
	}
	
	/**************************************************************************/
	private class ClientCom extends Thread {
		
		private int iD;
		private Socket socket = null;
		private boolean running;
		
		public ClientCom(int iD, Socket client) {
			this.iD = iD;
			this.socket = client;
		}
		
		/**************************************************************************/
		public void start() {
			running = true;
			super.start();
		}
		
		public void end() {
			if (!running) return;
			running = false;
			try {this.join();} catch (Exception ex) {}
		}
		
		/**************************************************************************/
		public void run() {
			// Open the input stream
			InputStream istream;
			try {
				istream = socket.getInputStream();
				while (running) {
					int bonus, to, from;
					// Get the message
					int cmd = istream.read();
					if (cmd < 0) break;
					// Interpret the message
					switch (cmd) {
					case STEAL_STONE:
						to = istream.read();
						if (to >= clients.size()) break;
						game.stealStone(iD, to);
						break;
					case GIVE_STONE:
						to = istream.read();
						if (to >= clients.size()) break;
						game.giveStone(iD, to);
						break;
					case USE_BONUS:
						bonus = istream.read();
						from = istream.read();
						to = istream.read();
						if (to > clients.size() || from > clients.size()) break;
						game.useBonus(iD, bonus, from, to);
						break;
					}
				}
			} catch (IOException io) {
				game.disconnect(iD);
				running = false;
			}
		}
		
		/**************************************************************************/
		public void startGame(int nb) {
			if (!running) return;
			// Open the stream
			OutputStream ostream;
			try {
				// Create the output stream
				ostream = socket.getOutputStream();
				// Prepare the message
				byte message[] = new byte[3];
				message[0] = START_GAME;
				message[1] = (byte) iD;
				message[2] = (byte) nb;
				// Send the message
				ostream.write(message);
			} catch (IOException io) {
				if (running) end();
				game.disconnect(iD);
			}
		}
		
		public void endGame(int iD) {
			if (!running) return;
			// Open the stream
			OutputStream ostream;
			try {
				// Create the output stream
				ostream = socket.getOutputStream();
				// Prepare the message
				byte message[] = new byte[2];
				message[0] = END_GAME;
				message[1] = (byte) (this.iD == iD ? 1 : 0);
				// Send the message
				ostream.write(message);
			} catch (IOException io) {
				if (running) end();
			}
		}
		
		/**************************************************************************/
		public void obtainBonus(int bonus) {
			if (!running) return;
			// Open the stream
			OutputStream ostream;
			try {
				// Create the output stream
				ostream = socket.getOutputStream();
				// Prepare the message
				byte message[] = new byte[2];
				message[0] = OBTAIN_BONUS;
				message[1] = (byte) bonus;
				// Send the message
				ostream.write(message);
			} catch (IOException io) {
				if (running) end();
				game.disconnect(iD);
			}
		}
		
		public void update(int stones, int actions) {
			if (!running) return;
			// Open the stream
			OutputStream ostream;
			try {
				// Create the output stream
				ostream = socket.getOutputStream();
				// Prepare the message
				byte message[] = new byte[4];
				message[0] = STONE_QUANTITY;
				message[1] = (byte) stones;
				message[2] = ACTION_GAUGE;
				message[3] = (byte) actions;
				// Send the message
				ostream.write(message);
			} catch (IOException io) {
				if (running) end();
				game.disconnect(iD);
			}
		}
		
		public void disconnect(int iD) {
			if (!running) return;
			// Open the stream
			OutputStream ostream;
			try {
				// Create the output stream
				ostream = socket.getOutputStream();
				// Prepare the message
				byte message[] = new byte[2];
				message[0] = DISCONNECT;
				message[1] = (byte) iD;
				// Send the message
				ostream.write(message);
			} catch (IOException io) {
				if (running) end();
				game.disconnect(this.iD);
			}
		}
	}
}
