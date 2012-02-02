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

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;

public class ConnectionScreen extends JDialog implements ActionListener {
	
	// Window layout constants
	private static final int RESO_X = 320;
	private static final int RESO_Y = 240;
	
	// Internal objects
	private Game game;
	private JPanel panel;
	private JList<String> clientList;
	private DefaultListModel<String> clientListModel;
	private JButton startButton;
	private JButton exitButton;
		
	/*************************************************************************/
	public ConnectionScreen (Game game) {
		super();
		// Store the game object
		this.game = game;
		
		// Create the window
		setTitle("Clients connection :");
		setSize(RESO_X, RESO_Y);
		setResizable(false);
				
		// Create the visual panel
		panel = new JPanel(new GridLayout(3, 1));
		setContentPane(panel);
		
		// Add the client list
		clientListModel = new DefaultListModel<String>();
		clientList = new JList<String>(clientListModel);
		clientList.setMinimumSize(new Dimension(300, 200));
		panel.add(clientList);
		
		// Add the interface buttons
		startButton = new JButton("Start");
		startButton.setActionCommand("Start");
		startButton.addActionListener(this);
		panel.add(startButton);
		
		exitButton = new JButton("Exit");
		exitButton.setActionCommand("Exit");
		exitButton.addActionListener(this);
		panel.add(exitButton);
	}
	
	public void actionPerformed(ActionEvent e) {
	    String Action;
	    Action = e.getActionCommand ();
	    if (Action.equals ("Exit")) game.end();
	    else if (Action.equals ("Start")) game.start();
	}
	
	/*************************************************************************/
	public void addClient(String name)
	{
		clientListModel.add(clientListModel.size(), name);
	}
	
	public void removeClient(int iD)
	{
		clientListModel.removeElementAt(iD);
	}
}
