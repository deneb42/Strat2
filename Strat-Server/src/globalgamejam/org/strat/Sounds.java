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

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sounds {

	// Game sound paths
	private static final String stonedownWav = "sounds/bloc.wav";
	private static final String normalbonusWav = "sounds/bonus.wav";
	private static final String specialbonusWav = "sounds/switch.wav";

	private static final String mixMusic = "sounds/mix.wav";
	private static final String menuMusic = "sounds/menu.wav";
	
	// Audio clips objects
	private Clip stonedown = null;
	private Clip normalbonus = null;
	private Clip specialbonus = null;
	
	private Clip menu = null;
	private Clip mix = null;
	
	AudioInputStream stonedownstream;
	AudioInputStream normalbonusstream;
	AudioInputStream specialbonusstream;
	
	AudioInputStream mixstream;
	AudioInputStream menustream;
	
	// Sound methods
	public Sounds() {

		File file;
		// Load the various sounds
		try {
			file = new File(stonedownWav);
			stonedownstream = AudioSystem.getAudioInputStream(file);
			stonedown = AudioSystem.getClip();
			stonedown.open(stonedownstream);

			file = new File(normalbonusWav);
			normalbonusstream = AudioSystem.getAudioInputStream(file);
			normalbonus = AudioSystem.getClip();
			normalbonus.open(normalbonusstream);

			file = new File(specialbonusWav);
			specialbonusstream = AudioSystem.getAudioInputStream(file);
			specialbonus = AudioSystem.getClip();
			specialbonus.open(specialbonusstream);

			file = new File(menuMusic);
			menustream = AudioSystem.getAudioInputStream(file);
			menu = AudioSystem.getClip();
			menu.open(menustream);
			
			file = new File(mixMusic);
			mixstream = AudioSystem.getAudioInputStream(file);
			mix = AudioSystem.getClip();
			mix.open(mixstream);

		} catch (Exception ex) {
			System.out.println("Sound : unable to load some sounds "
					+ ex.getLocalizedMessage());
		}
	}

	public void playNormalBonus() {
		if (normalbonus != null && !normalbonus.isRunning()) {
			normalbonus.setFramePosition(0);
			normalbonus.start();
		}
	}

	public void playSpecialBonus() {
		if (specialbonus != null && !specialbonus.isRunning()) {
			specialbonus.setFramePosition(0);
			specialbonus.start();
		}
	}

	public void playStonedown() {
		if (stonedown != null && !stonedown.isRunning()) {
			stonedown.setFramePosition(0);
			stonedown.start();
		}
	}
	
	public void startMenu() {
		if (menu != null && !menu.isRunning()) {
			menu.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	public void stopMenu() {
		if (menu != null && menu.isRunning()) {
			menu.stop();
		}
	}

	public void startMix() {
		if (mix != null && !mix.isRunning()) {
			mix.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	public void stopMix() {
		if (mix != null && mix.isRunning()) {
			mix.stop();
		}
	}
}
