package net.ccmob.mc.plugin.mgames.base;

import org.bukkit.entity.Player;

public abstract class MGame {

	private String gameName = "";
	
	public MGame(String name) {
		this.setGameName(name);
	}
	
	public abstract boolean handleCommand(String name, String[] args, Player p);

	public abstract void load();
	
	public abstract void unload();
	
	/**
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * @param gameName the gameName to set
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	
	
}
