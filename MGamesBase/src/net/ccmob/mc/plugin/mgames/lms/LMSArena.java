package net.ccmob.mc.plugin.mgames.lms;

import java.util.ArrayList;
import java.util.HashMap;

import net.ccmob.math.Utils;
import net.ccmob.math.Vector;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LMSArena {

	private int cubeDiameter, cubeDistance;
	private Vector vectorStart, vectorEnd;
	private int margin = 10;

	private String arenaName = "";
	private String worldName = "";

	private ArrayList<Player> player;
	private HashMap<Player, ItemStack[]> inventories;
	private HashMap<Player, Integer> playerIndecies;
	private ArrayList<Vector> playerPositions;
	private HashMap<Player, Location> playerOriginalPositions;

	public LMSArena() {
		player = new ArrayList<Player>();
		inventories = new HashMap<Player, ItemStack[]>();
		playerIndecies = new HashMap<Player, Integer>();
		playerPositions = new ArrayList<Vector>();
		playerOriginalPositions = new HashMap<Player, Location>();
	}
	
	public void startGame() {
		this.inventories.clear();
		this.playerIndecies = new HashMap<Player, Integer>();
		int i = 0;
		for (Player player : this.player) {
			System.out.println(playerPositions.get(i).getX() + ";" + playerPositions.get(i).getY() + ";" + playerPositions.get(i).getZ());
			inventories.put(player, player.getInventory().getContents().clone());
			playerIndecies.put(player, i);
			playerOriginalPositions.put(player, new Location(player.getLocation().getWorld(),player.getLocation().getX(),player.getLocation().getY(),player.getLocation().getZ(),player.getLocation().getYaw(),player.getLocation().getPitch()));
			player.teleport(new Location(Bukkit.getWorld(this.getWorldName()), playerPositions.get(i).getX() + 0.5f, playerPositions.get(i).getY(), playerPositions.get(i).getZ() + 0.5f));
			player.getInventory().clear();
			i++;
		}
	}

	public void endGame() {
		for (Player player : this.player) {
			player.getInventory().clear();
			player.getInventory().setContents(inventories.get(player));
			player.teleport(playerOriginalPositions.get(player));
		}
		playerOriginalPositions.clear();
		this.inventories.clear();
	}

	public void addPlayer(Player p) {
		if(player.size() < playerPositions.size()){
			this.getPlayer().add(p);
			p.sendMessage(ChatColor.BLUE + "[LMS]" + ChatColor.GREEN + " You joined lms arena - " + this.getArenaName());
		}else{
			p.sendMessage(ChatColor.BLUE + "[LMS]" + ChatColor.RED + " Arena - " + this.getArenaName() + " - is already full.");
		}
	}

	public void removePlayer(Player p) {
		this.getPlayer().remove(p);
		p.sendMessage(ChatColor.BLUE + "[LMS]" + ChatColor.GOLD + " You hav left lms arena - " + this.getArenaName());
	}

	public void rebuild() {
		World w = Bukkit.getWorld(this.getWorldName());
		Vector lower = Vector.min(getVectorStart(), getVectorEnd());
		Vector higher = Vector.max(getVectorStart(), getVectorEnd());
		for (int y = lower.getY(); y < higher.getY(); y++) {
			for (int x = lower.getX(); x < higher.getX(); x++) {
				for (int z = lower.getZ(); z < higher.getZ(); z++) {
					w.getBlockAt(x, y, z).setType(Material.AIR);
				}
			}
		}
		int cnt = 0;
		playerPositions.clear();
		playerPositions = new ArrayList<Vector>();
		final int overflow = 100;
		final int margin = 10;
		while(cnt < overflow){
			int x = Utils.randomInt(lower.getX(), higher.getX());
			int y = Utils.randomInt(lower.getY(), higher.getY());
			int z = Utils.randomInt(lower.getZ(), higher.getZ());
			System.out.println("[LMS|" + this.getArenaName() + "] Trying " + x +"," + y + "," + z);
			boolean flag = true;
			if(x > margin + lower.getX() && y > margin + lower.getY() && z > margin + lower.getZ() && x < higher.getX() + margin && y < higher.getY() + margin && z < higher.getZ() + margin){
				if(playerPositions.size() == 0){
					flag = true;
				}else{
					for(Vector v : playerPositions){
						if(v.getX() >= x + margin || v.getX() <= y + margin){
							if(v.getY() >= y + margin || v.getY() <= y + margin){
								if(v.getZ() >= z + margin || v.getZ() <= z + margin){
									flag = true;
								}else{
									System.out.println("[LMS|" + this.getArenaName() + "] Z-Margin");
									flag = false;
								}
							}else{
								System.out.println("[LMS|" + this.getArenaName() + "] Y-Margin");
								flag = false;
							}
						}else{
							System.out.println("[LMS|" + this.getArenaName() + "] X-Margin");
							flag = false;
						}
					}
				}
			}else{
				System.out.println("[LMS|" + this.getArenaName() + "] Out of bounds");
				flag = false;
			}
			if(flag){
				playerPositions.add(new Vector(x, y, z));
				System.out.println("[LMS|" + this.getArenaName() + "] Adding pos !");
				w.getBlockAt(x, y - 2, z).setType(Material.DIAMOND_BLOCK);
			}
			cnt++;
		}
		player.clear();
	}

	/**
	 * @return the arenaName
	 */
	public String getArenaName() {
		return arenaName;
	}

	/**
	 * @param arenaName
	 *            the arenaName to set
	 */
	public void setArenaName(String arenaName) {
		this.arenaName = arenaName;
	}

	/**
	 * @return the player
	 */
	public ArrayList<Player> getPlayer() {
		return player;
	}

	/**
	 * @param player
	 *            the player to set
	 */
	public void setPlayer(ArrayList<Player> player) {
		this.player = player;
	}

	/**
	 * @return the inventories
	 */
	public HashMap<Player, ItemStack[]> getInventories() {
		return inventories;
	}

	/**
	 * @param inventories
	 *            the inventories to set
	 */
	public void setInventories(HashMap<Player, ItemStack[]> inventories) {
		this.inventories = inventories;
	}

	/**
	 * @return the cubeDiameter
	 */
	public int getCubeDiameter() {
		return cubeDiameter;
	}

	/**
	 * @param cubeDiameter
	 *            the cubeDiameter to set
	 */
	public void setCubeDiameter(int cubeDiameter) {
		this.cubeDiameter = cubeDiameter;
	}

	/**
	 * @return the cubeDistance
	 */
	public int getCubeDistance() {
		return cubeDistance;
	}

	/**
	 * @param cubeDistance
	 *            the cubeDistance to set
	 */
	public void setCubeDistance(int cubeDistance) {
		this.cubeDistance = cubeDistance;
	}

	/**
	 * @return the worldName
	 */
	public String getWorldName() {
		return worldName;
	}

	/**
	 * @param worldName
	 *            the worldName to set
	 */
	public void setWorldName(String worldName) {
		this.worldName = worldName;

	}

	/**
	 * @return the vectorStart
	 */
	public Vector getVectorStart() {
		return vectorStart;
	}

	/**
	 * @param vectorStart
	 *            the vectorStart to set
	 */
	public void setVectorStart(Vector vectorStart) {
		this.vectorStart = vectorStart;
	}

	/**
	 * @return the vectorEnd
	 */
	public Vector getVectorEnd() {
		return vectorEnd;
	}

	/**
	 * @param vectorEnd
	 *            the vectorEnd to set
	 */
	public void setVectorEnd(Vector vectorEnd) {
		this.vectorEnd = vectorEnd;
	}

	/**
	 * @return the margin
	 */
	public int getMargin() {
		return margin;
	}

	/**
	 * @param margin the margin to set
	 */
	public void setMargin(int margin) {
		this.margin = margin;
	}

}
